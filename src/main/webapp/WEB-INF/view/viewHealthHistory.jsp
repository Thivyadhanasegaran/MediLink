<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="com.company.neuheathcaremanagement.pojo.HealthReport" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Health History - NEU Student Healthcare Management System</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/viewHealthHistoryStyles.css">
    
</head>
<body>
    <div class="header">
        <h1>
            <img src="${pageContext.request.contextPath}/images/logo2.png" alt="Icon">
            Welcome to NEU Student Healthcare Management System
        </h1>
    </div>
    <div class="container">
        <h2>Health History</h2>

        <c:choose>
            <c:when test="${not empty healthReports}">
                <div class="form-container">
                    <form action="${pageContext.request.contextPath}/patient/viewHealthHistory" method="get" id="pageSizeForm">
                        <label for="size">Items per page:</label>
                        <select name="size" onchange="document.getElementById('pageSizeForm').submit();">
                            <option value="5" ${size == 5 ? 'selected' : ''}>5</option>
                            <option value="10" ${size == 10 ? 'selected' : ''}>10</option>
                            <option value="15" ${size == 15 ? 'selected' : ''}>15</option>
                        </select>
                        <input type="hidden" name="page" value="${currentPage}" />
                    </form>
                </div>
                <table>
                    <thead>
                    <tr>
                            <th>Record ID</th>
                            <th>Appointment ID</th>
                            <th>Doctor Name</th>
                            <th>Reason for Visit</th>
                            <th>Diagnosis</th>
                            <th>Prescription</th>
                            <th>Appointment Status</th>
                            <th>Visited Date</th>
                        </tr> 
                        
                    </thead>
                    <tbody>
                        <c:forEach var="report" items="${healthReports}">
                            <tr>
                                <td>${report.recordId}</td>
                                <td>${report.appointment.appointmentId}</td>
                                <td>${report.appointment.doctor.name}</td>
                                <td>${report.appointment.reasonForVisit}</td>
                                <td>${report.diagnosis}</td>
                                <td>${report.prescription}</td>
                                <td>${report.appointment.status}</td>
                                <td>${report.appointment.appointmentDate}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div class="center-button">
                    <div class="back-to-home">
                        <a href="${pageContext.request.contextPath}/patient/patientHomePage">Back to Home</a>
                    </div>
                    <form action="${pageContext.request.contextPath}/patient/downloadAllHealthReport" method="get" style="margin: 0;">
                        <button type="submit">Download All Health Reports</button>
                    </form>
                    <div class="pagination">
                        <c:if test="${currentPage > 0}">
                            <a href="${pageContext.request.contextPath}/patient/viewHealthHistory?searchCriteria=${searchCriteria}&searchQuery=${searchQuery}&page=${currentPage - 1}&size=${size}">&laquo; Previous</a>
                        </c:if>
                        <c:forEach begin="0" end="${totalPages - 1}" var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <a href="${pageContext.request.contextPath}/patient/viewHealthHistory?searchCriteria=${searchCriteria}&searchQuery=${searchQuery}&page=${i}&size=${size}" class="active">${i + 1}</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/patient/viewHealthHistory?searchCriteria=${searchCriteria}&searchQuery=${searchQuery}&page=${i}&size=${size}">${i + 1}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${currentPage < totalPages - 1}">
                            <a href="${pageContext.request.contextPath}/patient/viewHealthHistory?searchCriteria=${searchCriteria}&searchQuery=${searchQuery}&page=${currentPage + 1}&size=${size}">Next &raquo;</a>
                        </c:if>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>Record ID</th>
                            <th>Appointment ID</th>
                            <th>Doctor Name</th>
                            <th>Reason for Visit</th>
                            <th>Diagnosis</th>
                            <th>Prescription</th>
                            <th>Appointment Status</th>
                            <th>Visited Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td colspan="8" style="text-align: center;">No health reports found.</td>
                        </tr>
                    </tbody>
                </table>
                <div class="back-to-home">
                    <a href="${pageContext.request.contextPath}/patient/patientHomePage">Back to Home</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
    