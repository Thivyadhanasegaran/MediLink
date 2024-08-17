<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reset Password - NEU Student Healthcare Management System</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/resetPasswordStyles.css">
    
</head>
<body>
    <div class="header">
        <h1>
            <img src="${pageContext.request.contextPath}/images/logo2.png" alt="Icon">
            Welcome to NEU Student Healthcare Management System
        </h1>
    </div>
    <div class="reset-password-container">
        <h2>Reset Password</h2>
        <c:if test="${message != null}">
            <p class="message">${message}</p>
        </c:if>
        <form:form modelAttribute="user" action="resetPassword" method="post">
            <input type="hidden" name="token" value="${token}" />
        
            <div class="form-group">
                <label for="password">New Password:</label>
                <form:password path="password" />
                <form:errors path="password" cssClass="error"/>
            </div>
            <div class="form-group">
                <label for="confirmPassword">Confirm Password:</label>
                <form:password path="confirmPassword" />
                <form:errors path="confirmPassword" cssClass="error"/>
            </div>
            <button type="submit">Reset Password</button>
        </form:form>
        <div class="back-to-login">
           
            <a href="${pageContext.request.contextPath}/login">Back to Login</a>
            
        </div>
    </div>
</body>
</html>
  