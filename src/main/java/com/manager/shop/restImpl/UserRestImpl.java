package com.manager.shop.restImpl;

import com.manager.shop.constents.ShopConstants;
import com.manager.shop.rest.UserRest;
import com.manager.shop.service.UserService;
import com.manager.shop.utils.ShopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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


}
