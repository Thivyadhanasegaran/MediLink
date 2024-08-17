package com.company.neuheathcaremanagement.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.company.neuheathcaremanagement.email.EmailService;
import com.company.neuheathcaremanagement.pojo.Appointment;
import com.company.neuheathcaremanagement.pojo.User;
import com.company.neuheathcaremanagement.service.AppointmentService;
import com.company.neuheathcaremanagement.validator.AppointmentValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import jakarta.servlet.http.HttpSession;

@Controller
@Tag(name = "Appointment Management", description = "Endpoints for managing appointments")

public class AppointmentController {

	private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private AppointmentValidator appointmentValidator;
	
	@GetMapping("/getDoctorsBySpecialization")
	@Operation(summary = "Get doctors by specialization", description = "Fetches a list of doctors based on their specialization")
    @Parameter(name = "specialization", description = "The specialization to filter doctors by", required = true)
	public @ResponseBody List<User> getDoctorsBySpecialization(@RequestParam("specialization") String specialization) {
	    List<User> doctors = appointmentService.getDoctorsBySpecialization(specialization);
	    return doctors;
	}

	@GetMapping("/patient/bookAppointment")
    @Operation(summary = "Show book appointment page", description = "Displays the page for booking an appointment")
	public String showBookAppointmentPage(Model model) {
		List<User> doctors = appointmentService.getDoctors();
		model.addAttribute("doctors", doctors);
		return "bookAppointment";
	}
	
	
	@PostMapping("/patient/bookAppointment")
	@Operation(summary = "Book an appointment", description = "Books an appointment with a specified doctor")
    @Parameters({
        @Parameter(name = "doctorId", description = "The ID of the doctor", required = true),
        @Parameter(name = "specialization", description = "The specialization of the doctor", required = true)
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Appointment booked successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or appointment validation failed")
    })
	public String bookAppointment(@ModelAttribute("appointment") @Validated Appointment appointment,
	        BindingResult result, @RequestParam("doctorId") int doctorId,@RequestParam("specialization") String specialization, HttpSession session, Model model)
	        throws Exception {
	    User student = (User) session.getAttribute("loginuser");

	    appointmentValidator.validate(appointment, result);

	    if (result.hasErrors()) {
	        List<User> doctors = appointmentService.getDoctors();
	        model.addAttribute("appointment", appointment);
	        model.addAttribute("specialization", specialization);
	        model.addAttribute("doctorId", doctorId);
	        model.addAttribute("doctors", doctors);
	        model.addAttribute("errors", result);
	        return "bookAppointment";
	    }

	    try {
	        LocalDate appointmentDate = appointment.getAppointmentDate();
	        LocalTime appointmentTime = appointment.getAppointmentTime();
	        String reasonForVisit = appointment.getReasonForVisit();
	        appointmentService.bookAppointment(student, doctorId, appointmentDate, appointmentTime, reasonForVisit);
	        model.addAttribute("successMessage", "Appointment booked successfully.");
	    } catch (Exception e) {
	        model.addAttribute("error", e.getMessage());
	    }

	    List<User> doctors = appointmentService.getDoctors();
	    model.addAttribute("doctors", doctors);
	    model.addAttribute("specialization", specialization);
	    model.addAttribute("doctorId", doctorId);

	    return "bookAppointment";
	}


	@GetMapping("/doctor/viewAppointment")
    @Operation(summary = "View appointments", description = "Displays the list of appointments for the logged-in doctor")
	public String viewAppointments(HttpSession session, Model model) {
		User doctor = (User) session.getAttribute("loginuser");

		List<Appointment> appointments = appointmentService.getAppointmentsByDoctorId(doctor.getNeuid());
		model.addAttribute("appointments", appointments);
		logger.info("Doctor viewing appointments: doctorId={}, numberOfAppointments={}", doctor.getNeuid(),
				appointments.size());
		return "viewAppointment";
	}

	@GetMapping("/doctor/rescheduleAppointment")
	@Operation(summary = "Show reschedule appointment page", description = "Displays the page for rescheduling an appointment")
    @Parameter(name = "appointmentId", description = "The ID of the appointment to be rescheduled", required = true)
	public String showRescheduleAppointmentPage(@RequestParam("appointmentId") int appointmentId, Model model) {
		Appointment appointment = appointmentService.getAppointmentById(appointmentId);
		model.addAttribute("appointment", appointment);
		return "rescheduleAppointment";
	}	

	 @PostMapping("/doctor/rescheduleAppointment")
	 @Operation(summary = "Reschedule an appointment", description = "Reschedules an existing appointment")
	    @ApiResponses({
	        @ApiResponse(responseCode = "200", description = "Appointment rescheduled successfully"),
	        @ApiResponse(responseCode = "400", description = "Invalid input or appointment validation failed")
	    })
	    public String rescheduleAppointment(@ModelAttribute("appointment") @Validated Appointment appointment,
	                                        BindingResult result, Model model) {
	        appointmentValidator.validateReschedule(appointment, result);

	        if (result.hasErrors()) {
	            return "rescheduleAppointment";
	        }

	        Appointment existingAppointment = appointmentService.getAppointmentById(appointment.getAppointmentId());

	        existingAppointment.setAppointmentDate(appointment.getAppointmentDate());
	        existingAppointment.setAppointmentTime(appointment.getAppointmentTime());
	        existingAppointment.setReasonForRescheduling(appointment.getReasonForRescheduling());
	        existingAppointment.setStatus("Rescheduled");

	        appointmentService.updateAppointment(existingAppointment);

	        User student = existingAppointment.getStudent();
	        String emailSubject = "Appointment Rescheduled";
	        String emailText = String.format(
	        	    "<html>"
	        	    + "<body>"
	        	    + "<p>Dear %s,</p>"
	        	    + "<p>Your appointment with <strong>Dr. %s</strong> has been rescheduled to <strong>%s</strong> at <strong>%s</strong>.</p>"
	        	    + "<p><strong>Reason:</strong> %s</p><br>"
	        	    + "<p>Best regards,<br> NEU Student Healthcare Management System</p>"
	        	    + "</body>"
	        	    + "</html>",
	        	    student.getName(),
	        	    existingAppointment.getDoctor().getName(),
	        	    existingAppointment.getAppointmentDate().toString(),
	        	    existingAppointment.getAppointmentTime().toString(),
	        	    existingAppointment.getReasonForRescheduling()
	        	);
	        
	        emailService.sendAppointmentConfirmation(student.getEmailId(), emailSubject, emailText);

	        model.addAttribute("successMessage", "Appointment rescheduled successfully.");

	        return "rescheduleAppointment";
	    }

	@GetMapping("/doctor/cancelAppointment")
	@Operation(summary = "Show cancel appointment page", description = "Displays the page for canceling an appointment")
    @Parameter(name = "appointmentId", description = "The ID of the appointment to be canceled", required = true)
	public String confirmCancelAppointment(@RequestParam("appointmentId") int appointmentId, Model model) {
		Appointment appointment = appointmentService.getAppointmentById(appointmentId);
		model.addAttribute("appointment", appointment);
		return "cancelAppointment";
	}
	
	@PostMapping("/doctor/cancelAppointment")
	@Operation(summary = "Cancel an appointment", description = "Cancels an existing appointment")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Appointment canceled successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or appointment validation failed")
    })
    public String cancelAppointment(@ModelAttribute("appointment") Appointment appointment, BindingResult result, Model model) {
        appointmentValidator.validateCancellationReason(appointment.getReasonForCancellation(), result);

        if (result.hasErrors()) {
            return "cancelAppointment";
        }

        Appointment existingAppointment = appointmentService.getAppointmentById(appointment.getAppointmentId());
        if (existingAppointment != null) {
            existingAppointment.setStatus("Cancelled");
            existingAppointment.setReasonForCancellation(appointment.getReasonForCancellation());

            appointmentService.updateAppointment(existingAppointment);

            User student = existingAppointment.getStudent();
            String emailSubject = "Appointment Cancelled";
            String emailText = String.format(
            	    "<html>"
            	    + "<body>"
            	    + "<p>Dear %s,</p>"
            	    + "<p>Your appointment with <strong>Dr. %s</strong> scheduled for <strong>%s</strong> at <strong>%s</strong> has been cancelled.</p>"
            	    + "<p><strong>Reason:</strong> %s</p><br>"
            	    + "<p>Best regards,<br> NEU Student Healthcare Management System</p>"
            	    + "</body>"
            	    + "</html>",
            	    student.getName(),
            	    existingAppointment.getDoctor().getName(),
            	    existingAppointment.getAppointmentDate().toString(),
            	    existingAppointment.getAppointmentTime().toString(),
            	    appointment.getReasonForCancellation()
            	);
            emailService.sendAppointmentConfirmation(student.getEmailId(), emailSubject, emailText);
        }
      
		model.addAttribute("successMessage", "Appointment cancelled successfully.");

        return "cancelAppointment";
    }

	@GetMapping("/patient/viewAppointmentStatus")
	public String viewAppointmentStatus(HttpSession session, Model model) {
		User patient = (User) session.getAttribute("loginuser");
		if (patient != null) {
			List<Appointment> appointments = appointmentService.getAppointmentsByStudent(patient);
			model.addAttribute("appointments", appointments);
			logger.info("Patient viewing appointment status: patientId={}, numberOfAppointments={}", patient.getNeuid(),
					appointments.size());
		}
		return "viewAppointmentStatus";
	}

	@GetMapping("/patient/searchAppointmentsStatus")
	public String searchAppointmentsStatus(@RequestParam("searchCriteria") String searchCriteria,
			@RequestParam("searchQuery") String searchQuery, HttpSession session, Model model) {
		User patient = (User) session.getAttribute("loginuser");
		List<Appointment> appointments = appointmentService.getAppointmentsByStudent(patient);
		List<Appointment> filteredAppointments = filterAppointments(appointments, searchCriteria, searchQuery);

		model.addAttribute("appointments", filteredAppointments);		    
		return "viewAppointmentStatus";
	}
	
	@GetMapping("/doctor/searchAppointments")
	public String searchAppointments(@RequestParam("searchCriteria") String searchCriteria,
			@RequestParam("searchQuery") String searchQuery, HttpSession session, Model model) {
		User user = (User) session.getAttribute("loginuser");

		List<Appointment> appointments = appointmentService.getAppointmentsByDoctorId(user.getNeuid());
		List<Appointment> filteredAppointments = filterAppointments(appointments, searchCriteria, searchQuery);
		
		model.addAttribute("appointments", filteredAppointments);			    
		return "viewAppointment";
	}

	private List<Appointment> filterAppointments(List<Appointment> appointments, String criteria, String query) {
		if ("all".equals(criteria)) {
			return appointments;
		}
		if (query == null || query.isEmpty()) {
			return appointments;
		}

		return appointments.stream().filter(appointment -> {
			switch (criteria) {
			case "appointmentId":
				return String.valueOf(appointment.getAppointmentId()).contains(query);
			case "patientId":
				return String.valueOf(appointment.getStudent().getNeuid()).contains(query);
			case "patientName":
				return appointment.getStudent().getName().toLowerCase().contains(query.toLowerCase());
			case "date":
				return appointment.getAppointmentDate().toString().contains(query);
			case "status":
				return appointment.getStatus().toLowerCase().contains(query.toLowerCase());
			case "doctorName":
				return appointment.getDoctor().getName().toLowerCase().contains(query.toLowerCase());

			default:
				return false;
			}
		}).collect(Collectors.toList());
	}

}
