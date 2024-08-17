package com.company.neuheathcaremanagement.validator;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.company.neuheathcaremanagement.pojo.User;

@Component
public class UserValidator implements Validator {

	private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!])[A-Za-z\\d@#$%^&+=!]{5,}$";
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	private static final String CONTACT_NUMBER_REGEX = "^\\d{10}$";
	private static final String PASSWORD_ERROR_CODE = "error.password";
	private static final String PASSWORD_ERROR_MESSAGE = "Password must be at least 5 characters long, contain both letters and numbers, and include at least one special character.";
	private static final String EMAIL_ERROR_CODE = "error.emailId";
	private static final String EMAIL_ERROR_MESSAGE = "Email must be a valid format.";
	private static final String CONTACT_NUMBER_ERROR_CODE = "error.contactNo";
	private static final String CONTACT_NUMBER_ERROR_MESSAGE = "Contact number must be exactly 10 digits.";
	private static final String DOB_ERROR_CODE = "error.dob";
	private static final String DOB_ERROR_MESSAGE = "Date of birth cannot be in the future.";

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.name", "Name must not be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailId", EMAIL_ERROR_CODE, "Email must not be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "error.address", "Address must not be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactNo", CONTACT_NUMBER_ERROR_CODE,
				"Contact number must not be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "error.gender", "Gender must not be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dob", "error.dob", "Date of birth must not be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userRole", "error.userRole", "User role must not be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", PASSWORD_ERROR_CODE,
				"Password must not be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", PASSWORD_ERROR_CODE,
				"Confirm password must not be empty.");
		if ("doctor".equals(user.getUserRole())) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "specialization", "error.specialization", "Specialization must not be empty.");
        }

		if (user.getEmailId() != null && !user.getEmailId().isEmpty() && !user.getEmailId().matches(EMAIL_REGEX)) {
			errors.rejectValue("emailId", EMAIL_ERROR_CODE, EMAIL_ERROR_MESSAGE);
		}

		if (user.getContactNo() != null && !user.getContactNo().isEmpty()
				&& !user.getContactNo().matches(CONTACT_NUMBER_REGEX)) {
			errors.rejectValue("contactNo", CONTACT_NUMBER_ERROR_CODE, CONTACT_NUMBER_ERROR_MESSAGE);
		}

		if (user.getDob() != null) {
			LocalDate dob = user.getDob();
			if (dob.isAfter(LocalDate.now())) {
				errors.rejectValue("dob", DOB_ERROR_CODE, DOB_ERROR_MESSAGE);
			}
		}

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

	public void validateUpdate(User user, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.name", "Name must not be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailId", EMAIL_ERROR_CODE, "Email must not be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "error.address", "Address must not be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactNo", CONTACT_NUMBER_ERROR_CODE,
				"Contact number must not be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dob", "error.dob", "Date of birth must not be empty.");

		if (user.getEmailId() != null && !user.getEmailId().isEmpty() && !user.getEmailId().matches(EMAIL_REGEX)) {
			errors.rejectValue("emailId", EMAIL_ERROR_CODE, EMAIL_ERROR_MESSAGE);
		}

		if (user.getContactNo() != null && !user.getContactNo().isEmpty()
				&& !user.getContactNo().matches(CONTACT_NUMBER_REGEX)) {
			errors.rejectValue("contactNo", CONTACT_NUMBER_ERROR_CODE, CONTACT_NUMBER_ERROR_MESSAGE);
		}

		if (user.getDob() != null) {
			LocalDate dob = user.getDob();
			if (dob.isAfter(LocalDate.now())) {
				errors.rejectValue("dob", DOB_ERROR_CODE, DOB_ERROR_MESSAGE);
			}
		}

	}

	public void validateLogin(Object target, Errors errors) {
		User user = (User) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailId", EMAIL_ERROR_CODE, "Email must not be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", PASSWORD_ERROR_CODE,
				"Password must not be empty.");

		if (user.getEmailId() != null && !user.getEmailId().isEmpty() && !user.getEmailId().matches(EMAIL_REGEX)) {
			errors.rejectValue("emailId", EMAIL_ERROR_CODE, EMAIL_ERROR_MESSAGE);
		}

		if (user.getConfirmPassword() != null && !user.getConfirmPassword().isEmpty()) {

			errors.rejectValue("password", PASSWORD_ERROR_CODE, "Password must not be empty.");
		}
	}

	public void validateResetPassword(Object target, Errors errors) {
		String email = (String) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailId", EMAIL_ERROR_CODE, "Email must not be empty.");

		if (email != null && !email.isEmpty() && !email.matches(EMAIL_REGEX)) {
			errors.rejectValue("emailId", EMAIL_ERROR_CODE, EMAIL_ERROR_MESSAGE);
		}

	}
	
	public void validateUserRole(Object target, Errors errors) {
		User user = (User) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userRole", "error.userRole", "User role must not be empty.");
		if ("doctor".equals(user.getUserRole())) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "specialization", "error.specialization", "Specialization must not be empty.");
        }
		}
}
