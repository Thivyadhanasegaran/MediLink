package com.company.neuheathcaremanagement.service;

import com.company.neuheathcaremanagement.pojo.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ResetPasswordService {

	@Autowired
	private UserService userService;

    public User findUserByResetToken(String token) {
    	  return userService.findByResetToken(token);
    }

    public boolean resetPassword(String token, String password) {
    	 User user = userService.findByResetToken(token);
        if (user != null) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            user.setPassword(hashedPassword);
            user.setResetToken(null);
            userService.updateUser(user);
            return true;
        }
        return false;
    }   
}
