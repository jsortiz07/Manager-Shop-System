package com.manager.shop.rest;

import com.manager.shop.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping("/user")
public interface UserRest {

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody(required = true)Map<String,String> requestMap);

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody(required = true)Map<String,String> requestMap);

    @GetMapping("/get")
    public ResponseEntity<List<UserWrapper>>getAllUser();

    @PostMapping("/update")
    public ResponseEntity<String>update(@RequestBody(required = true) Map<String,String> requestMap);

    @GetMapping("/checkToken")
    ResponseEntity<String>checkToken();

    @PostMapping("/changePassword")
    ResponseEntity<String>changePassword(@RequestBody(required =true)Map<String,String>requestMap);

    @PostMapping("/forgotPassword")
    ResponseEntity<String>forgotPassword(@RequestBody(required = true)Map<String,String>requestMap);

}
