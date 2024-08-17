package com.company.neuheathcaremanagement.dao;

import java.util.Collections;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.company.neuheathcaremanagement.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Repository
@Transactional
public class UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    @PersistenceContext
    private EntityManager entityManager;
    
    public List<User> findDoctorsBySpecialization(String specialization) {
        try {
            String jpql = "SELECT u FROM User u WHERE u.specialization = :specialization AND u.userRole = 'doctor' AND u.active = true";
            TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
            query.setParameter("specialization", specialization);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error finding doctors by specialization: {}", specialization, e);
            return Collections.emptyList();
        }
    }


    public void saveUser(User user) {
        try {
            entityManager.persist(user);
            logger.info("User saved successfully.");
        } catch (Exception e) {
            logger.error("Error saving user: {}", user, e);
        }
    }
    
    public void saveUserWithoutPassword(User user) {
        try {
            entityManager.persist(user);
            logger.info("Saving user: {}", user);
            logger.info("User updated successfully.");
        } catch (Exception e) {
            logger.error("Error saving user: {}", user, e);
        }
    }

    public boolean userCheck(String email) {
        try {
            String jpql = "SELECT COUNT(u) FROM User u WHERE u.emailId = :email";
            TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
            query.setParameter("email", email);
            Long count = query.getSingleResult();
            boolean exists = count > 0;
            logger.info("Checking user existence for email '{}': {}", email, exists);
            return exists;
        } catch (Exception e) {
            logger.error("Error checking user existence for email '{}'", email, e);
            return false;
        }
    }
    
    public User loginUser(String email, String password) {
        try {     
            String jpql = "FROM User WHERE emailId = :email";
            TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
            query.setParameter("email", email);

            User user = query.getSingleResult();
            
            if (BCrypt.checkpw(password, user.getPassword())) {
                logger.info("User '{}' logged in successfully.", email);
                return user;
            } else {
                logger.info("Invalid login attempt for email '{}'", email);
                return null;
            }
        } catch (NoResultException e) {
            logger.info("No user found with email '{}'", email);
            return null;
        } catch (Exception e) {
            logger.error("Error logging in user with email '{}'", email, e);
            return null;
        }
    }

    public User findByEmailId(String emailId) {
        try {
            String jpql = "FROM User WHERE emailId = :emailId";
            TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
            query.setParameter("emailId", emailId);
            User user = query.getSingleResult();
            logger.info("Found user by email '{}': {}", emailId, user);
            return user;
        } catch (Exception e) {
            logger.error("Error finding user by email '{}'", emailId, e);
            return null;
        }
    }

    public User findById(int id) {
        try {
            User user = entityManager.find(User.class, id);
            logger.info("Found user by ID '{}': {}", id, user);
            return user;
        } catch (Exception e) {
            logger.error("Error finding user by ID '{}'", id, e);
            return null;
        }
    }
    
    public User updateUser(User user) {
        try {
            User existingUser = entityManager.find(User.class, user.getNeuid());
            if (existingUser != null) {
                logger.info("Existing user found: {}", existingUser);
                existingUser.setName(user.getName());
                existingUser.setEmailId(user.getEmailId());
                existingUser.setDob(user.getDob());
                existingUser.setContactNo(user.getContactNo());
                existingUser.setAddress(user.getAddress());
                existingUser.setGender(user.getGender());
                existingUser.setActive(user.isActive());
                logger.info("Updating user: {}", existingUser);
                entityManager.merge(existingUser);
                logger.info("User updated successfully.");
                return existingUser; 
            } else {
                logger.info("User not found with ID: {}", user.getNeuid());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error updating user: {}", user, e);
            return null;
        }
    }

    public List<User> findDoctors() {
        try {
            String jpql = "FROM User WHERE userRole = 'doctor'";
            TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
            List<User> doctors = query.getResultList();
            logger.info("Found {} doctors", doctors.size());
            return doctors;
        } catch (Exception e) {
            logger.error("Error finding doctors", e);
            return Collections.emptyList();
        }
    }

    public List<User> getUsersByRole(String role) {
        try {
            String jpql = "FROM User WHERE userRole = :role";
            TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
            query.setParameter("role", role);
            List<User> users = query.getResultList();
            logger.info("Found {} users with role '{}'", users.size(), role);
            return users;
        } catch (Exception e) {
            logger.error("Error finding users by role '{}'", role, e);
            return Collections.emptyList();
        }
    }

    public User findByResetToken(String token) {
        try {
            String jpql = "FROM User WHERE resetToken = :token";
            TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
            query.setParameter("token", token);
            User user = query.getSingleResult();
            logger.info("Found user by reset token '{}': {}", token, user);
            return user;
        } catch (Exception e) {
            logger.error("Error finding user by reset token '{}'", token, e);
            return null;
        }
    }

    
    public void updateResetToken(String email, String token) {
        try {
            String sql = "UPDATE user_management SET reset_token = ? WHERE email_id = ?";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, token);
            query.setParameter(2, email);
            int result = query.executeUpdate();
            if (result > 0) {
                logger.info("Reset token updated successfully for email: {}", email);
            } else {
                logger.warn("No user found with email: {}", email);
            }
        } catch (Exception e) {
            logger.error("Error updating reset token for email: {}", email, e);
        }
    }
}

