package com.manager.shop.serviceImpl;

import com.manager.shop.JWT.CustomerUsersDetailsService;
import com.manager.shop.JWT.JwtFilter;
import com.manager.shop.JWT.JwtUtil;
import com.manager.shop.constents.ShopConstants;
import com.manager.shop.dao.UserDao;
import com.manager.shop.model.User;
import com.manager.shop.service.UserService;
import com.manager.shop.utils.EmailUtils;
import com.manager.shop.utils.ShopUtils;
import com.manager.shop.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Se ha logueado {}", requestMap);
        try {
            if(validateSignUpMap(requestMap)){

                User user = userDao.findByEmailId(requestMap.get("email"));
                //si no se obtiene el correo se inserta el registro
                if(Objects.isNull(user)){
                    userDao.save(getUserFromMap(requestMap));

                    return ShopUtils.getResponseEntity("El registro ha sido exitoso", HttpStatus.OK);
                }else{
                    System.out.println("eL CORREO YA EXISTE");
                    return ShopUtils.getResponseEntity("El correo ya existe", HttpStatus.BAD_REQUEST);
                }
            }else{
                System.out.println("Datos invalidos");
                return  ShopUtils.getResponseEntity(ShopConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return ShopUtils.getResponseEntity(ShopConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
            );
            if (auth.isAuthenticated()){
                if (customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\""+
                           jwtUtil.generateToken(customerUsersDetailsService.getUserDetail().getEmail(),
                                   customerUsersDetailsService.getUserDetail().getRole()) + "\"}",
                    HttpStatus.OK);
                }else{
                    return new ResponseEntity<String>("{\"message\":\""+"En espera aprobacion del admin"+"\"}",HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception ex){
            log.info("{}",ex);
        }
        //el mensaje que retorna se ingresa en estructura de cadena JSON
        return new ResponseEntity<String>("{\"message\":\""+"Error con las credenciales"+"\"}",HttpStatus.BAD_REQUEST);

    }



    private boolean validateSignUpMap(Map<String, String> requestMap) {
        // Sa valida si el coleccionador MAP tiene las etiquetas correspondientes a cada dato
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")){
            return true;
        }else{
            return false;
        }
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {

            if (jwtFilter.isAdmin()){
                return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);

            }else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            //solo si tiene rol admin (se extrae desde los claims)
            if (jwtFilter.isAdmin()){
              Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
                //si existe el usuario se ejecuta la actualizacion de estado
              if(!optional.isEmpty()){
                    userDao.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),userDao.getAllAdmin());
                    return ShopUtils.getResponseEntity("Se actualiza estado de usuario con exito",HttpStatus.OK);
              }else {
                    return ShopUtils.getResponseEntity("El usuario id no existe, por favor registrese",HttpStatus.OK);
              }
            }else {
                return ShopUtils.getResponseEntity(ShopConstants.UNAUTHORIZED_ACCES,HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("aqui llega ");
        return ShopUtils.getResponseEntity(ShopConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }



    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        //se envia correo para habilitacion y deshabilitacion
        if (status!=null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Aprobacion cuenta","USER:-"+user+"\n es aprobado por el admin:"+jwtFilter.getCurrentUser(),allAdmin);
        }else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Deshabilitacion cuenta","USER:-"+user+"\n es deshabilitada por el admin: "+jwtFilter.getCurrentUser(),allAdmin);

        }
    }

    @Override
    public ResponseEntity<String> checkToken() {

        return ShopUtils.getResponseEntity("true",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());
            if (!userObj.equals(null)){
                if (userObj.getPassword().equals(requestMap.get("oldPassword"))){
                    userObj.setPassword(requestMap.get("newPassword"));
                    userDao.save(userObj);

                    return ShopUtils.getResponseEntity("Contraseña actualizada correctamente",HttpStatus.OK);

                }
                return ShopUtils.getResponseEntity("La contraseña actual es incorrecta",HttpStatus.BAD_REQUEST);
            }
            return ShopUtils.getResponseEntity(ShopConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return ShopUtils.getResponseEntity(ShopConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }


}
