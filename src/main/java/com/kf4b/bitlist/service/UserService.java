package com.kf4b.bitlist.service;

import com.kf4b.bitlist.entity.User;

import javax.persistence.criteria.CriteriaBuilder;

public interface UserService {
    /**
     * 根据用户ID查询用户
     */
    User getUserById(String userId);

    /**
     * 根据用户名查询用户
     */
    User getUserByUsername(String username);

    /**
     * 根据邮箱查询用户
     */
    User getUserByEmail(String email);
}
