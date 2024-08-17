package com.company.neuheathcaremanagement.service;

import com.company.neuheathcaremanagement.email.EmailService;
import com.company.neuheathcaremanagement.pojo.User;
import com.company.neuheathcaremanagement.urlshortenerservice.UrlShortenerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ForgotPasswordService {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UrlShortenerService urlShortenerService;

//	@Value("${ngrok.url}")
//	private String ngrokUrl;

//	public String generateResetToken(String email) {
//	    User user = userService.findUserByEmail(email);
//	    if (user != null) {
//	        String token = java.util.UUID.randomUUID().toString();
//	        userService.updateResetToken(email, token);
//	        
//	        String resetLink = ngrokUrl + "/resetPassword?token=" + token;
//
//	        String shortResetLink = urlShortenerService.shortenUrl(resetLink);
//
//	        String subject = "Password Reset Request";
//	        String body = buildResetEmailBody(user.getName(), shortResetLink); 
//	        emailService.sendAppointmentConfirmation(email, subject, body);
//	        return "A password reset link has been sent to your email.";
//	    } else {
//	        return "Email not found, Please register";
//	    }
//	}

	public String generateResetToken(String email) {
		User user = userService.findUserByEmail(email);
		if (user != null) {
			String token = java.util.UUID.randomUUID().toString();
			userService.updateResetToken(email, token);
			String resetLink = "http://localhost:8080/resetPassword?token=" + token;

			String subject = "Password Reset Request";
			String body = buildResetEmailBody(user.getName(), resetLink); 
			emailService.sendAppointmentConfirmation(email, subject, body);
			return "A password reset link has been sent to your email.";
		} else {
			return "Email not found, Please register";
		}
	}

	private String buildResetEmailBody(String userName, String shortResetLink) {
		return "<html>" + "<body>" + "<p>Dear " + userName + ",</p>"
				+ "<p>We received a request to reset your password. If you did not request this, please ignore this email.</p>"
				+ "<p>To reset your password, please click the link below:</p>" + "<p>" + shortResetLink + "</p>"
				+ "<p>If you have any issues, please contact our support team.</p>"
				+ "<p>Best regards,<br> NEU Student Healthcare Management System</p>" + "</body>" + "</html>";
	}
}
