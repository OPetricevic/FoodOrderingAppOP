package com.omarpetricevic.example.model;

public class Items {
    private int itemID;
    private String itemName;
    private double price;
    private String foodDescription;

    public Items() {
    }

    public Items(int itemID, String itemName, double price, String foodDescription) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.price = price;
        this.foodDescription = foodDescription;
    }

    // Getter for itemID
    public int getItemID() {
        return itemID;
    }

    // Setter for itemID
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    // Getter for itemName
    public String getItemName() {
        return itemName;
    }

    // Setter for itemName
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    // Getter for price
    public double getPrice() {
        return price;
    }

    // Setter for price
    public void setPrice(double price) {
        this.price = price;
    }

    // Getter for foodDescription
    public String getFoodDescription() {
        return foodDescription;
    }

    // Setter for foodDescription
    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

}
