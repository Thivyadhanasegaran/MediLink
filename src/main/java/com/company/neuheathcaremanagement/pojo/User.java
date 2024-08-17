package com.company.neuheathcaremanagement.pojo;

import java.io.Serializable;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="user_management")
public class User implements Serializable {
	
    private static final long serialVersionUID = 1L; 
	
	@Id
	@Column(name="neuid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int neuid;
	
	@Column(name = "name", nullable = false, length = 50)
    private String name;
	
	@Column(name = "password")
    private String password;
	
	@Transient
    private String confirmPassword;
	
	@Column(name = "email_id", nullable = false, unique = true, length = 100)
    private String emailId;
	
	@Column(name = "address", length = 255)
    private String address;
	
	@Column(name = "contact_no", length = 20)
    private String contactNo;
	
	@Column(name = "gender", length = 10)
    private String gender;
	
	@Column(name = "dob")
    private LocalDate dob;
	
	@Column(name="user_role")
	private String userRole;
	
	@Column(name="specialization")
	private String specialization;
	
	@Column(name = "active", columnDefinition = "boolean default true")
	private boolean active = true;

	@Column(name="reset_token")
	private String resetToken;
	
	public User() {
	}

	public int getNeuid() {
		return neuid;
	}

	public void setNeuid(int neuid) {
		this.neuid = neuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}
}
