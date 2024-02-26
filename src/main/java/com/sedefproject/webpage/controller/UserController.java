package com.sedefproject.webpage.controller;

import com.sedefproject.webpage.businnes.concretes.UserService;
import com.sedefproject.webpage.request.ChangePasswordRequest;
import com.sedefproject.webpage.request.ResetPasswordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
            return userService.changePassword(request);
        }

    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPasswordSend(@RequestBody String email){
        return userService.forgetPasswordSend(email);
    }
    //link tıklandıgında direk post işlemi olmuyor. get isteği atılıyor. biz kendimiz post edicez. :)
    @PostMapping(path = "/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestBody ResetPasswordRequest request) {
        return userService.resetPassword(token,request);
    }

}
