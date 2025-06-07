package com.kf4b.bitlist;

import com.kf4b.bitlist.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.kf4b.bitlist.entity.User;
import com.kf4b.bitlist.repository.UserRepository;
import com.kf4b.bitlist.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootTest
class BitListApplicationTests {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() throws Exception {
        User x = new User();
        x.setUserId("1324345ysdg");
        x.setEmail("test@email");
        x.setUsername("test");
        x.setPassword("adsgfadshe");
        userRepository.save(x);
    }

}
