<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reschedule Appointment - NEU Student Healthcare Management System</title>
       <link rel="stylesheet" href="${pageContext.request.contextPath}/css/rescheduleAppointmentStyles.css">
   
</head>
<body>
     <div class="header">
        <h1>
            <img src="${pageContext.request.contextPath}/images/logo2.png" alt="Icon">
            Welcome to NEU Student Healthcare Management System
        </h1>
    </div>

    <div class="container">
        <h2>Reschedule Appointment</h2>

        <form:form action="${pageContext.request.contextPath}/doctor/rescheduleAppointment" method="post" modelAttribute="appointment">
            <c:if test="${not empty successMessage}">
                <p class="success">${successMessage}</p>
            </c:if>

            <form:hidden path="appointmentId" />

            <div class="form-group">
                <label for="appointmentDate">Appointment Date:</label>
                <form:input path="appointmentDate" type="date" value="${appointment.appointmentDate}" />
                <form:errors path="appointmentDate" cssClass="error" />
            </div>

            <div class="form-group">
                <label for="appointmentTime">Appointment Time:</label>
                <form:input path="appointmentTime" type="time" value="${appointment.appointmentTime}" />
                <form:errors path="appointmentTime" cssClass="error" />
            </div>

            <div class="form-group">
                <label for="reasonForRescheduling">Reason for Rescheduling:</label>
                <form:input path="reasonForRescheduling" value="${appointment.reasonForRescheduling}" />
                <form:errors path="reasonForRescheduling" cssClass="error" />
            </div>

            <div class="form-group">
                <input type="submit" value="Update Appointment">
            </div>
        </form:form>

        <div class="back-to-home">
            <a href="${pageContext.request.contextPath}/doctor/viewAppointment">Back to Appointments</a>
        </div>
    </div>
</body>
</html>
    