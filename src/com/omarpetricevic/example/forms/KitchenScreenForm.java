package com.omarpetricevic.example.forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

import com.omarpetricevic.example.dao.OrdersDAO;
import com.omarpetricevic.example.dao.ItemsDAO;
import com.omarpetricevic.example.model.Items;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.omarpetricevic.example.service.UserService;
import com.omarpetricevic.example.model.Orders;


public class KitchenScreenForm {
    private UserService userService;
    private JPanel restarauntPanel;
    private JTextField itemNameTextField;
    private JTextField itemPriceTextField;
    private JLabel orderName;
    private JLabel itemPriceLabel;
    private JLabel foodDescriptionLabel;
    private JButton createItemButton;
    private JButton deleteItem;
    private JTable itemsTable;
    private JButton backButton;
    private JButton updateButton;
    private JTextArea foodDescriptionTextArea;
    private JTable ordersTable;
    private JButton completeOrder;


    public KitchenScreenForm() {
        this.userService = userService;

        populateItemsTable();
        setupListeners();
        populateOngoingOrdersTable();

        updateButton.addActionListener(e -> updateItem());
        createItemButton.addActionListener(e -> createItem());
        deleteItem.addActionListener(e -> deleteSelectedItem());
        backButton.addActionListener(e -> backToLogin());
        completeOrder.addActionListener(e -> markAsCompletedButton());
    }

    private void markAsCompletedButton() {
        // Get the selected row from the JTable
        int selectedRow = ordersTable.getSelectedRow();

        if (selectedRow >= 0) {
            int orderID = (int) ordersTable.getValueAt(selectedRow, 0);

            boolean updated = updateIsCompletedStatus(orderID);

            if (updated) {
                DefaultTableModel model = (DefaultTableModel) ordersTable.getModel();
                model.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update order status.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select an order to mark as completed.");
        }
    }

    private void populateOngoingOrdersTable() {
        OrdersDAO ordersDAO = new OrdersDAO();
        List<Orders> ongoingOrdersList = ordersDAO.getAllOngoingOrders();

        DefaultTableModel model = (DefaultTableModel) ordersTable.getModel();
        model.setRowCount(0);

        model.addColumn("Order ID");
        model.addColumn("Item Name");
        model.addColumn("Order Date");
        model.addColumn("Price");

        for (Orders order : ongoingOrdersList) {
            model.addRow(new Object[]{
                    order.getOrderID(),
                    order.getItemName(),
                    order.getOrderDate().toString(),
                    order.getPrice(),
            });
        }
        ordersTable.setModel(model);
    }
    private void backToLogin(){
        // Dispose of the current window
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(restarauntPanel);
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

    private void setupListeners() {
        itemsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = itemsTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        itemNameTextField.setText(itemsTable.getValueAt(selectedRow, 1).toString());
                        itemPriceTextField.setText(itemsTable.getValueAt(selectedRow, 2).toString());
                        foodDescriptionTextArea.setText(itemsTable.getValueAt(selectedRow, 3).toString());
                    }
                }
            }
        });



    }
    private void populateItemsTable() {
        // Create an instance of ItemsDAO
        ItemsDAO itemsDAO = new ItemsDAO();

        // Fetch all items from the database
        List<Items> items = itemsDAO.getAllItems();

        // Create a table model to display the items
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Item ID");
        model.addColumn("Item Name");
        model.addColumn("Price");
        model.addColumn("Food Description");

        // Populate the table model with the items
        for (Items item : items) {
            model.addRow(new Object[]{
                    item.getItemID(),
                    item.getItemName(),
                    item.getPrice(),
                    item.getFoodDescription()
            });
        }

        itemsTable.setModel(model);
    }

    private void updateItem() {
        int selectedRow = itemsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select an item to update.");
            return;
        }

        int itemID = Integer.parseInt(itemsTable.getValueAt(selectedRow, 0).toString());
        String itemName = itemNameTextField.getText();
        double itemPrice = Double.parseDouble(itemPriceTextField.getText());
        String foodDescription = foodDescriptionTextArea.getText();

        Items itemToUpdate = new Items(itemID, itemName, itemPrice, foodDescription);

        ItemsDAO itemsDAO = new ItemsDAO();
        boolean success = itemsDAO.updateItem(itemToUpdate);

        if (success) {
            JOptionPane.showMessageDialog(null, "Item updated successfully.");
            populateItemsTable();
            clearTextFields();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to update item.");
        }
    }
    private void createItem() {
        String itemName = itemNameTextField.getText();
        double itemPrice;
        try {
            itemPrice = Double.parseDouble(itemPriceTextField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid price.");
            return;
        }
        String foodDescription = foodDescriptionTextArea.getText();

        ItemsDAO itemsDAO = new ItemsDAO();
        boolean success = itemsDAO.createItem(itemName, itemPrice, foodDescription);

        if (success) {
            JOptionPane.showMessageDialog(null, "Item created successfully.");
            populateItemsTable();
            clearTextFields();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to create item.");
        }
    }
    private void deleteSelectedItem() {
        int selectedRow = itemsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select an item to delete.");
            return;
        }

        int itemID = Integer.parseInt(itemsTable.getValueAt(selectedRow, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete this item?", "Delete Item",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            ItemsDAO itemsDAO = new ItemsDAO();
            boolean success = itemsDAO.deleteItem(itemID);

            if (success) {
                JOptionPane.showMessageDialog(null, "Item deleted successfully.");
                populateItemsTable();
                clearTextFields();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete item.");
            }
        }
    }
    public JPanel getRestaurantPanel() {
        return restarauntPanel;
    }

    private void clearTextFields() {
        itemNameTextField.setText("");
        itemPriceTextField.setText("");
        foodDescriptionTextArea.setText("");
    }

    private boolean updateIsCompletedStatus(int orderID) {
        OrdersDAO ordersDAO = new OrdersDAO();
        boolean updated = ordersDAO.markOrderAsComplete(orderID);
        return updated;
    }
}

