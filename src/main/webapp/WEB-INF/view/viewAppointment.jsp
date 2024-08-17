<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>View Appointments - NEU Student Healthcare Management System</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/viewAppointmentStyles.css">

</head>
<body>
    <div class="header">
        <h1>
            <img src="${pageContext.request.contextPath}/images/logo2.png" alt="Icon">
            Welcome to NEU Student Healthcare Management System
        </h1>
    </div>
    <h2>View Appointments</h2>
    <form action="${pageContext.request.contextPath}/doctor/searchAppointments" method="get">
        <div class="search-container">
            <div class="form-group">
                <label for="searchCriteria">Search by:</label>
                <select name="searchCriteria">
                    <option value="all">All</option>
                    <option value="patientId">Patient ID</option>
                    <option value="patientName">Patient Name</option>
                    <option value="date">Date</option>
                    <option value="status">Status</option>
   				 </select>
            </div>
            <div class="form-group">
                <input type="text" name="searchQuery" placeholder="Enter your search query" />
            </div>
            <div class="form-group">
                <button type="submit">Search</button>
            </div>
        </div>
    </form>
    <br>
    <table>
        <thead>
            <tr>
                <th>Appointment ID</th>
                <th>Patient ID</th>
                <th>Patient Name</th>
                <th>Appointment Date</th>
                <th>Appointment Time</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${not empty appointments}">
                    <c:forEach items="${appointments}" var="appointment">
                        <tr>
                            <td>${appointment.appointmentId}</td>
                            <td>${appointment.student.neuid}</td>
                            <td>${appointment.student.name}</td>
                            <td>${appointment.appointmentDate}</td>
                            <td>${appointment.appointmentTime}</td>
                            <td>${appointment.status}</td>                   
							<td class="centered actions-column">
						    <c:choose>
						       <c:when test="${appointment.status == 'Scheduled' || appointment.status == 'Rescheduled'}">
						    <div class="actions-column">
						        <a href="${pageContext.request.contextPath}/doctor/createHealthReport?appointmentId=${appointment.appointmentId}">Create Health Report</a>
						        <a href="${pageContext.request.contextPath}/doctor/rescheduleAppointment?appointmentId=${appointment.appointmentId}" class="reschedule">Reschedule Appointment</a>
						        <a href="${pageContext.request.contextPath}/doctor/cancelAppointment?appointmentId=${appointment.appointmentId}" class="cancel">Cancel Appointment</a>
						    </div>
						    </c:when>
						        <c:otherwise>
						           <div class="no-actions">-</div>
						        </c:otherwise>
						    </c:choose>
						 </td>
					   </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="7" style="text-align: center;">No appointments found.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
 
    <div class="back-to-home">
        <a href="${pageContext.request.contextPath}/doctor/doctorHomePage">Back to Home</a>
    </div>

   <script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
</body>
</html>
       