<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cancel Appointment - NEU Student Healthcare Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cancelAppointmentStyles.css">
</head>
<body>
    <div class="header">
        <h1>
            <img src="${pageContext.request.contextPath}/images/logo2.png" alt="Icon">
            Welcome to NEU Student Healthcare Management System
        </h1>
    </div>

    <div class="container">
        <h2>Cancel Appointment</h2>
        
        <form:form action="${pageContext.request.contextPath}/doctor/cancelAppointment" method="post" modelAttribute="appointment">
            <c:if test="${not empty successMessage}">
                <p class="success">${successMessage}</p>
            </c:if>
            
            <form:hidden path="appointmentId" />

            <div class="form-group">
                <label for="reasonForCancellation">Reason for Cancellation:</label>
                <form:input path="reasonForCancellation"/>
                <form:errors path="reasonForCancellation" cssClass="error" />
            </div>

            <div class="confirmation">
                <p>Are you sure you want to cancel this appointment?</p>
                <input type="submit" value="Yes, Cancel Appointment">
            </div>

            <div class="back-to-home">
                <a href="${pageContext.request.contextPath}/doctor/viewAppointment">No, Go Back</a>
            </div>
        </form:form>
    </div>
</body>
</html>
  