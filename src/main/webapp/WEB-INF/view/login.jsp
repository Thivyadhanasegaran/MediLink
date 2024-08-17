<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login - NEU Student Healthcare Management System</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/loginStyles.css">
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
		<div class="image-container">
			<div class="image-top">
				<img src="${pageContext.request.contextPath}/images/neulogin.jpg"
					alt="Top Image">
			</div>
		</div>
		<div class="login-container">
			<h2>Login</h2>
			<form action="userlogin" method="post">
				<c:if test="${not empty errors}">
					<div class="error">${errors}</div>
				</c:if>
				<div class="form-group">
					<label for="email">Email ID:</label> <input type="text"
						name="emailid" value="${emailid}">
				</div>
				<div class="form-group">
					<label for="password">Password:</label> <input type="password"
						name="password">
				</div>
				<button type="submit">Login</button>
				<div class="links">
					<a href="oauth2/authorization/google" class="google-link">

						Continue with Google <img
						src="https://www.gstatic.com/firebasejs/ui/2.0.0/images/auth/google.svg"
						alt="Google Icon" class="google-icon">
					</a> <a href="registerForm">Don't have an account? Register here</a> <a
						href="forgotPassword">Forgot Password?</a>
				</div>
			</form>
		</div>
	</div>

	<footer class="footer">
		<div class="footer-content">
			<p>&copy; 2024 NEU Student Healthcare Management System. All
				rights reserved.</p>
			<p>
				<a href="https://uhcs.northeastern.edu/about-uhcs/" target="_blank">About
					Us</a> | <a href="https://uhcs.northeastern.edu/health-and-wellness/"
					target="_blank">Resources</a> | <a
					href="https://www.northeastern.edu/privacy-information/"
					target="_blank">Privacy Policy</a> | <a
					href="https://uhcs.northeastern.edu/about-uhcs/contact-us/"
					target="_blank">Contact Us</a>
			</p>
			<p>
				Follow us: <a href="https://www.facebook.com/northeastern/"
					target="_blank">Facebook</a> | <a href="https://x.com/Northeastern"
					target="_blank">Twitter</a> | <a
					href="https://www.instagram.com/northeastern/" target="_blank">Instagram</a>

			</p>
		</div>
	</footer>
</body>
</html>
