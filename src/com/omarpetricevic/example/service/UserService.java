package com.omarpetricevic.example.service;

import com.omarpetricevic.example.model.User;
import com.omarpetricevic.example.dao.UserDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UserService {

    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    public boolean registerUser(String fullName, String plainPassword, String email, String phoneNumber, String userType, String preferredRole) {
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        User newUser = new User(fullName, hashedPassword, email, phoneNumber, userType, preferredRole);
        return userDAO.insertUser(newUser);
    }
    public boolean updateUser(Long userId, String fullName, String email, String plainPassword, String phoneNumber, String userType, String preferredRole) throws IllegalArgumentException {
        User user = userDAO.findById(userId);
        if (user != null) {
            // Update user details
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setUserType(userType);
            user.setPreferredRole(preferredRole);

            // Check if a password change is requested
            boolean updatePassword = false;
            if (plainPassword != null && !plainPassword.trim().isEmpty()) {
                if (plainPassword.length() < 8) {
                    // Password is provided but less than 8 characters
                    throw new IllegalArgumentException("Password must be at least 8 characters long.");
                } else {
                    // Password is valid, hash it and update
                    String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
                    user.setPasswordHash(hashedPassword);
                    updatePassword = true;
                }
            }
            // If plainPassword is empty, do not update the password

            // Update the user in the database
            return userDAO.updateUser(user, updatePassword);
        }
        return false;
    }

    public boolean authenticateUser(String email, String plainPassword) {
        if ("admin".equals(email) && "admin".equals(plainPassword)) {
            return true;
        }

        User user = userDAO.findByEmail(email);
        if (user != null) {
            return BCrypt.checkpw(plainPassword, user.getPasswordHash());
        }
        return false;
    }
    public long authenticateUserAndGetId(String email, String plainPassword) {
        if ("admin".equals(email) && "admin".equals(plainPassword)) {
            return -1;
        }

        User user = userDAO.findByEmail(email);
        if (user != null && BCrypt.checkpw(plainPassword, user.getPasswordHash())) {
            return user.getId();
        }
        return -1;
    }
    public String getUserType(String email) {
        User user = userDAO.findByEmail(email);
        if (user != null) {
            return user.getUserType();
        }
        return null;
    }
    public boolean deleteUserById(Long userId) {
        try {
            boolean success = userDAO.deleteUserById(userId);

            if (success) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
