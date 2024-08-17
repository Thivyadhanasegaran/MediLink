<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Book Appointment - NEU Student Healthcare Management
	System</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bookAppointmentStyles.css">
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
		<h2>Book an Appointment</h2>

		<form:form action="${pageContext.request.contextPath}/patient/bookAppointment"
			method="post" modelAttribute="appointment">
			<c:if test="${not empty successMessage}">
				<p class="success">${successMessage}</p>
			</c:if>
			 <c:if test="${not empty error}">
                    <div class="error">
                        ${error}
                    </div>
                </c:if>

			<div class="form-group">
			    <label for="specialization">Select Specialization:</label>
			    <select id="specialization" name="specialization">
			        <option value="">Select Specialization</option>
			        <option value="Eye Care Specialist" ${specialization == 'Eye Care Specialist' ? 'selected' : ''}>Eye Care Specialist</option>
			        <option value="Dental Specialist" ${specialization == 'Dental Specialist' ? 'selected' : ''}>Dental Specialist</option>
			        <option value="General Medicine" ${specialization == 'General Medicine' ? 'selected' : ''}>General Medicine</option>
			        <option value="Skin Specialist" ${specialization == 'Skin Specialist' ? 'selected' : ''}>Skin Specialist</option>
			        <option value="Mental Health Specialist" ${specialization == 'Mental Health Specialist' ? 'selected' : ''}>Mental Health Specialist</option>
			    </select>
			</div>

			<div class="form-group">
				<label for="doctor">Select Healthcare Doctor:</label> <select
					id="doctor" name="doctorId" required>
					<option value="">Select Healthcare Doctor</option>
					<c:forEach var="doctor" items="${doctors}">
						<option value="${doctor.neuid}"
							${doctorId == doctor.neuid ? 'selected' : ''}>
							${doctor.name}</option>
					</c:forEach>
				</select>
			</div>

			<div class="form-group">
				<label for="date">Choose Date:</label> <input type="date"
					name="appointmentDate" value="${appointment.appointmentDate}">
				<form:errors path="appointmentDate" cssClass="error" />
			</div>

			<div class="form-group">
				<label for="time">Choose Time:</label> <input type="time"
					name="appointmentTime" value="${appointment.appointmentTime}">
				<form:errors path="appointmentTime" cssClass="error" />
			</div>

			<div class="form-group">
				<label for="reason">Reason for Visit:</label>
				<textarea name="reasonForVisit" rows="4">${appointment.reasonForVisit}</textarea>
				<form:errors path="reasonForVisit" cssClass="error" />
			</div>

			<div class="form-group">
				<button type="submit">Book Appointment</button>
			</div>
		</form:form>

		<div class="back-to-home">
			 <a href="patientHomePage">Back to Home</a>
		</div>
	</div>
	
	<script>
        document.addEventListener("DOMContentLoaded", function() {
            var specializationSelect = document.getElementById('specialization');
            var doctorSelect = document.getElementById('doctor');

            specializationSelect.addEventListener('change', function() {
                var specialization = this.value;
                var xmlHttp = new XMLHttpRequest();
                xmlHttp.open('GET', '${pageContext.request.contextPath}/getDoctorsBySpecialization?specialization=' + encodeURIComponent(specialization), true);
                xmlHttp.onreadystatechange = function() {
                    if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
                        var data = JSON.parse(xmlHttp.responseText);
                        var options = '<option value="">Select Healthcare Doctor</option>';
                        data.forEach(function(doctor) {
                            options += '<option value="' + doctor.neuid + '">' + doctor.name + '</option>';
                        });
                        doctorSelect.innerHTML = options;
                    }
                };
                xmlHttp.send();
            });
        });
    </script>
    
    <!-- <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
        $(document).ready(function() {
            $('#specialization').change(function() {
                var specialization = $(this).val();
                $.ajax({
                    url: '${pageContext.request.contextPath}/getDoctorsBySpecialization',
                    type: 'GET',
                    data: { specialization: specialization },
                    success: function(data) {
                        var options = '<option value="">Select Healthcare Doctor</option>';
                        $.each(data, function(index, doctor) {
                            options += '<option value="' + doctor.neuid + '">' + doctor.name + '</option>';
                        });
                        $('#doctor').html(options);
                    },
                    error: function(xhr, status, error) {
                        console.error("Error fetching doctors:", error);
                    }
                });
            });
        });
    </script>  -->
    
</body>
</html>
