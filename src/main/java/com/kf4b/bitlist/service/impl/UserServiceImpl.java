package com.kf4b.bitlist.service.impl;

import com.kf4b.bitlist.entity.User;
import com.kf4b.bitlist.repository.UserRepository;
import com.kf4b.bitlist.service.UserService;
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
        u.setBirth(user.getBirth());
        u.setLoginState(user.getLoginState());
        u.setAvatarUri(user.getAvatarUri());
        u.setPassword(user.getPassword());
        userRepository.save(u);
    }
}


