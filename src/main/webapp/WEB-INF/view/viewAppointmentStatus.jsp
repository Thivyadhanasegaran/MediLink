<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View Appointment Status - NEU Student Healthcare Management System</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/viewAppointmentStatusStyles.css">
    
</head>
<body>
     <div class="header">
        <h1>
            <img src="${pageContext.request.contextPath}/images/logo2.png" alt="Icon">
            Welcome to NEU Student Healthcare Management System
        </h1>
    </div>
    <div class="container">
        <h2>View Appointment Status</h2>

        <form action="${pageContext.request.contextPath}/patient/searchAppointmentsStatus" method="get">
        <label for="searchCriteria">Search By:</label>
         <select name="searchCriteria">
            <option value="all">All</option>
            <option value="appointmentId">Appointment ID</option>
            <option value="doctorName">Doctor Name</option>
            <option value="date">Date</option>
            <option value="status">Status</option>
         </select>
       <input type="text" name="searchQuery" placeholder="Enter search query" /> 
       
        <input type="submit" value="Search" />
    </form>

    <table border="1">
        <thead>
            <tr>
                <th>Appointment ID</th>
                <th>Patient ID</th>
                <th>Patient Name</th>
                <th>Doctor Name</th>
                <th>Appointment Date</th>
                <th>Appointment Time</th>
                <th>Current Status</th>
                <th>Reason for Rescheduling</th>
                <th>Reason for Cancellation</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${not empty appointments}">
                    <c:forEach items="${appointments}" var="appointment">
                        <tr>
                            <td class="centered">${appointment.appointmentId}</td>
                            <td class="centered">${appointment.student.neuid}</td>
                            <td class="centered">${appointment.student.name}</td>
                            <td class="centered">${appointment.doctor.name}</td>
                            <td class="centered">${appointment.appointmentDate}</td>
                            <td class="centered">${appointment.appointmentTime}</td>
                            <td class="centered">${appointment.status}</td>
                            <td class="centered">
                                <c:choose>
                                    <c:when test="${not empty appointment.reasonForRescheduling}">
                                        ${appointment.reasonForRescheduling}
                                    </c:when>
                                    <c:otherwise>
                                        -
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="centered">
                                <c:choose>
                                    <c:when test="${not empty appointment.reasonForCancellation}">
                                        ${appointment.reasonForCancellation}
                                    </c:when>
                                    <c:otherwise>
                                        -
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="9" class="centered">No appointments found.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>

        <div class="back-to-home">
            <a href="${pageContext.request.contextPath}/patient/patientHomePage">Back to Home</a>
        </div>
    </div>
</body>
</html>
   