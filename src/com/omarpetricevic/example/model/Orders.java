package com.omarpetricevic.example.model;

import java.sql.Timestamp;

public class Orders {
    private int orderID;
    private int userID;
    private int itemID;
    private String itemName;
    private Timestamp orderDate;
    private double price;
    private boolean isCompleted;

    public Orders(int orderID, int userID, int itemID, String itemName, Timestamp orderDate, double price, boolean isCompleted) {
        this.orderID = orderID;
        this.userID = userID;
        this.itemID = itemID;
        this.itemName = itemName;
        this.orderDate = orderDate;
        this.price = price;
        this.isCompleted = isCompleted;
    }

    public Orders() {
    }

    // Getters
    public int getOrderID() {
        return orderID;
    }

    public int getUserID() {
        return userID;
    }

    public int getItemID() {
        return itemID;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public double getPrice() {
        return price;
    }
    public String getItemName() {
        return itemName;
    }
    public boolean getIsCompleted() {
        return isCompleted;
    }

    // Setters
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
