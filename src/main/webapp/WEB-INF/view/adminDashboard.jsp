<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - NEU Student Healthcare Management System</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminStyles.css">
</head>
<body>
     <div class="header">
        <h1>
            <img src="${pageContext.request.contextPath}/images/logo2.png" alt="Icon">
            Welcome to NEU Student Healthcare Management System
        </h1>
    </div>
    
    <div class="container main-container">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="text-center flex-grow-1" style="margin-left: 70px;">Admin Dashboard</h2>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-primary ml-auto">Logout</a> <!-- Changed to blue color -->
        </div>
        <div class="text-center mb-4">
            <form action="${pageContext.request.contextPath}/admin/listPatients" method="get" class="d-inline">
                <input type="submit" value="List All Patients" class="btn btn-primary"/>
            </form>
            <form action="${pageContext.request.contextPath}/admin/listDoctors" method="get" class="d-inline">
                <input type="submit" value="List All Doctors" class="btn btn-primary"/>
            </form>
        </div>

        <c:if test="${not empty patients}">
        <div class="mb-4">
            <h3>Search Patients</h3>
            <form action="${pageContext.request.contextPath}/admin/searchPatients" method="get" class="form-inline">
                <label for="searchPatientCriteria" class="mr-2">Search Patients By:</label>
                <select name="searchPatientCriteria" class="form-control mr-2">
                    <option value="all">All</option>
                    <option value="neuid">Patient ID</option>
                    <option value="name">Name</option>
                    <option value="emailId">Email</option>
                    <option value="active">Account Status</option>
                </select>
                <input type="text" name="searchPatientQuery" placeholder="Enter search query" class="form-control mr-2"/>
                <input type="submit" value="Search" class="btn btn-primary"/>
            </form>
        </div>

            <h3>List of Patients</h3>
            <table class="table table-bordered">
                <thead class="thead-light text-center">
                    <tr>
				        <th class="text-nowrap">Patient ID</th>
				        <th class="text-nowrap">Name</th>
				        <th class="text-nowrap">Email</th>
				        <th class="text-nowrap">Address</th>
				        <th class="text-nowrap">Contact No</th>
				        <th class="text-nowrap">Account Status</th>
				        <th class="text-nowrap">Status Update</th>
				    </tr>
                </thead>
                <tbody>
                    <c:forEach var="patient" items="${patients}">
                        <tr class="text-center">
                            <td>${patient.neuid}</td>
                            <td>${patient.name}</td>
                            <td>${patient.emailId}</td>
                            <td>${patient.address}</td>
                            <td>${patient.contactNo}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${patient.active}">
                                        Active
                                    </c:when>
                                    <c:otherwise>
                                        Inactive
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <form action="${pageContext.request.contextPath}/admin/activateUser" method="post" class="d-inline">
                                    <input type="hidden" name="neuid" value="${patient.neuid}">
                                    <input type="hidden" name="role" value="Patients">
                                    <button type="submit" class="btn btn-primary btn-sm mb-1 mr-1" style="width: 100px;">Activate</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/admin/deactivateUser" method="post" class="d-inline">
                                    <input type="hidden" name="neuid" value="${patient.neuid}">
                                    <input type="hidden" name="role" value="Patients">
                                     <button type="submit" class="btn btn-danger btn-sm mb-1" style="width: 100px;">Deactivate</button>
 
 								</form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        
        <c:if test="${not empty doctors}">
        <div class="mb-4">
            <h3>Search Doctors</h3>
            <form action="${pageContext.request.contextPath}/admin/searchDoctors" method="get" class="form-inline">
                <label for="searchDoctorCriteria" class="mr-2">Search Doctors By:</label>
                <select name="searchDoctorCriteria" class="form-control mr-2">
                    <option value="all">All</option>
                    <option value="neuid">Doctor ID</option>
                    <option value="name">Name</option>
                    <option value="emailId">Email</option>
                    <option value="specialization">Specialization</option>
                    <option value="active">Account Status</option>
                </select>
                <input type="text" name="searchDoctorQuery" placeholder="Enter search query" class="form-control mr-2"/>
                <input type="submit" value="Search" class="btn btn-primary"/>
            </form>
        </div>

            <h3>List of Doctors</h3>
            <table class="table table-bordered">
               <thead class="thead-light text-center">
                     <tr>
				        <th class="text-nowrap">Doctor ID</th>
				        <th class="text-nowrap">Name</th>
				        <th class="text-nowrap">Email</th>
				        <th class="text-nowrap">Address</th>
				        <th class="text-nowrap">Contact No</th>
				        <th class="text-nowrap">Specialization</th>
				        <th class="text-nowrap">Account Status</th>
				        <th class="text-nowrap">Status Update</th>
				    </tr>
                </thead>
                <tbody>
                    <c:forEach var="doctor" items="${doctors}">
                        <tr class="text-center">
                            <td>${doctor.neuid}</td>
                            <td>${doctor.name}</td>
                            <td>${doctor.emailId}</td>
                            <td>${doctor.address}</td>
                            <td>${doctor.contactNo}</td>
                            <td>${doctor.specialization}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${doctor.active}">
                                        Active
                                    </c:when>
                                    <c:otherwise>
                                        Inactive
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <form action="${pageContext.request.contextPath}/admin/activateUser" method="post" class="d-inline">
                                    <input type="hidden" name="neuid" value="${doctor.neuid}">
                                    <input type="hidden" name="role" value="Doctors">
                                    <button type="submit" class="btn btn-primary btn-sm mb-1 mr-1" style="width: 100px;">Activate</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/admin/deactivateUser" method="post" class="d-inline">
                                    <input type="hidden" name="neuid" value="${doctor.neuid}">
                                    <input type="hidden" name="role" value="Doctors">
                                    <button type="submit" class="btn btn-danger btn-sm mb-1" style="width: 100px;">Deactivate</button>                               
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
  

   
   
 