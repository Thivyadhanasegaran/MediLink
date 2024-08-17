package com.company.neuheathcaremanagement.controller;

import com.company.neuheathcaremanagement.pojo.User;
import com.company.neuheathcaremanagement.service.ForgotPasswordService;
import com.company.neuheathcaremanagement.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ForgotPasswordController {

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

    @Autowired
    private ForgotPasswordService forgotPasswordService;
    
    @Autowired
	private UserValidator userValidator;
    
    @GetMapping("/forgotPassword")
    public String forgotPasswordPage(Model model) {
        logger.info("Accessed forgot password page");
        model.addAttribute("user", new User());
        return "forgotPassword";
    }  
    
    @PostMapping("/forgotPassword")
    public String sendResetLink(@ModelAttribute("user") User user, BindingResult result, Model model) {
        userValidator.validateResetPassword(user.getEmailId(), result);

        if (result.hasErrors()) {
            model.addAttribute("error", result);
            return "forgotPassword";
        }

        String message = forgotPasswordService.generateResetToken(user.getEmailId());
        model.addAttribute("message", message);
        logger.info("Forgot password process completed for email: {}", user.getEmailId());
        return "forgotPassword";
    } 
}
