package com.company.neuheathcaremanagement.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorPageController implements ErrorController {
	@GetMapping("/error")
	public String handleErrorPage(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());

			if (statusCode == HttpStatus.FORBIDDEN.value()) {
				return "403ErrorPage";
			} else if (statusCode == HttpStatus.NOT_FOUND.value()) {
				return "404ErrorPage";
			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				return "500ErrorPage";
			} else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
				return "400ErrorPage";
			}else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
			    return "400ErrorPage";
			}
		}
		return "error";
	}
}
