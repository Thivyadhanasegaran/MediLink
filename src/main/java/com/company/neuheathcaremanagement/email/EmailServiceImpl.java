package com.company.neuheathcaremanagement.email;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.ByteArrayResource;
import java.io.InputStream;

@Service
public class EmailServiceImpl implements EmailService {
	
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);


    @Autowired
    private JavaMailSender javaMailSender;
    
    @Override
    public void sendAppointmentConfirmation(String to, String subject, String htmlContent) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true); 
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); 
            javaMailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send email to: {}", to, e);
        }
    }
    
    @Override
    public void sendEmailWithAttachment(String to, String subject, String htmlContent, InputStream attachmentStream, String attachmentName) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true); 
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); 

            InputStreamSource attachment = new ByteArrayResource(attachmentStream.readAllBytes());
            helper.addAttachment(attachmentName, attachment);

            javaMailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
        } catch (MessagingException | IOException e) {
            logger.error("Failed to send email to: {}", to, e);
        }
    }
}
