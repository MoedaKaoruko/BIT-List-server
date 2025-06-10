package com.kf4b.bitlist.controller;

import com.kf4b.bitlist.entity.User;
import com.kf4b.bitlist.service.EmailCodeService;
import com.kf4b.bitlist.service.UserService;
import com.kf4b.bitlist.util.HashUtil;
import com.kf4b.bitlist.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailCodeService emailCodeService;

    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody Map<String,Object> params) {
        Map<String,Object> map = new HashMap<>();
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        String email = (String) params.get("email");
        User user = null;
        if (username != null){
            user = userService.getUserByUsername(username);
        }else if (email!=null){
            user = userService.getUserByEmail(email);
        }else {
            map.put("error","用户名或邮箱不能为空");
            return map;
        }
        if(user == null||!HashUtil.checkHash(password,user.getPassword())){
            map.put("error","用户名或密码错误");
            return map;
        }
        try {;
            String token = jwtTokenUtil.generateToken(user.getUsername());
            map.put("token",token);
            map.put("userId",user.getUserId());
        }catch (Exception e){
            map.put("error",e.getMessage());
        }
        return map;
    }

    @PostMapping("/getCaptcha")
    public boolean getCaptcha(@RequestBody Map<String,Object> params) {
        Map<String,Object> map = new HashMap<>();
        String email = (String) params.get("email");
        if(email == null){
            return false;
        }
        // 检查是否可以发送
        int canSendAfter = emailCodeService.canSendCodeAfter(email);
        if (canSendAfter > 0) {
            return false;
        }

        return emailCodeService.sendVerificationCode(email);
    }


    @PostMapping("/verifyCode")
    public Map<String, Object> register(@RequestBody Map<String, Object> requestBody) {
        // 从请求体中提取用户信息
        Map<String, Object> userMap = (Map<String, Object>) requestBody.get("user");
        String password = (String) requestBody.get("password");
        String code = (String) requestBody.get("code");
        String email = (String) userMap.get("email");

        Map<String,Object> response = new HashMap<>();
        //FIXME 此部分功能已经实现，使用前请配置邮箱服务，并取消注释
//        if(!emailCodeService.verifyCode(email, code)){
//            response.put("message", "验证码错误");
//            response.put("success", false);
//            return response;
//        }

        // 创建 User 对象
        User user;
        user = new User();
        user.setUserId((Integer) userMap.get("id"));
        user.setUsername((String) userMap.get("name"));
        user.setEmail((String) userMap.get("email"));
        user.setAvatarUri((String) userMap.get("avatarUri"));
        user.setPassword(HashUtil.getHash(password));
        try {
            userService.updateUserById(-1,user);
            response.put("message", "注册成功");
            response.put("success", true);
        } catch (Exception e) {
            // 注册失败，返回错误信息
            response.put("message", "注册失败: " + e.getMessage());
            response.put("success", false);
        }
        return response;
    }

}
