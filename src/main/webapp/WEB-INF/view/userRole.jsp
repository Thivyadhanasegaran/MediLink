<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User Role Selection - NEU Student Healthcare Management
	System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/userRoleStyles.css">

</head>
<body>
	<div class="header">
		<h1>
			<img src="${pageContext.request.contextPath}/images/logo2.png"
				alt="Icon"> Welcome to NEU Student Healthcare Management
			System
		</h1>
	</div>
	<div class="main-container">
		<div class="user-role-container">
			<h2>Select User Role</h2>
			<c:if test="${message != null}">
				<p class="message">${message}</p>
			</c:if>
			<form:form modelAttribute="user"
				action="${pageContext.request.contextPath}/oauth2userlogin"
				method="post">
				<div class="form-group">
					<label for="userRole">User Role:</label>
					<form:select path="userRole" id="userRole"
						onchange="toggleSpecializationField()">
						<form:option value="" label="Select Role" />
						<form:option value="doctor" label="Doctor" />
						<form:option value="patient" label="Patient" />
					</form:select>
					<form:errors path="userRole" cssClass="error" />
				</div>

				<div id="specializationField" class="form-group"
					style="display: none;">
					<label for="specialization">Specialization:</label>
					<form:select path="specialization">
						<form:option value="" label="Select Specialization" />
						<form:option value="Eye Care Specialist"
							label="Eye Care Specialist" />
						<form:option value="Dental Specialist" label="Dental Specialist" />
						<form:option value="General Medicine" label="General Medicine" />
						<form:option value="Skin Specialist" label="Skin Specialist" />
						<form:option value="Mental Health Specialist"
							label="Mental Health Specialist" />
					</form:select>
					<form:errors path="specialization" cssClass="error" />
				</div>
				<button type="submit">Save Role</button>
			</form:form>
			<div class="back-to-login">
				<a href="${pageContext.request.contextPath}/login">Back to Login</a>
			</div>
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
