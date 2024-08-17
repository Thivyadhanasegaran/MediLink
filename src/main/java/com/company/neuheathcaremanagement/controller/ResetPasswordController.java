package com.company.neuheathcaremanagement.controller;


import com.company.neuheathcaremanagement.pojo.User;
import com.company.neuheathcaremanagement.service.ResetPasswordService;
import com.company.neuheathcaremanagement.validator.PasswordResetValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@Controller
public class ResetPasswordController {

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordController.class);

    @Autowired
    private ResetPasswordService resetPasswordService;
    
    @Autowired
	private PasswordResetValidator passwordResetValidator;
    
    @GetMapping("/resetPassword")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        logger.info("Received reset token: {}", token);
        User user = resetPasswordService.findUserByResetToken(token);
        if (user != null) {
            logger.info("Valid reset token received for user: {}", user.getEmailId());
            model.addAttribute("token", token);
            model.addAttribute("user", user);
            return "resetPassword";
        } else {
            logger.warn("Invalid reset token received: {}", token);
            model.addAttribute("message", "Invalid token");
            return "resetPassword";
        }
    }
    
    @PostMapping("/resetPassword")
    public String resetPassword(@ModelAttribute("user") @Validated User user, BindingResult result,@RequestParam("token") String token, @RequestParam("password") String password, Model model) {
  	  passwordResetValidator.validate(user, result);
    
  	    if (result.hasErrors()) {
  	    	model.addAttribute("token", token);
  			model.addAttribute("error", result);
  			return "resetPassword";
  		}
  	   
  	    boolean isResetSuccessful = resetPasswordService.resetPassword(token, password);

        if (isResetSuccessful) {
            logger.info("Password reset successfully.");
            model.addAttribute("message", "Password has been successfully reset. You can now log in.");
            return "resetPassword";
        } else {
            logger.warn("Failed to reset password: Invalid token or user not found.");
            model.addAttribute("message", "Invalid token or user not found.");
            return "resetPassword";
        }
    }
}