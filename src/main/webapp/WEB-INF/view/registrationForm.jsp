<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register - NEU Student Healthcare Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/registrationFormStyles.css">
</head>
<body>
   <div class="header">
        <h1>
            <img src="${pageContext.request.contextPath}/images/logo2.png" alt="Icon">
            Welcome to NEU Student Healthcare Management System
        </h1>
    </div>
    <div class="main-container">
        <div class="register-container">
            <h2>Registration Form</h2>
            <form:form modelAttribute="user" action="registerForm" method="post">
                <c:if test="${not empty successMessage}">
                    <p class="success">${successMessage}</p>
                </c:if>
                <c:if test="${not empty errors}">
                    <div class="error">
                        ${errors}
                    </div>
                </c:if>
                <div class="form-group">
                    <label for="name">Name:</label>
                    <form:input path="name"/>
                    <form:errors path="name" cssClass="error"/>
                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <form:password path="password"/>
                    <form:errors path="password" cssClass="error"/>
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password:</label>
                    <form:password path="confirmPassword" />
                    <form:errors path="confirmPassword" cssClass="error"/>
                </div>
                <div class="form-group">
                    <label for="emailId">Email:</label>
                    <form:input path="emailId" />
                    <form:errors path="emailId" cssClass="error"/>
                </div>
                <div class="form-group">
                    <label for="contactNo">Contact Number:</label>
                    <form:input path="contactNo" />
                    <form:errors path="contactNo" cssClass="error"/>
                </div>
                <div class="form-group">
                    <label for="address">Address:</label>
                    <form:input path="address" />
                    <form:errors path="address" cssClass="error"/>
                </div>
                <div class="form-group">
                    <label for="dob">Date of Birth:</label>
                    <form:input path="dob" type="date" value="${user.dob}" />
                    <form:errors path="dob" cssClass="error"/>
                </div>
                <div class="form-group">
                    <label for="gender">Gender:</label>
                    <form:select path="gender" >
                        <option value="male">Male</option>
                        <option value="female">Female</option>
                    </form:select>
                    <form:errors path="gender" cssClass="error"/>
                </div>
                <div class="form-group">
                    <label for="userRole">Role:</label>
                    <form:select path="userRole" id="userRole" onchange="toggleSpecializationField()" >
                        <option value="patient">Patient</option>
                        <option value="doctor">Doctor</option>
                         <option value="admin">Admin</option>
                    </form:select>
                    <form:errors path="userRole" cssClass="error"/>
                </div>
				<div id="specializationField" class="form-group" style="display: none;">
				    <label for="specialization">Specialization:</label>
				    <form:select path="specialization">
				        <form:option value="" label="Select Specialization" />
				        <form:option value="Eye Care Specialist" label="Eye Care Specialist" />
				        <form:option value="Dental Specialist" label="Dental Specialist" />
				        <form:option value="General Medicine" label="General Medicine" />
				        <form:option value="Skin Specialist" label="Skin Specialist" />
				        <form:option value="Mental Health Specialist" label="Mental Health Specialist" />
				    </form:select>
				    <form:errors path="specialization" cssClass="error"/>
				</div>
                <div class="form-group">
                    <button type="submit">Register Now</button>
                </div>
                <div class="links">
                    <a href="login">Already have an account? Login here</a>
                </div>
            </form:form>
        </div>
    </div>
     <script>
        function toggleSpecializationField() {
            var userRole = document.getElementById("userRole").value;
            var specializationField = document.getElementById("specializationField");
            if (userRole === "doctor") {
                specializationField.style.display = "block";
            } else {
                specializationField.style.display = "none";
            }
        }
    </script>
</body>
</html>
