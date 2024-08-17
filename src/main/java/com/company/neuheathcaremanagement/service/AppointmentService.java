package com.company.neuheathcaremanagement.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.company.neuheathcaremanagement.dao.AppointmentDAO;
import com.company.neuheathcaremanagement.email.EmailService;
import com.company.neuheathcaremanagement.pojo.Appointment;
import com.company.neuheathcaremanagement.pojo.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    @Autowired
    private AppointmentDAO appointmentDAO;

    @Autowired
	private UserService userService;

    @Autowired
    private EmailService emailService;

    public List<User> getDoctorsBySpecialization(String specialization) {
        logger.info("Fetching doctors with specialization: {}", specialization);
        return userService.findDoctorsBySpecialization(specialization);
    }

    public List<User> getDoctors() {
        logger.info("Fetching all doctors");
        return userService.findDoctors();
    }

    public void bookAppointment(User student, int doctorId, LocalDate appointmentDate, LocalTime appointmentTime, String reasonForVisit) throws Exception {
        logger.info("Booking appointment for student: {} with doctorId: {}, on date: {} at time: {} for reason: {}", student.getName(), doctorId, appointmentDate, appointmentTime, reasonForVisit);
        User doctor = userService.findById(doctorId);
        if (doctor == null || !"doctor".equals(doctor.getUserRole())) {
            logger.error("Invalid doctor selected with id: {}", doctorId);
            throw new Exception("Invalid doctor selected.");
        }

        Appointment existingAppointment = appointmentDAO.findAppointmentByDoctorAndDateTime(doctorId, appointmentDate, appointmentTime);
        if (existingAppointment != null) {
            logger.error("Appointment already booked for doctorId: {} on date: {} at time: {}", doctorId, appointmentDate, appointmentTime);
            throw new Exception("Appointment already booked for the selected date and time.");
        }

        Appointment appointment = new Appointment();
        appointment.setStudent(student);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setReasonForVisit(reasonForVisit);
        appointment.setStatus("Scheduled");

        appointmentDAO.save(appointment);
        logger.info("Appointment successfully booked with doctor: {} for student: {}", doctor.getName(), student.getName());

        String emailSubject = "Appointment Confirmation";
        String emailText = String.format(
            "<html>"
            + "<body>"
            + "<p>Dear %s,</p>"
            + "<p>Your appointment with <strong>Dr. %s</strong> is scheduled for <strong>%s</strong> at <strong>%s</strong>.</p>"
            + "<p><strong>Reason:</strong> %s</p><br>"
            + "<p>Best regards,<br> NEU Student Healthcare Management System</p>"
            + "</body>"
            + "</html>",
            student.getName(),
            doctor.getName(),
            appointmentDate.toString(),
            appointmentTime.toString(),
            reasonForVisit
        );
        emailService.sendAppointmentConfirmation(student.getEmailId(), emailSubject, emailText);
        logger.info("Appointment confirmation email sent to: {}", student.getEmailId());
    }

    public List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        logger.info("Fetching appointments for doctorId: {}", doctorId);
        return appointmentDAO.findAppointmentsByDoctorId(doctorId);
    }

    public Appointment getAppointmentById(int appointmentId) {
        logger.info("Fetching appointment by id: {}", appointmentId);
        return appointmentDAO.findById(appointmentId);
    }

    public void updateAppointment(Appointment appointment) {
        appointmentDAO.update(appointment);
        logger.info("Appointment updated successfully");
    }

    public List<Appointment> getAppointmentsByStudent(User student) {
        logger.info("Fetching appointments for student: {}", student.getName());
        return appointmentDAO.findByStudent(student);
    }

    public User getDoctorById(int doctorId) {
        logger.info("Fetching doctor by id: {}", doctorId);
        return userService.findById(doctorId);
    }
}

