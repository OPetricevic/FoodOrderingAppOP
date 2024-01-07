package com.omarpetricevic.example;

import com.omarpetricevic.example.service.UserService;
import com.omarpetricevic.example.forms.LoginScreenForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserService userService = new UserService();
            LoginScreenForm loginForm = new LoginScreenForm(userService);
            JFrame frame = new JFrame("Login");
            frame.setContentPane(loginForm.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
