package com.omarpetricevic.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.omarpetricevic.example.model.Items;
import com.omarpetricevic.example.util.DatabaseUtil;

public class ItemsDAO {
    public List<Items> getAllItems() {
        List<Items> itemList = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Items");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Items item = new Items();
                item.setItemID(resultSet.getInt("ItemID"));
                item.setItemName(resultSet.getString("ItemName"));
                item.setPrice(resultSet.getDouble("Price"));
                item.setFoodDescription(resultSet.getString("FoodDescription"));

                itemList.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itemList;
    }
    public boolean updateItem(Items item) {
        String sql = "UPDATE Items SET ItemName = ?, Price = ?, FoodDescription = ? WHERE ItemID = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, item.getItemName());
            preparedStatement.setDouble(2, item.getPrice());
            preparedStatement.setString(3, item.getFoodDescription());
            preparedStatement.setInt(4, item.getItemID());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean createItem(String itemName, double price, String foodDescription) {
        String sql = "INSERT INTO Items (ItemName, Price, FoodDescription) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, itemName);
            preparedStatement.setDouble(2, price);
            preparedStatement.setString(3, foodDescription);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteItem(int itemID) {
        String sql = "DELETE FROM Items WHERE ItemID = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, itemID);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public int findItemIDByName(String itemName) {
        String sql = "SELECT ItemID FROM Items WHERE ItemName = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, itemName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("ItemID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}

