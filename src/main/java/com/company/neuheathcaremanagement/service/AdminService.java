package com.company.neuheathcaremanagement.service;

import com.company.neuheathcaremanagement.pojo.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class AdminService {
	
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
	private UserService userService;

    public List<User> getUsersByRole(String role) {
        List<User> users = userService.getUsersByRole(role);
        logger.info("Fetched {} users with role: {}", users.size(), role);
        return users;
    }

    public void activateUser(int neuid) {
        User user = userService.findById(neuid);
        if (user != null) {
            user.setActive(true);
            userService.updateUser(user);
            logger.info("User with NEU ID: {} activated", neuid);
        } else {
            logger.warn("User with NEU ID: {} not found", neuid);
        }
    }

    public void deactivateUser(int neuid) {
        User user = userService.findById(neuid);
        if (user != null) {
            user.setActive(false);
            userService.updateUser(user);
            logger.info("User with NEU ID: {} deactivated", neuid);
        } else {
            logger.warn("User with NEU ID: {} not found", neuid);
        }
    }

    public void createResetToken(String email, String token) {
        userService.updateResetToken(email, token);
        logger.info("Reset token created for email: {}", email);
    }
}
