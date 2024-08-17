package com.company.neuheathcaremanagement.validator;

import java.time.LocalDate;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.company.neuheathcaremanagement.pojo.HealthReport;

@Component
public class HealthReportValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return HealthReport.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HealthReport healthReport = (HealthReport) target;

        if (healthReport.getDiagnosis() == null || healthReport.getDiagnosis().isEmpty()) {
            errors.rejectValue("diagnosis", "error.diagnosis", "Diagnosis is required.");
        }

        if (healthReport.getPrescription() == null || healthReport.getPrescription().isEmpty()) {
            errors.rejectValue("prescription", "error.prescription", "Prescription is required.");
        }

        if (healthReport.getCreatedDate() == null) {
            errors.rejectValue("createdDate", "error.createdDate", "Creation date is required.");
        } else {
            if (healthReport.getCreatedDate().isBefore(LocalDate.now())) {
                errors.rejectValue("createdDate", "error.createdDatePast", "Creation date cannot be in the past. The appointment have been missed and should be rescheduled or canceled.");
            } else if (healthReport.getAppointment() != null && !healthReport.getCreatedDate().isEqual(healthReport.getAppointment().getAppointmentDate())) {
                errors.rejectValue("createdDate", "error.createdDateMismatch", "Creation date must be the same as the appointment date.");
            }
        }
    }
}
