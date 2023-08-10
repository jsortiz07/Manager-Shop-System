package com.manager.shop.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ShopUtils {

    private ShopUtils() {

    }
    // metodo para instanciar los mensajes en las respuestas de cada peticion
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {

        return new ResponseEntity<>("{\"message\":\"" + responseMessage + "\"}", httpStatus);

    }
}