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
        String htmlBody="Hi <span>User</span><br>"+
                        "<p>Thank you for registering with <span>"+companyName +"</span> We're excited to have you on board. Your account is ready, and we look forward to helping you make the most of our services.</p>"+
                        "<p>If you have any questions, feel free to reach out to us at <span>contactjobsagar@gmail.com</span></p>"+
                        "<h3>Welcome aboard!</h3><br>" +
                        "<h3>"+companyName+"</h3>"+
                        "<h4>"+to+"</h4>";

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
        String htmlBody = "Hi <span>" + "userName" + "</span><br>" +
                "<p>Thank you for registering with <span>" + companyName + "</span>. We're thrilled to have you on board!</p>" +
                "<p>To complete your registration, please use the following OTP:</p>" +
                "<h2 style='color: #4CAF50; text-align: center;'>" + otp + "</h2>" +
                "<p style='color: #555;'>This OTP is valid for the next <strong>10 minutes</strong>. If you did not request this, please ignore this email or contact our support team.</p>" +
                "<br>" +
                "<p>Need help? Reach out to us at <span>contactjobsagar@gmail.com</span>.</p>" +
                "<h3>Welcome aboard!</h3><br>" +
                "<h3>" + companyName + "</h3>";

        helper.setText(htmlBody,true);
        mailSender.send(message);
        return otp;
    }

}
