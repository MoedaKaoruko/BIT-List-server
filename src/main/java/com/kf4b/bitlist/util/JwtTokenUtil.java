package com.kf4b.bitlist.util;

import com.kf4b.bitlist.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private static final String secret = "vY3pU2rS2gC7gS6yZ0yP0dF6rY4cC3dF0cF0tQ0kQ0fO5dI2vK2oO5dB0nV6iW4yB9hR3oB6gG9nS3hH3dZ2rN0qF3mB0gS9fM3t";

    private static final int expiration = 360000;

    /**
     * 生成 Token
     * @param username 用户名
     * @return Token 字符串
     */
    public static String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username);
    }

    private static String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 验证 Token
     * @param token Token 字符串
     * @param username 用户名
     * @return 是否验证通过
     */
    public static Boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * 从 Token 中获取用户名
     * @param token Token 字符串
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    private static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public static Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private static Date getExpirationDateFromToken(String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }
}
