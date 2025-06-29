package com.kf4b.bitlist.service.impl;

import com.kf4b.bitlist.entity.User;
import com.kf4b.bitlist.repository.UserRepository;
import com.kf4b.bitlist.service.UserService;
import com.kf4b.bitlist.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.*;
import java.lang.*;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Integer userId) {
        Optional<User> a =  userRepository.findById(userId);
        if(a.isPresent()){
            User x = a.get();
            return x;
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        User x =  userRepository.findByUsername(username);
        return x == null ? null : x;
    }

    @Override
    public User getUserByEmail(String email) {
        User x = userRepository.findByEmail(email);
        return x == null ? null : x;
    }

    @Transactional
    public void updateUserById(Integer userId, User user){
        Optional<User> us = userRepository.findById(userId);
        User u = us.isPresent() ? us.get() : new User();
        u.setEmail(user.getEmail());
        u.setUsername(user.getUsername());
        u.setGrade(user.getGrade());
        u.setBirth(user.getBirth());
        u.setStuId(user.getStuId());
        u.setLoginState(user.getLoginState());
        u.setAvatarUri(user.getAvatarUri());
        u.setPassword(user.getPassword());
        u.setSchool(user.getSchool());
        userRepository.save(u);
    }

    @Override
    public User getUserByHeader(String header) {
        String username = null;
        String jwtToken = null;

        if (header != null && header.startsWith("Bearer ")) {
            jwtToken = header.substring(7);
            try {
                if(!JwtTokenUtil.isTokenExpired(jwtToken)){
                    username = JwtTokenUtil.getUsernameFromToken(jwtToken);
                    return userRepository.findByUsername(username);
                }
            } catch (Exception e) {
                System.out.println("Unable to get JWT Token");
            }
        } else {
            System.out.println("JWT Token does not begin with Bearer String");
        }
        return null;
    }
}


