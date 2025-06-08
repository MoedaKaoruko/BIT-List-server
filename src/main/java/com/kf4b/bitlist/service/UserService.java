package com.kf4b.bitlist.service;
import com.kf4b.bitlist.entity.User;
import javax.persistence.criteria.CriteriaBuilder;
import java.lang.*;

public interface UserService {
    /**
     * 根据用户ID查询用户
     */
    User getUserById(Integer userId);

    /**
     * 根据用户名查询用户
     */
    User getUserByUsername(String username);

    /**
     * 根据邮箱查询用户
     */
    User getUserByEmail(String email);

    // 更新用户信息
    void updateUserById(Integer userId, User user);

    User getUserByHeader(String token);
}
