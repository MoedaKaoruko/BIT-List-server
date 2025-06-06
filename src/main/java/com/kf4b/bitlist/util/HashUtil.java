package com.kf4b.bitlist.util;

import org.springframework.util.DigestUtils;


public class HashUtil {
    public final static String salt="1145141919810";

    public static  String  getHash(String password) {
        String hashedPwd = DigestUtils.md5DigestAsHex((password + salt).getBytes());
        hashedPwd = DigestUtils.md5DigestAsHex((hashedPwd + salt).getBytes()); // 第一次加密
        hashedPwd = DigestUtils.md5DigestAsHex((hashedPwd + salt).getBytes()); // 第二次加密
        return hashedPwd;
    }

    public static boolean checkHash(String password, String hashedPassword) {
        return getHash(password).equals(hashedPassword);
    }

    public static void main(String[] args) {
        System.out.println(getHash("123456"));
    }
}
