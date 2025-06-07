package com.kf4b.bitlist.controller;

import com.kf4b.bitlist.entity.User;
import com.kf4b.bitlist.service.UserService;
import com.kf4b.bitlist.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/profile")
    public User getUserProfile(@RequestHeader("Authorization") String token) {
        //String userId = jwtTokenUtil.getUserIdFromToken(token.substring(7));
        //return userService.getUserById(userId);
        return userService.getUserById("1");
    }


}