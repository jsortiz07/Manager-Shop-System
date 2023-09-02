package com.manager.shop.service;

import com.manager.shop.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<String> signUp(Map<String,String>requestMap);

    ResponseEntity<String> login(Map<String,String>requestMap);

    ResponseEntity<List<UserWrapper>> getAllUser();

    ResponseEntity<String>update(Map<String,String>requestMap);

}
