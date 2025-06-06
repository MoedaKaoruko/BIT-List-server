package com.kf4b.bitlist.service.impl;

import com.kf4b.bitlist.entity.User;
import com.kf4b.bitlist.repository.UserRepository;
import com.kf4b.bitlist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(String userId) {
        User user = new User();
        user.setEmail("test@email");
        user.setUsername("test");
        user.setPassword("a045f8516fb790bf74fcd65f017d8852");
        user.setUserId("bd65bf65-cf3f-4d1c-b902-ddce3f944c34");
        return user;
        //return userRepository.findById(userId).orElse(new User());
    }

    @Override
    public User getUserByUsername(String username) {
        User user = new User();
        user.setEmail("test@email");
        user.setUsername("test");
        user.setPassword("a045f8516fb790bf74fcd65f017d8852");
        user.setUserId("bd65bf65-cf3f-4d1c-b902-ddce3f944c34");
        return user;

        //return userRepository.findByUsername(username);
    }

    @Override
    public User getUserByEmail(String email) {
        User user = new User();
        user.setEmail("test@email");
        user.setUsername("test");
        user.setPassword("a045f8516fb790bf74fcd65f017d8852");
        user.setUserId("bd65bf65-cf3f-4d1c-b902-ddce3f944c34");
        return user;

        //return userRepository.findByEmail(email);
    }

}
