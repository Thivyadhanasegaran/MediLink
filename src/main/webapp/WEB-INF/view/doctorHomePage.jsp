<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Doctor Home - NEU Student Healthcare Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/doctorHomePageStyles.css">
</head>
<body>
    <div class="header">
        <h1>
            <img src="${pageContext.request.contextPath}/images/logo2.png" alt="Icon">
            Welcome to NEU Student Healthcare Management System
        </h1>
    </div>
    <div class="welcome-message">
        <h2>Welcome, Dr. ${sessionScope.userName}!</h2>
    </div>
    <div class="container">
        <div class="left-side">
            <a class="button" href="managePersonalDetails">Manage Personal Details</a>
            <a class="button" href="viewAppointment">View Appointments</a>
            <a class="button" href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
        <div class="right-side">
            <img src="${pageContext.request.contextPath}/images/doctorImage.png" alt="Doctor Image">
        </div>
    </div>
</body>
</html>
   