package com.company.neuheathcaremanagement.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.company.neuheathcaremanagement.dao.UserDAO;
import com.company.neuheathcaremanagement.pojo.User;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserDAO userDAO;

   @CacheEvict(value = "users", allEntries = true)
    public void saveUser(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        userDAO.saveUser(user);
    }
    
    @CacheEvict(value = "users", allEntries = true)
    public void saveUserWithoutPassword(User user) {
        userDAO.saveUserWithoutPassword(user);
    }

    public boolean userExists(String email) {
        return userDAO.userCheck(email);
    }
    
    public User loginUser(String email, String password) {
         User user = userDAO.loginUser(email,password);
         if (user == null) {
             return null;
         }
          else {
             return user;
         }
     }

   @Cacheable(value = "users", key = "#email")
	public User findUserByEmail(String email) {
    System.out.println("Caching User details by Email ID");
        return userDAO.findByEmailId(email);
    }
   
   @CachePut(value = "users", key = "#user.emailId")
   public User updateUser(User user) {
	    System.out.println("Updating user information in the database and cache");
       User updatedUser = userDAO.updateUser(user);
       return updatedUser;
   }

    public List<User> findDoctors() {
        return userDAO.findDoctors();
    }
    
    public List<User> findDoctorsBySpecialization(String specialization) {
        return userDAO.findDoctorsBySpecialization(specialization);
    }

    public List<User> getUsersByRole(String role) {
        return userDAO.getUsersByRole(role);
    }

    public User findByResetToken(String token) {
        return userDAO.findByResetToken(token);
    }
    
    public User findById(int id) {
        return userDAO.findById(id);
    }

   @CacheEvict(value = "users", key = "#email")
    public void updateResetToken(String email, String token) {
        userDAO.updateResetToken(email, token);
    }
    
    public List<User> getAllPatients(String role) {
    	 return userDAO.getUsersByRole(role);
    }

    public List<User> getAllDoctors(String role) {
    	 return userDAO.getUsersByRole(role);
    }
    
    public List<User> getFilteredPatients(String criteria, String query, String role) {
        List<User> patients = userDAO.getUsersByRole(role);
        return filterPatients(patients, criteria, query);
    }

    public List<User> getFilteredDoctors(String criteria, String query, String role) {
        List<User> doctors = userDAO.getUsersByRole(role);
        return filterDoctors(doctors, criteria, query);
    }

    private List<User> filterPatients(List<User> patients, String criteria, String query) {
        return patients.stream().filter(patient -> {
        	if ("active".equalsIgnoreCase(criteria)) {
                boolean isActive = "active".equalsIgnoreCase(query);
                return patient.isActive() == isActive;
            }
        	
        	switch (criteria) {
                case "neuid":
                    return String.valueOf(patient.getNeuid()).contains(query);
                case "name":
                    return patient.getName().toLowerCase().contains(query.toLowerCase());
                case "emailId":
                    return patient.getEmailId().toLowerCase().contains(query.toLowerCase());
                default:
                    return false;
            }
        }).collect(Collectors.toList());
    }

    private List<User> filterDoctors(List<User> doctors, String criteria, String query) {
        return doctors.stream().filter(doctor -> {
        	 if ("active".equalsIgnoreCase(criteria)) {
                 boolean isActive = "active".equalsIgnoreCase(query);
                 return doctor.isActive() == isActive;
             }
        	 switch (criteria) {
                case "neuid":
                    return String.valueOf(doctor.getNeuid()).contains(query);
                case "name":
                    return doctor.getName().toLowerCase().contains(query.toLowerCase());
                case "emailId":
                    return doctor.getEmailId().toLowerCase().contains(query.toLowerCase());
                case "specialization":
                    return doctor.getSpecialization().toLowerCase().contains(query.toLowerCase());
                default:
                    return false;
            }
        }).collect(Collectors.toList());
    }


}
