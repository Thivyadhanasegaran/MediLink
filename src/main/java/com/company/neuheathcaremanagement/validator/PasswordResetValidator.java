package com.company.neuheathcaremanagement.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.company.neuheathcaremanagement.pojo.User;

@Component
public class PasswordResetValidator implements Validator {

	private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!])[A-Za-z\\d@#$%^&+=!]{5,}$";
	private static final String PASSWORD_ERROR_CODE = "error.password";
	private static final String PASSWORD_ERROR_MESSAGE = "Password must be at least 5 characters long, contain both letters and numbers, and include at least one special character.";

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", PASSWORD_ERROR_CODE,
				"Password must not be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", PASSWORD_ERROR_CODE,
				"Confirm password must not be empty.");

		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			if (!user.getPassword().equals(user.getConfirmPassword())) {
				errors.rejectValue("confirmPassword", "error.confirmPassword",
						"Password and confirm password do not match.");
			}

			if (!user.getPassword().matches(PASSWORD_REGEX)) {
				errors.rejectValue("password", PASSWORD_ERROR_CODE, PASSWORD_ERROR_MESSAGE);
			}
		} else if (user.getConfirmPassword() != null && !user.getConfirmPassword().isEmpty()) {
			errors.rejectValue("password", PASSWORD_ERROR_CODE, "Password must not be empty.");
		}
	}
}
