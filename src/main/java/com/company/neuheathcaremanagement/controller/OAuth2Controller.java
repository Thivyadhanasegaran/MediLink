package com.company.neuheathcaremanagement.controller;

import com.company.neuheathcaremanagement.pojo.User;
import com.company.neuheathcaremanagement.service.UserService;
import com.company.neuheathcaremanagement.validator.UserValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class OAuth2Controller {

    @Autowired
    private UserService userService;
    
    @Autowired
	private UserValidator userValidator;
    
    @GetMapping("/oauth2userlogin")
    @Operation(summary = "OAuth2 user login", description = "Handles login for users authenticated via OAuth2")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User logged in successfully"),
        @ApiResponse(responseCode = "400", description = "Failed to retrieve user details from OAuth2 login"),
        @ApiResponse(responseCode = "401", description = "User account is inactive")
    })
    public String oauth2login(Model model, HttpSession session, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication: " + authentication);
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            System.out.println("OAuth2 User Attributes: " + oAuth2User.getAttributes());

            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");

            User user = new User();
            boolean isExist = userService.userExists(email);
            if (isExist) {
                user = userService.findUserByEmail(email);
            } else {
                user.setEmailId(email);
                user.setName(name);
                user.setUserRole(null); 
                session.setAttribute("newUser", user); 
            }

            session.setAttribute("loginuser", user);
            session.setAttribute("userName", user.getName());
            session.setAttribute("userRole", user.getUserRole());

            if (user.getUserRole() == null) {
                model.addAttribute("user", user);
                return "userRole";
            }

            if (user.isActive()) {
                switch (user.getUserRole()) {
                    case "doctor":
                        return "redirect:doctor/doctorHomePage";
                    case "patient":
                        return "redirect:patient/patientHomePage";
                    case "admin":
                        return "redirect:admin/adminDashboard";
                    default:
                        model.addAttribute("errors", "Invalid user role.");
                        return "userRole";
                }
            } else {
                model.addAttribute("errors", "Your account is inactive. Please contact admin support.");
                session.invalidate();
                return "login";
            	
            }
        } else {
            model.addAttribute("errors", "Failed to retrieve user details from OAuth2 login");
            return "login";
        }
    }
    
    @GetMapping("/oauth2userlogin/userRole")
    public String showUserRoleForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loginuser");
        if (user != null && user.getUserRole() == null) {
            model.addAttribute("user", user);
            return "userRole";
        } else {
            model.addAttribute("errors", "Failed to retrieve user details. Please login again.");
            return "login";
        }
    }

    @PostMapping("/oauth2userlogin")
    public String saveUserRole(@ModelAttribute("user") User user, BindingResult result, HttpSession session, Model model) {
    	userValidator.validateUserRole(user, result);

		if (result.hasErrors()) {
			model.addAttribute("error", result);
			return "userRole";
		}
		
		User sessionUser = (User) session.getAttribute("loginuser");
        if (sessionUser != null) {

            sessionUser.setUserRole(user.getUserRole());
            sessionUser.setSpecialization(user.getSpecialization());
            userService.saveUserWithoutPassword(sessionUser);
            
            session.setAttribute("loginuser", sessionUser);
            session.setAttribute("userRole", sessionUser.getUserRole());
            session.setAttribute("neuid", sessionUser.getNeuid());

            if (sessionUser.isActive()) {
                switch (sessionUser.getUserRole()) {
                    case "doctor":
                        return "redirect:doctor/doctorHomePage";
                    case "patient":
                        return "redirect:patient/patientHomePage";
                    case "admin":
                        return "redirect:admin/adminDashboard";
                    default:
                        model.addAttribute("errors", "Invalid user role.");
                        return "userRole";
                }
            } else {
            	
                model.addAttribute("errors", "Your account is inactive. Please contact admin support.");
                session.invalidate();
                return "login";
            }
        } else {
            model.addAttribute("errors", "Failed to retrieve user details. Please login again.");
            return "login";
        }
    }
}
