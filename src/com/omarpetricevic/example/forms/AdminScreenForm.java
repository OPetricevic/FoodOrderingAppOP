package com.omarpetricevic.example.forms;

import com.omarpetricevic.example.model.User;
import com.omarpetricevic.example.model.UserTableModel;
import com.omarpetricevic.example.service.UserService;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class AdminScreenForm {
    private JPanel adminField;
    private JTable userTable;
    private JTextField userIDTextField;
    private JTextField fullNameTextField;
    private JPasswordField passwordPasswordField;
    private JTextField emailTextField;
    private JLabel phoneNumberLabel;
    private JTextField phoneNumberTextField;
    private String[] userTypes = {"Customer", "Manager", "Kitchen"};
    private JButton saveButton;
    private JTextField userTypeTextField;
    private JButton backButton;
    private JButton deleteButton;
    private JButton createButton;
    private JScrollPane userTableScroll;
    private JLabel preferredRoleValueLabel;
    private JLabel preferredRole;
    private JLabel userType;
    private JLabel phoneNumber;
    private JLabel passwordLabel;
    private JLabel emailLabel;
    private JLabel fullNameLabel;
    private JLabel userIDLabel;
    private UserService userService = new UserService();

    public AdminScreenForm() {
        setupUserTableModel();
        setupListeners();
    }

    private void setupListeners() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIDTextField.getText();
                String fullName = fullNameTextField.getText();
                String email = emailTextField.getText();
                String password = new String(passwordPasswordField.getPassword());
                String phoneNumber = phoneNumberTextField.getText();
                String userType = (String) userTypeTextField.getText();

                if (validateInput(fullName, email, password, phoneNumber, userType)) {
                    updateUser(userId, fullName, email, password, phoneNumber, userType);
                    clearTextFields();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid input!");
                }
            }
        });

        userTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = userTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        userIDTextField.setText(userTable.getValueAt(selectedRow, 0).toString());
                        fullNameTextField.setText(userTable.getValueAt(selectedRow, 1).toString());
                        emailTextField.setText(userTable.getValueAt(selectedRow, 2).toString());
                        phoneNumberTextField.setText(userTable.getValueAt(selectedRow, 4).toString());
                        userTypeTextField.setText(userTable.getValueAt(selectedRow, 5).toString());
                        preferredRoleValueLabel.setText(userTable.getValueAt(selectedRow, 6).toString());
                    }
                }
            }
        });
        backButton.addActionListener(e -> backToLogin());
        createButton.addActionListener(e -> createUser());
        deleteButton.addActionListener(e -> deleteUser());
    }

    private void deleteUser() {
        String userId = userIDTextField.getText();

        if (userId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select a user to delete.");
            return;
        }
        Long id = Long.parseLong(userId);

        boolean success = userService.deleteUserById(id);

        if (success) {
            JOptionPane.showMessageDialog(null, "User deleted successfully!");
            setupUserTableModel();
            clearTextFields();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to delete user.");
        }
    }

    private void createUser() {
        String fullName = fullNameTextField.getText();
        String password = new String(passwordPasswordField.getPassword());
        String email = emailTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String userType = (String) userTypeTextField.getText();

        if (!userType.equals("Kitchen") && !userType.equals("Customer") && !userType.equals("Manager")) {
            JOptionPane.showMessageDialog(null, "User Type must be either 'Kitchen,' 'Customer,' or 'Manager.'");
            return;
        }
        String preferredRole;
        if (userType.equals("Kitchen") || userType.equals("Customer") || userType.equals("Manager")) {
            preferredRole = userType;
        } else {
            preferredRole = "Customer";
        }

        if (validateInput(fullName, email, password, phoneNumber, userType)) {
            boolean success = userService.registerUser(fullName, password, email, phoneNumber, userType, preferredRole);

            if (success) {
                JOptionPane.showMessageDialog(null, "User created successfully!");
                setupUserTableModel();
                clearTextFields();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to create user.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid input!");
        }
    }

    private void backToLogin() {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(adminField);
        if (currentFrame != null) {
            currentFrame.dispose();
        }

        // Open the Login Screen
        UserService userService = new UserService();
        LoginScreenForm loginForm = new LoginScreenForm(userService);
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setContentPane(loginForm.getMainPanel());
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    private void updateUser(String userId, String fullName, String email, String password, String phoneNumber, String userType) {
        Long id = Long.parseLong(userId);
        String preferredRole = preferredRoleValueLabel.getText();

        if (password.equals("")) {
            try {
                boolean success = userService.updateUser(id, fullName, email, null, phoneNumber, userType, preferredRole);
                handleUpdateResult(success);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }

        if (!password.equals("") && password.length() >= 8) {
            try {
                boolean success = userService.updateUser(id, fullName, email, password, phoneNumber, userType, preferredRole);
                handleUpdateResult(success);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }

        if (!password.equals("") && password.length() < 8) {
            JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long.");
        }
    }

    private void handleUpdateResult(boolean success) {
        if (success) {
            JOptionPane.showMessageDialog(null, "User updated successfully!");
            setupUserTableModel();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to update user.");
        }
    }

    private boolean validateInput(String fullName, String email, String password, String phoneNumber, String userType) {
        if (fullName == null || fullName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Full Name cannot be empty.");
            return false;
        }

        if (email == null || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            JOptionPane.showMessageDialog(null, "Please enter a valid email address.");
            return false;
        }

        if (password == null || password.length() < 8) {
            JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long.");
            return false;
        }
        if (!userType.equals("Manager") && !userType.equals("Customer") && !userType.equals("Kitchen")) {
            JOptionPane.showMessageDialog(null, "User Type must be either 'Manager', 'Customer', or 'Kitchen'.");
            return false;
        }

        return true;
    }

    private void setupUserTableModel() {
        List<User> users = userService.getAllUsers();
        UserTableModel tableModel = new UserTableModel(users);
        userTable.setModel(tableModel);
    }

        public JPanel getAdminPanel () {
            return adminField;
        }
        private void clearTextFields () {
            userIDTextField.setText("");
            fullNameTextField.setText("");
            emailTextField.setText("");
            passwordPasswordField.setText("");
            phoneNumberTextField.setText("");
            userTypeTextField.setText("");
        }
}
