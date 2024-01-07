package com.omarpetricevic.example.dao;

import com.omarpetricevic.example.model.User;
import com.omarpetricevic.example.util.DatabaseUtil;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class UserDAO {

    public UserDAO() {
    }


    public boolean insertUser(User user) {
        String sql = "INSERT INTO Users (FullName, PasswordHash, Email, PhoneNumber, UserType, PreferredRole) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getUserType());
            pstmt.setString(6, user.getPreferredRole());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public User findById(Long userId) {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return createUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE Email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return createUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User createUserFromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("UserID");
        String fullName = rs.getString("FullName");
        String passwordHash = rs.getString("PasswordHash");
        String email = rs.getString("Email");
        String phoneNumber = rs.getString("PhoneNumber");
        String userType = rs.getString("UserType");
        String preferredRole = rs.getString("PreferredRole");

        return new User(id, fullName, passwordHash, email, phoneNumber, userType, preferredRole);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = createUserFromResultSet(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean updateUser(User user, boolean updatePassword) {
        String sql = "UPDATE Users SET FullName = ?, Email = ?, PhoneNumber = ?, UserType = ?, PreferredRole = ?"
                + (updatePassword ? ", PasswordHash = ?" : "")
                + " WHERE UserID = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhoneNumber());
            pstmt.setString(4, user.getUserType());
            pstmt.setString(5, user.getPreferredRole());

            if (updatePassword) {
                pstmt.setString(6, user.getPasswordHash());
                pstmt.setLong(7, user.getId());
            } else {
                pstmt.setLong(6, user.getId());
            }

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUserById(Long userId) {
        String sql = "DELETE FROM Users WHERE UserID = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}