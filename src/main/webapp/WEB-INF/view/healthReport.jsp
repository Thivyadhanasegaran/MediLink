<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Create Health Report - NEU Student Healthcare Management
	System</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/healthReportStyles.css">

</head>
<body>
	<div class="header">
		<h1>
			<img src="${pageContext.request.contextPath}/images/logo2.png"
				alt="Icon"> Welcome to NEU Student Healthcare Management
			System
		</h1>
	</div>
	<div class="container">
		<h2>Create Health Report</h2>
		<c:if test="${not empty successMessage}">
			<p class="success">${successMessage}</p>
		</c:if>
		<c:choose>
			<c:when test="${not empty appointment}">
				<form:form
					action="${pageContext.request.contextPath}/doctor/saveHealthReport"
					method="post" modelAttribute="healthReport">
					<form:hidden path="appointment.appointmentId"
						value="${appointment.appointmentId}" />

					<div class="form-group">
						<p>
							<strong>Patient ID:</strong> ${appointment.student.neuid}
						</p>
					</div>
					<div class="form-group">
						<p>
							<strong>Patient Name:</strong> ${appointment.student.name}
						</p>
					</div>
					<div class="form-group">
						<p>
							<strong>Reason for Visit:</strong> ${appointment.reasonForVisit}
						</p>
					</div>
					<div class="form-group">
						<p>
							<strong>Appointment Date:</strong> ${appointment.appointmentDate}
						</p>
					</div>
					<div class="form-group">
						<p>
							<strong>Appointment Time:</strong> ${appointment.appointmentTime}
						</p>
					</div>

					<div class="form-group">
						<form:label path="diagnosis">Diagnosis:</form:label>
						<br>
						<form:textarea path="diagnosis" rows="4" cols="50" />
						<br>
						<form:errors path="diagnosis" cssClass="error" />
					</div>

					<div class="form-group">
						<form:label path="prescription">Prescription:</form:label>
						<br>
						<form:textarea path="prescription" rows="4" cols="50" />
						<br>
						<form:errors path="prescription" cssClass="error" />
					</div>

					<div class="form-group">
						<form:label path="createdDate">Creation Date:</form:label>
						<br>
						<form:input path="createdDate" type="date"
							value="${healthReport.createdDate}" />
						<br>
						<form:errors path="createdDate" cssClass="error" />
					</div>

					<div class="form-group">
						<input type="submit" value="Save">
					</div>
				</form:form>
			</c:when>
			<c:otherwise>
				<p>Appointment not found.</p>
			</c:otherwise>
		</c:choose>
		<div class="back-to-home">
			<a href="${pageContext.request.contextPath}/doctor/viewAppointment">Back
				to Appointments</a>
		</div>
	</div>
</body>
</html>
