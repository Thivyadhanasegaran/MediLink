package com.company.neuheathcaremanagement;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.company.neuheathcaremanagement.controller.UserController;
import com.company.neuheathcaremanagement.pojo.User;
import com.company.neuheathcaremanagement.service.UserService;
import com.company.neuheathcaremanagement.validator.UserValidator;

public class TestUserController {

	private MockMvc mockMvc;

	@Mock
	private UserService userService;

	@Mock
	private UserValidator userValidator;

	@InjectMocks
	private UserController userController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	@Test
	public void testRegisterPage() throws Exception {
		mockMvc.perform(get("/registerForm"))
		.andExpect(status().isOk())
		.andExpect(view().name("registrationForm"))
		.andExpect(model().attributeExists("user"));
	}

	@Test
	public void testLoginUser_Success() throws Exception {
		User user = new User();
		user.setName("John");
		user.setEmailId("test@example.com");
		user.setPassword("password");
		user.setUserRole("patient");
		user.setActive(true);

		when(userService.loginUser(anyString(), anyString())).thenReturn(user);

		mockMvc.perform(post("/userlogin")
				.param("emailid", "test@example.com")
				.param("password", "password"))
				.andExpect(redirectedUrl("patient/patientHomePage"));
	}

	@Test
	public void testLoginUser_InvalidEmail() throws Exception {
		mockMvc.perform(post("/userlogin")
				.param("emailid", "invalid-email")
				.param("password", "password"))
				.andExpect(status().isOk())
				.andExpect(view().name("login"))
				.andExpect(model().attributeExists("errors"));
	}

}
