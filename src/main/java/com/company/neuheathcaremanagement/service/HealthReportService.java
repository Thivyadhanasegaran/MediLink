package com.company.neuheathcaremanagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.company.neuheathcaremanagement.dao.HealthReportDAO;
import com.company.neuheathcaremanagement.pojo.Appointment;
import com.company.neuheathcaremanagement.pojo.HealthReport;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
@Transactional
public class HealthReportService {

    @Autowired
    private HealthReportDAO healthReportDAO;

    @Autowired
	private AppointmentService appointmentService;

    public Appointment getAppointmentById(int appointmentId) {
        return appointmentService.getAppointmentById(appointmentId);
    }

    public void saveHealthReport(HealthReport healthReport) {
        healthReportDAO.save(healthReport);
    }

    public List<HealthReport> getHealthReportsByUserId(int userId) {
        return healthReportDAO.findByUserId(userId);
    }
    
    public void updateAppointment(Appointment appointment) {
    	appointmentService.updateAppointment(appointment);
    }
      
    public Page<HealthReport> getHealthReportsByUserId(int userId, Pageable pageable) {
        return healthReportDAO.findByUserIdPage(userId, pageable);
    }
}
