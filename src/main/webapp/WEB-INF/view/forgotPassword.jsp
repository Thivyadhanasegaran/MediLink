<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Forgot Password - NEU Student Healthcare Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/forgotPasswordStyles.css">  
</head>
<body>
    <div class="header">
        <h1>
            <img src="${pageContext.request.contextPath}/images/logo2.png" alt="Icon">
            Welcome to NEU Student Healthcare Management System
        </h1>
    </div>
    <div class="main-container">
        <div class="forgot-password-container">
            <h2>Forgot Password</h2>
            <c:if test="${message != null}">
                <p class="message">${message}</p>
            </c:if>
              <form:form modelAttribute="user" action="forgotPassword" method="post">        
                <div class="form-group">
                    <label for="emailId">Email ID:</label>
                    <form:input path="emailId"/>
                    <form:errors path="emailId" cssClass="error"/>
                </div>
                <button type="submit">Send Reset Link</button>
            </form:form>
            <div class="back-to-login">              
                 <a href="${pageContext.request.contextPath}/login">Back to Login</a>
            </div>
        </div>
    </div>
</body>
</html>
  