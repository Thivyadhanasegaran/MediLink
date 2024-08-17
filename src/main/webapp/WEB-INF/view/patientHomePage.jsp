<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Patient Home - NEU Student Healthcare Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/patientHomePageStyles.css">
</head>
<body>
    <div class="header">
        <h1>
            <img src="${pageContext.request.contextPath}/images/logo2.png" alt="Icon">
            Welcome to NEU Student Healthcare Management System
        </h1>
    </div>
    <div class="container">
        <div class="left-side">
            <a class="button" href="managePersonalDetails">Manage Personal Details</a>
            <a class="button" href="bookAppointment">Book Appointments</a>
            <a class="button" href="viewAppointmentStatus">View Appointment Status</a>
            <a class="button" href="viewHealthHistory">View Health History</a>
            <a class="button" href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
        <div class="right-side">
            <img src="${pageContext.request.contextPath}/images/support.jpg" alt="Patient Image">
        </div>
    </div>
</body>
</html>

  
 
        
        
        
       