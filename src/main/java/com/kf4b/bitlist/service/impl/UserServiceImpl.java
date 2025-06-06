package com.kf4b.bitlist.service.impl;

import com.kf4b.bitlist.entity.User;
import com.kf4b.bitlist.repository.UserRepository;
import com.kf4b.bitlist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Integer userId) {
        String sql = "SELECT * FROM testtab1";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        System.out.println(list);
        User x =  userRepository.findById(userId);
        return x == null ? new User() : x;
    }

    @Override
    public User getUserByUsername(String username) {
        User x =  userRepository.findByUsername(username);
        return x == null ? new User() : x;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = new User();
        user.setEmail("test@email");
        user.setUsername("test");
        user.setPassword("a045f8516fb790bf74fcd65f017d8852");
        user.setUserId(1111);
        return user;

        //return userRepository.findByEmail(email);
    }
}


