package com.company.neuheathcaremanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.company.neuheathcaremanagement.service.UserService;
import com.company.neuheathcaremanagement.pojo.User;
import com.company.neuheathcaremanagement.validator.UserValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
@Tag(name = "User Management System", description = "User management operations")

public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserValidator userValidator;


	@GetMapping("/login")
	@Operation(summary = "Display login page")

	public String loginPage() {
		logger.info("Reached login page");
		return "login";
	}
	
	@GetMapping("/registerForm")
	@Operation(summary = "Display registration form")
	public String registerPage(Model model) {
		logger.info("Reached register form page");
		model.addAttribute("user", new User());
		return "registrationForm";
	}

	@GetMapping("patient/patientHomePage")
    @Operation(summary = "Display patient home page")
	public String patientHomePage() {
		return "patientHomePage";
	}

	@GetMapping("doctor/doctorHomePage")
    @Operation(summary = "Display doctor home page")
	public String doctorHomePage() {
		return "doctorHomePage";
	}

	@PostMapping("/registerForm")
    @Operation(summary = "Register a new user")
	public String registerUser(@ModelAttribute("user") @Validated User user, BindingResult result, Model model) {
		userValidator.validate(user, result);

		if (result.hasErrors()) {
			model.addAttribute("error", result);
			return "registrationForm";
		}

		boolean isExist = userService.userExists(user.getEmailId());

		if (isExist) {
			logger.info("Attempt to register with existing email: {}", user.getEmailId());
			model.addAttribute("errors", "Email ID already exists. Please use a different email.");
			return "registrationForm";
		}
	    
		userService.saveUser(user);
		logger.info("User registered successfully with email: {}", user.getEmailId());
		model.addAttribute("successMessage", "User registered successfully!");
		return "registrationForm";
	}
	
	
	@PostMapping("/userlogin")
    @Operation(summary = "Authenticate user login")
	public String loginUser(@RequestParam("emailid") String email, @RequestParam("password") String password,
	                        HttpSession session, Model model) {

	    if (email == null || email.trim().isEmpty()) {
	    	model.addAttribute("emailid", email);
	        model.addAttribute("errors", "Email cannot be empty.");
	        logger.warn("Login attempt with empty email.");
	        return "login";
	    }
	    if (password == null || password.trim().isEmpty()) {
	    	model.addAttribute("emailid", email);
	        model.addAttribute("errors", "Password cannot be empty.");
	        logger.warn("Login attempt with empty password for email: {}", email);
	        return "login";
	    }

	    String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	    if (!email.matches(emailRegex)) {
	    	model.addAttribute("emailid", email);
	        model.addAttribute("errors", "Invalid email format.");
	        logger.warn("Login attempt with invalid email format: {}", email);
	        return "login";
	    }

	    User user = userService.loginUser(email, password);

	    if (user != null) {
	        if (user.isActive()) {
	            session.setAttribute("loginuser", user);
	            session.setAttribute("userName", user.getName());
	            session.setAttribute("userRole", user.getUserRole());

	            logger.info("User logged in: {} with role: {}", user.getName(), user.getUserRole());

	            switch (user.getUserRole()) {
	                case "doctor":
	                    return "redirect:doctor/doctorHomePage";
	                case "patient":
	                    return "redirect:patient/patientHomePage";
	                case "admin":
	                    return "redirect:admin/adminDashboard";
	                default:
	                    model.addAttribute("errors", "Invalid user role.");
	                    logger.error("Invalid user role for user: {}", user.getName());
	                    return "error";
	            }
	        } else {
	            logger.warn("Inactive account login attempt for email: {}", email);
	            model.addAttribute("errors", "Your account is inactive. Please contact admin support.");
	            model.addAttribute("emailid", email); 
	            return "login";
	        }
	    } else {
	        User existingUser = userService.findUserByEmail(email);
	        if (existingUser == null) {
	            model.addAttribute("errors", "User not registered.");
	        } else {
	            model.addAttribute("errors", "Invalid email or password.");
	        }
	        logger.warn("Invalid login attempt for email: {}", email);
	        model.addAttribute("emailid", email); 
	        return "login";
	    }
	}

	
	@GetMapping("/patient/managePersonalDetails")
    @Operation(summary = "Display personal details management page")
	public String managePatientPersonalDetails(HttpSession session, Model model) {
		User loginUser = (User) session.getAttribute("loginuser");
		User user = userService.findUserByEmail(loginUser.getEmailId());
		model.addAttribute("user", user);
		model.addAttribute("userRole", user.getUserRole());
		return "managePatientPersonalDetails";
	}
	
	@GetMapping("/doctor/managePersonalDetails")
    @Operation(summary = "Display personal details management page")
	public String manageDoctorPersonalDetails(HttpSession session, Model model) {
		User loginUser = (User) session.getAttribute("loginuser");
		User user = userService.findUserByEmail(loginUser.getEmailId());
		model.addAttribute("user", user);
		model.addAttribute("userRole", user.getUserRole());
		return "manageDoctorPersonalDetails";
	}

	@PostMapping("/patient/managePersonalDetails")
    @Operation(summary = "Update user personal details")
	public String updatePatientDetails(@ModelAttribute("user") @Validated User user, BindingResult result, HttpSession session,
			Model model) {
		User sessionUser = (User) session.getAttribute("loginuser");
		user.setNeuid(sessionUser.getNeuid());

		userValidator.validateUpdate(user, result);

		if (result.hasErrors()) {
			logger.warn("Validation errors found for user: {}", user.getEmailId());
			model.addAttribute("errors", result);
			return "managePatientPersonalDetails";
		}

		userService.updateUser(user);
		User updatedUser = userService.findUserByEmail(user.getEmailId());

		session.setAttribute("loginuser", updatedUser);
		model.addAttribute("user", updatedUser);
		model.addAttribute("successMessage", "Details updated successfully.");
		logger.info("Details updated successfully for user: {}", user.getEmailId());

		return "managePatientPersonalDetails";
	}
	
	@PostMapping("/doctor/managePersonalDetails")
    @Operation(summary = "Update user personal details")
	public String updateDoctorDetails(@ModelAttribute("user") @Validated User user, BindingResult result, HttpSession session,
			Model model) {
		User sessionUser = (User) session.getAttribute("loginuser");
		user.setNeuid(sessionUser.getNeuid());

		userValidator.validateUpdate(user, result);

		if (result.hasErrors()) {
			logger.warn("Validation errors found for user: {}", user.getEmailId());
			model.addAttribute("errors", result);
			return "manageDoctorPersonalDetails";
		}

		userService.updateUser(user);
		User updatedUser = userService.findUserByEmail(user.getEmailId());

		session.setAttribute("loginuser", updatedUser);
		model.addAttribute("user", updatedUser);
		model.addAttribute("successMessage", "Details updated successfully.");
		logger.info("Details updated successfully for user: {}", user.getEmailId());

		return "manageDoctorPersonalDetails";
	}

	@GetMapping("/admin/searchPatients")
    @Operation(summary = "Search for patients based on criteria")
	public String searchPatients(@RequestParam("searchPatientCriteria") String searchCriteria,
			@RequestParam("searchPatientQuery") String searchQuery, HttpSession session, Model model) {

		List<User> patients;
		if ("all".equalsIgnoreCase(searchCriteria)) {
			patients = userService.getAllPatients("patient");
		} else {
			patients = userService.getFilteredPatients(searchCriteria, searchQuery, "patient");
		}
		model.addAttribute("patients", patients);
		return "adminDashboard";
	}

	@GetMapping("/admin/searchDoctors")
    @Operation(summary = "Search for doctors based on criteria")
	public String searchDoctors(@RequestParam("searchDoctorCriteria") String searchCriteria,
			@RequestParam("searchDoctorQuery") String searchQuery, HttpSession session, Model model) {

		List<User> doctors;
		if ("all".equalsIgnoreCase(searchCriteria)) {
			doctors = userService.getAllDoctors("doctor");
		} else {
			doctors = userService.getFilteredDoctors(searchCriteria, searchQuery, "doctor");
		}

		model.addAttribute("doctors", doctors);
		
		return "adminDashboard";
	}
}
