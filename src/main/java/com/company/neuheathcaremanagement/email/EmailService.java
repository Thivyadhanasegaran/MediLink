package com.company.neuheathcaremanagement.email;

import java.io.InputStream;

public interface EmailService {
    void sendAppointmentConfirmation(String to, String subject, String text);
    void sendEmailWithAttachment(String to, String subject, String text, InputStream attachmentStream, String attachmentName);
}