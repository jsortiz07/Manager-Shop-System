package com.manager.shop.restImpl;

import com.manager.shop.constents.ShopConstants;
import com.manager.shop.rest.UserRest;
import com.manager.shop.service.UserService;
import com.manager.shop.utils.ShopUtils;
import com.manager.shop.wrapper.UserWrapper;
import com.sun.jdi.event.ExceptionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {

    //se llaman las dependencias del servicio user para implementarlo en esta clase a partir del polimorfismo
    @Autowired
    UserService userService;


    // se sobreescribe el metodo implementado de la clase UserRest
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {

        //try cath para la tolerancia a fallas e identificacion de errores de compilacion
        try {
            return userService.signUp(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return ShopUtils.getResponseEntity(ShopConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {

        try {
            return userService.login(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return ShopUtils.getResponseEntity(ShopConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
           return userService.getAllUser();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            return userService.update(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return ShopUtils.getResponseEntity(ShopConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
       try {
           return userService.checkToken();
       }catch (Exception ex){
            ex.printStackTrace();
       }

        return ShopUtils.getResponseEntity(ShopConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
           return userService.changePassword(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return ShopUtils.getResponseEntity(ShopConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }




}
