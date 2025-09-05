package com.example.SagarNaukri.com.EmailServices;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;


    // Email Using Inline
    public void getAfterRegistrationMail(String to, String companyName) throws Exception{
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);
        helper.setFrom("contactjobsagar@gmail.com");
        helper.setTo(to);
        helper.setSubject("Welcome to "+companyName);

        String htmlBody = "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "  <meta charset='UTF-8'>" +
                "  <title>Welcome - " + companyName + "</title>" +
                "  <style>" +
                "    body { font-family: Arial, sans-serif; background-color: #f4f4f5; margin: 0; padding: 40px 0; }" +
                "    .container { max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px;" +
                "                box-shadow: 0 0 10px rgba(0,0,0,0.05); padding: 40px; text-align: center; }" +
                "    .logo { font-size: 28px; font-weight: bold; color: #0077cc; margin-bottom: 20px; }" +
                "    p { font-size: 16px; color: #555; margin: 15px 0; line-height: 1.5; }" +
                "    h3, h4 { color: #333; margin: 10px 0; }" +
                "    span { font-weight: bold; color: #333; }" +
                "    .footer { margin-top: 30px; font-size: 12px; color: #aaa; }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='logo'>" + companyName + "</div>" +
                "    <p>Hi <span>User</span>,</p>" +
                "    <p>Thank you for registering with <span>" + companyName + "</span>. We're excited to have you on board. Your account is ready, and we look forward to helping you make the most of our services.</p>" +
                "    <p>If you have any questions, feel free to reach out to us at <span>contactjobsagar@gmail.com</span>.</p>" +
                "    <h3>Welcome aboard!</h3>" +
                "    <h3>" + companyName + "</h3>" +
                "    <h4>" + to + "</h4>" +
                "    <div class='footer'>© 2025 " + companyName + ", All rights reserved.</div>" +
                "  </div>" +
                "</body>" +
                "</html>";


        helper.setText(htmlBody,true);
        mailSender.send(message);
    }

    public String sendOTP(String email,String companyName) throws Exception{
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);

        // OTP Generate Using Random Package
        Random random=new Random();
        String otp=random.nextInt(111111,999999)+"";
        System.out.println("OTP is "+otp);

        helper.setFrom("contactjobsagar@gmail.com");
        helper.setTo(email);
        helper.setSubject("Welcome to "+companyName);

        String htmlBody = "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "  <meta charset='UTF-8'>" +
                "  <title>Verify Your Email - JobSagar</title>" +
                "  <style>" +
                "    body { font-family: Arial, sans-serif; background-color: #f5f7fa; margin: 0; padding: 40px 0; }" +
                "    .container { max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px;" +
                "                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08); padding: 40px; text-align: center; }" +
                "    .logo { font-size: 30px; font-weight: bold; color: #0077cc; margin-bottom: 20px; }" +
                "    h1 { font-size: 24px; color: #222; margin-bottom: 16px; }" +
                "    p { font-size: 16px; color: #555; margin: 14px 0; line-height: 1.5; }" +
                "    .otp-box { font-size: 28px; font-weight: bold; letter-spacing: 6px; background-color: #f0f0f0;" +
                "               padding: 15px 25px; border-radius: 8px; display: inline-block; margin: 20px 0; color: #222; }" +
                "    .footer { margin-top: 40px; font-size: 12px; color: #999; line-height: 1.4; }" +
                "    .footer a { color: #0077cc; text-decoration: none; }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='logo'>" + companyName + "</div>" +
                "    <h1>Verify Your Email</h1>" +
                "    <p>Hi <strong>User!</strong></p>" +
                "    <p>Thank you for registering with <strong>" + companyName + "</strong>. We're excited to have you on board.</p>" +
                "    <p>To complete your registration, please use the OTP below:</p>" +
                "    <div class='otp-box'>" + otp + "</div>" +
                "    <p>This OTP will expire in <strong>10 minutes</strong>.</p>" +
                "    <p>If you did not request this, please ignore this email or contact our support team.</p>" +
                "    <p>Need help? Reach out at <a href='mailto:contactjobsagar@gmail.com'>contactjobsagar@gmail.com</a></p>" +
                "    <p><strong>Welcome aboard!</strong><br><strong>" + companyName + "</strong></p>" +
                "    <div class='footer'>" +
                "      © 2025 " + "SagarNaukri" + ", All rights reserved.<br>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";


        helper.setText(htmlBody,true);
        mailSender.send(message);
        return otp;
    }
    public String getPasswordResetOTP(String email,String companyName) throws Exception{
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);

        // OTP Generate Using Random Package
        Random random=new Random();
        String otp=random.nextInt(111111,999999)+"";
        System.out.println("OTP is "+otp);

        helper.setFrom("contactjobsagar@gmail.com");
        helper.setTo(email);
        helper.setSubject("Welcome to "+companyName);
//        String htmlBody = "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>" +
//                "<h2>Hi User,</h2>" +
//                "<p>We received a request to reset your password for your account at <strong>" + companyName + "</strong>.</p>" +
//                "<p>Please use the following One-Time Password (OTP) to reset your password:</p>" +
//                "<h2 style='color: #4CAF50; text-align: center;'>" + otp + "</h2>" +
//                "<p style='color: #555;'>This OTP is valid for the next <strong>10 minutes</strong>. If you did not request this, please ignore this email or contact our support team immediately.</p>" +
//                "<br>" +
//                "<p>Need help? Reach out to us at <a href='mailto:contactjobsagar@gmail.com'>contactjobsagar@gmail.com</a>.</p>" +
//                "<br>" +
//                "<h3>Thank you,</h3>" +
//                "<h3>The " + companyName + " Team</h3>" +
//                "</div>";

        String htmlBody = "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "  <meta charset='UTF-8'>" +
                "  <title>Password Reset - " + companyName + "</title>" +
                "  <style>" +
                "    body { font-family: Arial, sans-serif; background-color: #f4f4f5; margin: 0; padding: 40px 0; }" +
                "    .container { max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px;" +
                "                box-shadow: 0 0 10px rgba(0,0,0,0.05); padding: 40px; text-align: center; }" +
                "    .logo { font-size: 26px; font-weight: bold; color: #0077cc; margin-bottom: 20px; }" +
                "    h2, h3 { color: #222; margin: 16px 0; }" +
                "    p { font-size: 16px; color: #555; margin: 12px 0; line-height: 1.6; }" +
                "    .otp-box { font-size: 28px; font-weight: bold; letter-spacing: 6px; background-color: #f0f0f0;" +
                "               padding: 15px 25px; border-radius: 8px; display: inline-block; margin: 20px 0; color: #222; }" +
                "    a { color: #0077cc; text-decoration: none; }" +
                "    .footer { margin-top: 30px; font-size: 12px; color: #aaa; }" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class='container'>" +
                "    <div class='logo'>" + companyName + "</div>" +
                "    <h2>Hi User,</h2>" +
                "    <p>We received a request to reset your password for your account at <strong>" + companyName + "</strong>.</p>" +
                "    <p>Please use the following One-Time Password (OTP) to reset your password:</p>" +
                "    <div class='otp-box'>" + otp + "</div>" +
                "    <p>This OTP is valid for the next <strong>10 minutes</strong>.<br>If you did not request this, please ignore this email or contact our support team immediately.</p>" +
                "    <p>Need help? Reach out to us at <a href='mailto:contactjobsagar@gmail.com'>contactjobsagar@gmail.com</a></p>" +
                "    <h3>Thank you,</h3>" +
                "    <h3>The " + companyName + " Team</h3>" +
                "    <div class='footer'>© 2025 " + companyName + ", All rights reserved.</div>" +
                "  </div>" +
                "</body>" +
                "</html>";

        helper.setText(htmlBody,true);
        mailSender.send(message);
        return otp;
    }

}

