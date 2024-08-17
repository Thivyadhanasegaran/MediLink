<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Personal Details - NEU Student Healthcare Management System</title>
       <link rel="stylesheet" href="${pageContext.request.contextPath}/css/managePersonalDetailsStyles.css">
   
</head>
<body>
     <div class="header">
        <h1>
            <img src="${pageContext.request.contextPath}/images/logo2.png" alt="Icon">
            Welcome to NEU Student Healthcare Management System
        </h1>
    </div>
    <div class="main-container">
        <div class="details-container">
            <h2>Manage Personal Details</h2>
            <form:form modelAttribute="user" action="managePersonalDetails" method="post">
                <c:if test="${not empty successMessage}">
                    <p class="success">${successMessage}</p>
                </c:if>
                <form:input type="hidden" path="neuid" value="${user.neuid}" />
                <div class="form-group">
                    <label for="name">Name:</label>
                    <form:input path="name" value="${user.name}" />
                    <form:errors path="name" cssClass="error" />
                </div>
                <div class="form-group">
                    <label for="emailId">Email:</label>
                    <form:input path="emailId" value="${user.emailId}" readonly="true" cssClass="readonly-field" />
                    <form:errors path="emailId" cssClass="error" />
                </div>
                <div class="form-group">
                    <label for="dob">Date of Birth:</label>
                    <form:input path="dob" type="date" value="${user.dob}" />
                    <form:errors path="dob" cssClass="error" />
                </div>
                <div class="form-group">
                    <label for="contactNo">Contact Number:</label>
                    <form:input path="contactNo" value="${user.contactNo}" />
                    <form:errors path="contactNo" cssClass="error" />
                </div>
                <div class="form-group">
                    <label for="address">Address:</label>
                    <form:input path="address" value="${user.address}" />
                    <form:errors path="address" cssClass="error" />
                </div>
                <div class="form-group">
                    <label for="gender">Gender:</label>
                    <form:select path="gender" value="${user.gender}">
                        <option value="male" ${user.gender == 'male' ? 'selected' : ''}>Male</option>
                        <option value="female" ${user.gender == 'female' ? 'selected' : ''}>Female</option>
                    </form:select>
                    <form:errors path="gender" cssClass="error" />
                </div>
                <div class="form-group">
                    <button type="submit">Update Details</button>
                </div>
            </form:form>
           <div class="links">
          	   <a href="${pageContext.request.contextPath}/patient/patientHomePage">Back to Home</a>
       	   </div>
        </div>
    </div>
</body>
</html>
