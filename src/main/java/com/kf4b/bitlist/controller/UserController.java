package com.kf4b.bitlist.controller;

import com.kf4b.bitlist.entity.MindedItem;
import com.kf4b.bitlist.entity.User;
import com.kf4b.bitlist.service.MindedItemService;
import com.kf4b.bitlist.service.UserService;
import com.kf4b.bitlist.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MindedItemService mindedItemService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 获取用户信息
     */
    @GetMapping("/profile")
    public User getUserProfile(@RequestHeader("Authorization") String token) {
        //String userId = jwtTokenUtil.getUserIdFromToken(token.substring(7));
        //return userService.getUserById(userId);
        return userService.getUserById(1);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public boolean updateUserProfile(@RequestHeader("Authorization") String token,
                                  @RequestBody Map<String, Object> requestMap) {
        User user = userService.getUserByHeader(token);
        if(!user.getUserId().equals(requestMap.get("userId"))){
            return false;
        }
        if(requestMap.containsKey("username")) {
            user.setUsername((String) requestMap.get("username"));
        }
        if(requestMap.containsKey("email")) {
            user.setEmail((String) requestMap.get("email"));
        }
        if(requestMap.containsKey("grade")) {
            //user.set((String) requestMap.get("grade"));
        }
        if(requestMap.containsKey("birth")) {
            user.setBirth((Date) requestMap.get("birth"));
        }
        if(requestMap.containsKey("stuId")) {
            //user.setStuId((String) requestMap.get("stuId"));
        }
        if(requestMap.containsKey("avatarUri")) {
            user.setAvatarUri((String) requestMap.get("avatarUri"));
        }
        userService.updateUserById(user.getUserId(), user);
        return true;
    }

    /**
     * 更改密码
     */
    @PutMapping("/changePassword")
    public boolean changePassword(@RequestHeader("Authorization") String token,
                                  @RequestBody Map<String, Object> requestMap) {
        User user = userService.getUserByHeader(token);
        if(!user.getUserId().equals(requestMap.get("userId"))){
            return false;
        }
        if(requestMap.containsKey("oldPassword") && requestMap.containsKey("newPassword")) {
            if(!user.getPassword().equals(requestMap.get("oldPassword"))){
                return false;
            }
            user.setPassword((String) requestMap.get("newPassword"));
        }
        return true;
    }

    @GetMapping("/stats")
    public Map<String, Object> getUserStats(@RequestHeader("Authorization") String token) {
        //String userId = jwtTokenUtil.getUserIdFromToken(token.substring(7));
        //return userService.getUserStats(userId);
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    @GetMapping("/focus")
    public List<MindedItem> getUserMindedItem(@RequestHeader("Authorization") String token) {
        User user = userService.getUserByHeader(token);
        return mindedItemService.getMindedItemByUserId(user.getUserId());
    }

}