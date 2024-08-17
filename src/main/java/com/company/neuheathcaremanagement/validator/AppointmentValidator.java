package com.company.neuheathcaremanagement.validator;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.company.neuheathcaremanagement.pojo.Appointment;

@Component
public class AppointmentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Appointment.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Appointment appointment = (Appointment) target;

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        LocalDate appointmentDate = appointment.getAppointmentDate();
        LocalTime appointmentTime = appointment.getAppointmentTime();
        String reasonForVisit = appointment.getReasonForVisit();

        if (appointmentDate == null) {
            errors.rejectValue("appointmentDate", "error.appointmentDate", "Appointment date is required.");
        } else if (appointmentDate.isBefore(currentDate)) {
            errors.rejectValue("appointmentDate", "error.appointmentDate", "Appointment date must be in the future.");
        }

        if (appointmentTime == null) {
            errors.rejectValue("appointmentTime", "error.appointmentTime", "Appointment time is required.");
        } else if (appointmentDate != null && 
                   appointmentDate.isEqual(currentDate) &&
                   appointmentTime.isBefore(currentTime)) {
            errors.rejectValue("appointmentTime", "error.appointmentTime", "Appointment time must be in the future.");
        }

        if (reasonForVisit == null || reasonForVisit.isEmpty()) {
            errors.rejectValue("reasonForVisit", "error.reasonForVisit", "Reason for visit is required.");
        }    
    }
    
    public void validateCancellationReason(String reasonForCancellation, Errors errors) {
        if (reasonForCancellation == null || reasonForCancellation.isEmpty()) {
            errors.rejectValue("reasonForCancellation", "error.reasonForCancellation", "Reason for cancellation is required.");
        }
    }
    
    public void validateReschedule(Appointment appointment, Errors errors) {
        LocalDate currentDate = LocalDate.now();
        LocalDate appointmentDate = appointment.getAppointmentDate();
        LocalTime appointmentTime = appointment.getAppointmentTime();
        String reasonForRescheduling = appointment.getReasonForRescheduling();

        if (appointmentDate == null) {
            errors.rejectValue("appointmentDate", "error.appointmentDate", "Appointment date is required.");
        } else if (appointmentDate.isBefore(currentDate)) {
            errors.rejectValue("appointmentDate", "error.appointmentDate", "Appointment date must be in the future.");
        }

        if (appointmentTime == null) {
            errors.rejectValue("appointmentTime", "error.appointmentTime", "Appointment time is required.");
        }

        if (reasonForRescheduling == null || reasonForRescheduling.isEmpty()) {
            errors.rejectValue("reasonForRescheduling", "error.reasonForRescheduling", "Reason for rescheduling is required.");
        }
    }
}
