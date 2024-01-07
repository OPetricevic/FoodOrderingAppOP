package com.omarpetricevic.example.forms;

import com.omarpetricevic.example.service.UserService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationScreenForm {
    private JTextField fullNameTextField;
    private JTextField emailTextField;
    private JPasswordField passwordTextField;
    private JTextField phoneNumberTextField;
    private JRadioButton kitchenRadioButton;
    private JRadioButton managerRadioButton;
    private JButton registerButton;
    private JButton backToLogin;
    private JPanel registrationPanel;
    private JLabel passwordDesc;
    private JLabel emailDesc;
    private JLabel preferredRoleLabel;
    private JLabel preferredRoleDesc;
    private JLabel phoneNumberDesc;
    private JLabel phoneNumberLabel;
    private JLabel passwordLabel;
    private JLabel emailLabel;
    private JLabel fullNameLabel;
    private JLabel requirementsLabel;

    private UserService userService;

    public RegistrationScreenForm(UserService userService) {
        this.userService = userService;

        ButtonGroup roleButtonGroup = new ButtonGroup();
        roleButtonGroup.add(kitchenRadioButton);
        roleButtonGroup.add(managerRadioButton);

        backToLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new LoginScreenForm frame
                JFrame loginFrame = new JFrame("Login");
                LoginScreenForm loginForm = new LoginScreenForm(userService);
                loginFrame.setContentPane(loginForm.getMainPanel());
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.pack();
                loginFrame.setVisible(true);

                SwingUtilities.getWindowAncestor(registrationPanel).dispose();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fullName = fullNameTextField.getText();
                String email = emailTextField.getText();
                String password = new String(passwordTextField.getPassword());
                String phoneNumber = phoneNumberTextField.getText();
                String preferredRole = kitchenRadioButton.isSelected() ? "Kitchen" :
                        managerRadioButton.isSelected() ? "Manager" : "Customer";

                String userType = "Customer";


                if (fullName.isEmpty() || email.isEmpty() || password.length() < 8) {
                    JOptionPane.showMessageDialog(null, "Please fill in all required fields and ensure the password is at least 8 characters.");
                    return;
                }
                if (!isValidEmail(email)) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid email address.");
                    return;
                }
                boolean success = userService.registerUser(fullName, password, email, phoneNumber, userType, preferredRole);

                if (success) {
                    JOptionPane.showMessageDialog(null, "Registration successful!");
                } else {
                    JOptionPane.showMessageDialog(null, "Registration failed!");
                }
            }
            private boolean isValidEmail(String email) {
                String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
                return email.matches(emailRegex);
            }
        });
    }
    public JPanel getMainPanel() {
        return registrationPanel;
    }

}
