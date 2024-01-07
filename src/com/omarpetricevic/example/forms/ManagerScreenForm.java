package com.omarpetricevic.example.forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.omarpetricevic.example.dao.OrdersDAO;
import com.omarpetricevic.example.model.Orders;
import com.omarpetricevic.example.service.UserService;

import java.util.List;

public class ManagerScreenForm {
    private JPanel managerPanel;
    private JTable showAllPastOrders;
    private JButton backButton;
    private JLabel completedProfitValueLabel;
    private JTable notCompletedOrders;
    private JLabel notCompletedProfitLabel;

    private OrdersDAO ordersDAO;

    public ManagerScreenForm() {
        ordersDAO = new OrdersDAO();
        initializeForm();
    }

    private void initializeForm() {
        setupTables();
        updateProfitLabels();
        setupListeners();
    }

    private void setupTables() {
        populateCompletedOrdersTable();
        populateOngoingOrdersTable();
    }

    private void populateCompletedOrdersTable() {
        List<Orders> completedOrders = ordersDAO.getAllPastOrders();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Order ID", "Item Name", "Order Date", "Price"});
        for (Orders order : completedOrders) {
            model.addRow(new Object[]{
                    order.getOrderID(),
                    order.getItemName(),
                    order.getOrderDate().toString(),
                    order.getPrice()
            });
        }
        showAllPastOrders.setModel(model);
    }

    private void populateOngoingOrdersTable() {
        List<Orders> ongoingOrders = ordersDAO.getAllOngoingOrders();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Order ID", "Item Name", "Order Date", "Price"});
        for (Orders order : ongoingOrders) {
            model.addRow(new Object[]{
                    order.getOrderID(),
                    order.getItemName(),
                    order.getOrderDate().toString(),
                    order.getPrice()
            });
        }
        notCompletedOrders.setModel(model);
    }

    private void updateProfitLabels() {
        double completedProfit = ordersDAO.getSumOfAllCompletedOrders();
        completedProfitValueLabel.setText(String.format("€ %.2f", completedProfit));

        double ongoingProfit = ordersDAO.getSumOfAllPendingOrders();
        notCompletedProfitLabel.setText(String.format("€ %.2f", ongoingProfit));
    }

    private void setupListeners() {
        backButton.addActionListener(e -> backToLogin());
    }

    private void backToLogin(){
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(managerPanel);
        if (currentFrame != null) {
            currentFrame.dispose();
        }

        UserService userService = new UserService();
        LoginScreenForm loginForm = new LoginScreenForm(userService);
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setContentPane(loginForm.getMainPanel());
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }


    public JPanel getManagerPanel() {
        return managerPanel;
    }
}
