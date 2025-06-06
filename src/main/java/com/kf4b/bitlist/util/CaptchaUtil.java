package com.kf4b.bitlist.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CaptchaUtil {

    @Autowired
    private JavaMailSender mailSender;

    public final int EXPERIRE_TIME = 600; // 验证码过期时间(单位: 秒)

    public final int INTERVAL_TIME = 60; // 验证码发送间隔时间(单位: 秒)

    // 存储验证码及其生成时间，键为邮箱，值为包含验证码和生成时间的数组
    private final ConcurrentHashMap<String, Object[]> captchaMap = new ConcurrentHashMap<>();

    /**
     * 生成随机验证码
     * @return 验证码字符串
     */
    static public String generateCaptcha() {
        String characters = "0123456789";
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            captcha.append(characters.charAt(index));
        }
        return captcha.toString();
    }

    /**
     * 发送验证码到指定邮箱
     * @param recipientEmail 收件人邮箱地址
     * @param captcha 验证码
     * return 验证码是否
     */
    public boolean sendCaptcha(String recipientEmail, String captcha) {
        if (captchaMap.containsKey(recipientEmail)) {
            return false;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("验证码");
        message.setText("您的验证码是：" + captcha + "，请在"+ EXPERIRE_TIME +"秒内完成验证。");
        mailSender.send(message);
        // 存储验证码及其生成时间
        captchaMap.put(recipientEmail, new Object[]{captcha, System.currentTimeMillis()});
        return true;
    }

    /**
     * 验证验证码是否有效
     * @param recipientEmail 收件人邮箱地址
     * @param inputCaptcha 用户输入的验证码
     * @return 验证码是否有效
     */
    public boolean checkCaptcha(String recipientEmail, String inputCaptcha) {
        Object[] captchaInfo = captchaMap.get(recipientEmail);
        if (captchaInfo == null) {
            return false;
        }
        String storedCaptcha = (String) captchaInfo[0];
        long generateTime = (long) captchaInfo[1];
        // 检查验证码是否过期
        if (System.currentTimeMillis() - generateTime > EXPERIRE_TIME * 1000) {
            captchaMap.remove(recipientEmail);
            return false;
        }
        return storedCaptcha.equals(inputCaptcha);
    }
}