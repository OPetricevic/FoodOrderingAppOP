package com.omarpetricevic.example.forms;

import com.omarpetricevic.example.service.UserService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreenForm {
    private UserService userService;
    private JTextField emailTextField;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JButton registrationButton;
    private JButton loginButton;
    private JPanel loginPanel;
    private JPasswordField passwordField;


    public LoginScreenForm(UserService userService) {
        this.userService = userService;

        registrationButton.addActionListener(e -> {
            JFrame registrationFrame = new JFrame("Registration");
            RegistrationScreenForm registrationForm = new RegistrationScreenForm(this.userService);
            registrationFrame.setContentPane(registrationForm.getMainPanel());
            registrationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            registrationFrame.pack();
            registrationFrame.setVisible(true);

            SwingUtilities.getWindowAncestor(loginPanel).dispose();
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());

                if (userService.authenticateUser(email, password)) {
                    // Admin check
                    if ("admin".equals(email) && "admin".equals(password)) {
                        openAdminScreen();
                        JOptionPane.showMessageDialog(null, "Admin login successful!");
                        return;
                    }

                    // Non-admin users
                    String userType = userService.getUserType(email);
                    switch (userType) {
                        case "Kitchen":
                            openKitchenScreen();
                            JOptionPane.showMessageDialog(null, "Kitchen login successful!");
                            break;
                        case "Customer":
                            long userId = userService.authenticateUserAndGetId(email, password);
                            if (userId != -1) {
                                openCustomerScreen(userId);
                                JOptionPane.showMessageDialog(null, "Customer login successful!");
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to retrieve customer ID.");
                            }
                            break;
                        case "Manager":
                            openManagerScreen();
                            JOptionPane.showMessageDialog(null, "Manager login successful!");
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "User type not recognized or login failed.");
                            break;
                    }
                } else {
                    // Login failed
                    JOptionPane.showMessageDialog(null, "Login failed. Please check your credentials.");
                }
            }
        });
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton.doClick();
            }
        });
        emailTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton.doClick();
            }
        });
    }

    private void openAdminScreen() {

        SwingUtilities.getWindowAncestor(loginPanel).dispose();


        JFrame adminFrame = new JFrame("Admin Dashboard");
        AdminScreenForm adminForm = new AdminScreenForm();
        adminFrame.setContentPane(adminForm.getAdminPanel());
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.pack();
        adminFrame.setLocationRelativeTo(null);
        adminFrame.setVisible(true);
    }
    private void openKitchenScreen() {

        SwingUtilities.getWindowAncestor(loginPanel).dispose();


        JFrame restaurantFrame = new JFrame("Kitchen Dashboard");
        KitchenScreenForm restaurantForm = new KitchenScreenForm();
        restaurantFrame.setContentPane(restaurantForm.getRestaurantPanel());
        restaurantFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        restaurantFrame.pack();
        restaurantFrame.setLocationRelativeTo(null);
        restaurantFrame.setVisible(true);
    }
    private void openCustomerScreen(long userId) {

        SwingUtilities.getWindowAncestor(loginPanel).dispose();


        JFrame customerFrame = new JFrame("Customer Dashboard");
        CustomerScreenForm customerForm = new CustomerScreenForm(userId);
        customerFrame.setContentPane(customerForm.getOrderPanel());
        customerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        customerFrame.pack();
        customerFrame.setLocationRelativeTo(null); // Center on screen
        customerFrame.setVisible(true);
    }
    private void openManagerScreen() {
        SwingUtilities.getWindowAncestor(loginPanel).dispose();

        JFrame managerFrame = new JFrame("Manager Dashboard");
        ManagerScreenForm managerForm = new ManagerScreenForm();
        managerFrame.setContentPane(managerForm.getManagerPanel());
        managerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerFrame.pack();
        managerFrame.setLocationRelativeTo(null);
        managerFrame.setVisible(true);
    }
    public JPanel getMainPanel() {
        return loginPanel;
    }
}

