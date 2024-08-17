package com.company.neuheathcaremanagement.controller;

import com.company.neuheathcaremanagement.pojo.User;
import com.company.neuheathcaremanagement.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
 class AdminController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private AdminService adminService;

	@GetMapping("admin/adminDashboard")
	public String showAdminDashboard(Model model) {
		model.addAttribute("patients", null);
		model.addAttribute("doctors", null);
		return "adminDashboard";
	}

	
	@GetMapping("admin/listPatients")
	public String listPatients(Model model) {
		List<User> patients = adminService.getUsersByRole("patient");
		model.addAttribute("patients", patients);
		model.addAttribute("doctors", null);
		logger.info("Patient list displayed. Number of patients: {}", patients.size());
		return "adminDashboard";
	}

	@GetMapping("admin/listDoctors")
	public String listDoctors(Model model) {
		List<User> doctors = adminService.getUsersByRole("doctor");
		model.addAttribute("doctors", doctors);
		model.addAttribute("patients", null);
		logger.info("Doctor list displayed. Number of doctors: {}", doctors.size());
		return "adminDashboard";
	}

	@PostMapping("admin/activateUser")
	public String activateUser(@RequestParam("neuid") int neuid, @RequestParam("role") String role) {
		adminService.activateUser(neuid);
		logger.info("User with NEU ID {} activated", neuid);
		return "redirect:/admin/list" + role;
	}

	@PostMapping("admin/deactivateUser")
	public String deactivateUser(@RequestParam("neuid") int neuid, @RequestParam("role") String role) {
		adminService.deactivateUser(neuid);
		logger.info("User with NEU ID {} deactivated", neuid);
		return "redirect:/admin/list" + role;
	}
}
