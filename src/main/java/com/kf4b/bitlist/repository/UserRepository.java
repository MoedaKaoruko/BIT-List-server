package com.kf4b.bitlist.repository;

import com.kf4b.bitlist.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public interface UserRepository extends JpaRepository<User, String> {

    User findById(Integer userId);

    User findByUsername(String username);

    User findByEmail(String email);

}
