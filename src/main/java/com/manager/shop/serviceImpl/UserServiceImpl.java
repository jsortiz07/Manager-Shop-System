package com.manager.shop.serviceImpl;

import com.manager.shop.constents.ShopConstants;
import com.manager.shop.dao.UserDao;
import com.manager.shop.model.User;
import com.manager.shop.service.UserService;
import com.manager.shop.utils.ShopUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Se ha logueado {}", requestMap);
        try {
            if(validateSignUpMap(requestMap)){

                User user = userDao.findByEmailId(requestMap.get("email"));
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

    private boolean validateSignUpMap(Map<String, String> requestMap) {
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
}
