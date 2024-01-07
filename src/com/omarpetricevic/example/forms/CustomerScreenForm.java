package com.omarpetricevic.example.forms;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.omarpetricevic.example.dao.ItemsDAO;
import com.omarpetricevic.example.dao.OrdersDAO;
import com.omarpetricevic.example.model.Items;
import com.omarpetricevic.example.model.Orders;
import com.omarpetricevic.example.service.UserService;

import java.util.List;


public class CustomerScreenForm {

    private JPanel orderPanel;
    private JButton deleteOrder;
    private JTable ongoingOrders;
    private JTable completedOrders;
    private JButton backButton;
    private JTable itemTable;
    private JButton updateButton;
    private JTextField foodNameTextField;
    private JTextField priceTextField;
    private JTextField foodDescriptionTextField;
    private JLabel priceLabel;
    private JButton orderButton;
    private long userId;


    private int selectedOrderID;

    public CustomerScreenForm(long userId) {
        initializeForm();
        this.userId = userId;

    }

    private void initializeForm() {


        foodNameTextField.setEditable(false);
        priceTextField.setEditable(false);
        foodDescriptionTextField.setEditable(false);

        setupItemTable();
        setupListeners();
        setupOngoingOrdersTable();
        setupCompletedOrdersTable();
        itemTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && itemTable.getSelectedRow() != -1) {
                    int selectedRow = itemTable.getSelectedRow();
                    foodNameTextField.setText(itemTable.getValueAt(selectedRow, 1).toString());
                    priceTextField.setText(itemTable.getValueAt(selectedRow, 2).toString());
                    foodDescriptionTextField.setText(itemTable.getValueAt(selectedRow, 3).toString());
                }
            }
        });

        ongoingOrders.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = ongoingOrders.getSelectedRow();
                    if (selectedRow >= 0) {
                        int orderID = (Integer) ongoingOrders.getValueAt(selectedRow, 0);
                        String itemName = (String) ongoingOrders.getValueAt(selectedRow, 1);
                        Double price = (Double) ongoingOrders.getValueAt(selectedRow, 3);

                        foodNameTextField.setText(itemName);
                        priceTextField.setText(price.toString());

                        selectedOrderID = orderID;
                    }
                }
            }
        });

    }

    private void setupItemTable() {
        ItemsDAO itemsDAO = new ItemsDAO();
        List<Items> itemsList = itemsDAO.getAllItems();

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Item ID", "Item Name", "Price", "Food Description"});

        for (Items item : itemsList) {
            model.addRow(new Object[]{
                    item.getItemID(),
                    item.getItemName(),
                    item.getPrice(),
                    item.getFoodDescription()
            });
        }

        itemTable.setModel(model);
    }
    private void setupOngoingOrdersTable() {
        OrdersDAO ordersDAO = new OrdersDAO();
        List<Orders> ongoingOrdersList = ordersDAO.getOngoingOrdersByUserId((int) userId);

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: // Order ID
                        return Integer.class;
                    case 1: // Item Name
                        return String.class;
                    case 2: // Order Date
                        return String.class;
                    case 3: // Price
                        return Double.class;
                    default:
                        return Object.class;
                }
            }
        };

        model.setColumnIdentifiers(new String[]{"Order ID", "Item Name", "Order Date", "Price"});

        for (Orders order : ongoingOrdersList) {
            model.addRow(new Object[]{
                    order.getOrderID(),
                    order.getItemName(),
                    order.getOrderDate().toString(),
                    order.getPrice()
            });
        }

        ongoingOrders.setModel(model);
    }
    private void setupCompletedOrdersTable(){
        OrdersDAO ordersDAO = new OrdersDAO();
        List<Orders> completedOrdersList = ordersDAO.getCompletedOrdersByUserId((int) userId);

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Order ID", "Item Name", "Order Date", "Price"});

        for (Orders order : completedOrdersList) {
            model.addRow(new Object[]{
                    order.getOrderID(),
                    order.getItemName(),
                    order.getOrderDate().toString(),
                    order.getPrice()
            });
        }

        completedOrders.setModel(model);
    }
    private void setupListeners() {
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int selectedRow = itemTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int itemID = Integer.parseInt(itemTable.getValueAt(selectedRow, 0).toString());
                        String itemName = foodNameTextField.getText();
                        double price = Double.parseDouble(priceTextField.getText());
                        String foodDescription = foodDescriptionTextField.getText();

                        Orders newOrder = new Orders();
                        newOrder.setUserID((int) userId);
                        newOrder.setItemID(itemID);
                        newOrder.setOrderDate(new java.sql.Timestamp(System.currentTimeMillis()));
                        newOrder.setPrice(price);
                        newOrder.setIsCompleted(false);

                        OrdersDAO ordersDAO = new OrdersDAO();
                        boolean success = ordersDAO.createOrder(newOrder);

                        if (success) {
                            String receiptMessage = String.format("Receipt:\nItem: %s\nPrice: %.2f\nDescription: %s\nOrder placed on: %s",
                                    itemName, price, foodDescription, newOrder.getOrderDate().toString());
                            JOptionPane.showMessageDialog(null, receiptMessage);
                            setupOngoingOrdersTable(); // Refresh the ongoing orders table
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to place order.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select an item to order.");
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Please enter valid item information.");
                }
            }
        });

        deleteOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedOrderID == -1) {
                    JOptionPane.showMessageDialog(null, "Please select an order to delete.");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this order?", "Delete Order", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    OrdersDAO ordersDAO = new OrdersDAO();
                    boolean success = ordersDAO.deleteOrder(selectedOrderID);

                    if (success) {
                        JOptionPane.showMessageDialog(null, "Order deleted successfully.");
                        setupOngoingOrdersTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete the order.");
                    }
                }
            }
        });


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedOrderID == -1) {
                    JOptionPane.showMessageDialog(null, "Please select an order to update.");
                    return;
                }

                String updatedItemName = foodNameTextField.getText();
                double updatedPrice;
                try {
                    updatedPrice = Double.parseDouble(priceTextField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid price.");
                    return;
                }
                String updatedDescription = foodDescriptionTextField.getText();

                int itemID = new ItemsDAO().findItemIDByName(updatedItemName);
                if (itemID == -1) {
                    JOptionPane.showMessageDialog(null, "The specified item does not exist.");
                    return;
                }

                Orders updatedOrder = new Orders(selectedOrderID, (int) userId, itemID, updatedItemName, new java.sql.Timestamp(System.currentTimeMillis()), updatedPrice, false);

                boolean success = new OrdersDAO().updateOrder(updatedOrder);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Order updated successfully.");
                    setupOngoingOrdersTable(); // Refresh the ongoing orders table
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update the order.");
                }
            }
        });


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Dispose of the current screen
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(orderPanel);
                if (currentFrame != null) {
                    currentFrame.dispose();
                }

                // Open the LoginScreenForm
                UserService userService = new UserService();
                LoginScreenForm loginForm = new LoginScreenForm(userService);
                JFrame loginFrame = new JFrame("Login");
                loginFrame.setContentPane(loginForm.getMainPanel());
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.pack();
                loginFrame.setLocationRelativeTo(null);
                loginFrame.setVisible(true);
            }
        });

    }


    public JPanel getOrderPanel() {
        return orderPanel;
    }
}
