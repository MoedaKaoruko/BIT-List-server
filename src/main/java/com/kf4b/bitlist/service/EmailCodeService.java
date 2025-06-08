package com.kf4b.bitlist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailCodeService {

    private final Cache<String, String> emailCodeCache;
    private final Cache<String, Long> emailSendTimeCache;

    // 装配
    @Autowired
    public EmailCodeService(@Qualifier("emailCodeCache") Cache<String, String> emailCodeCache,
                            @Qualifier("emailSendTimeCache") Cache<String, Long> emailSendTimeCache) {
        this.emailCodeCache = emailCodeCache;
        this.emailSendTimeCache = emailSendTimeCache;
    }

    @Autowired
    private JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String mailFrom;

    /**
     * 发送验证码（带60秒限流）
     * @param email 用户邮箱
     * @return 发送结果（true成功，false被限流）
     */
    public boolean sendVerificationCode(String email) {
        // 检查60秒内是否已发送
        Long lastSendTime = emailSendTimeCache.getIfPresent(email);
        if (lastSendTime != null) {
            long currentTime = System.currentTimeMillis();
            long secondsSinceLastSend = (currentTime - lastSendTime) / 1000;

            // 60秒内禁止重复发送
            if (secondsSinceLastSend < 60) {
                return false;
            }
        }

        // 生成6位随机验证码
        String code = String.format("%06d", (int) (Math.random() * 1000000));;

        // 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(email);
        message.setSubject("验证码");
        message.setText("您的验证码是：" + code + "，请在10分钟内完成验证。");
        mailSender.send(message);

        // 缓存验证码（10分钟有效）
        emailCodeCache.put(email, code);

        // 记录发送时间（用于限流）
        emailSendTimeCache.put(email, System.currentTimeMillis());

        return true;
    }

    /**
     * 验证用户输入的验证码
     * @param email 用户ID
     * @param inputCode 用户输入的验证码
     * @return 验证结果
     */
    public boolean verifyCode(String email, String inputCode) {
        if (inputCode == null || inputCode.trim().isEmpty()) {
            return false;
        }

        // 获取缓存的验证码
        String cachedCode = emailCodeCache.getIfPresent(email);

        // 验证码不存在或已过期
        if (cachedCode == null) {
            return false;
        }

        // 忽略大小写比较
        if (cachedCode.equalsIgnoreCase(inputCode.trim())) {
            // 验证成功后立即清除验证码
            emailCodeCache.invalidate(email);
            return true;
        }

        return false;
    }

    /**
     * 检查用户是否可以发送验证码（前端可调用）
     * @param userId 用户ID
     * @return 距离下次可发送的秒数（0表示可以发送）
     */
    public int canSendCodeAfter(String userId) {
        Long lastSendTime = emailSendTimeCache.getIfPresent(userId);
        if (lastSendTime == null) {
            return 0;
        }

        long currentTime = System.currentTimeMillis();
        long secondsPassed = (currentTime - lastSendTime) / 1000;

        return secondsPassed < 60 ? (int) (60 - secondsPassed) : 0;
    }
}
