package com.omarpetricevic.example.dao;

import com.omarpetricevic.example.model.Orders;
import com.omarpetricevic.example.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAO {
    // NE koristim ovu done Funckiju ali u slucaju moze se dodati.
    public List<Orders> getAllOrders() {
        List<Orders> ordersList = new ArrayList<>();
        String sql = "SELECT o.*, i.ItemName FROM Orders o INNER JOIN Items i ON o.ItemID = i.ItemID";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Orders order = new Orders(
                        resultSet.getInt("OrderID"),
                        resultSet.getInt("UserID"),
                        resultSet.getInt("ItemID"),
                        resultSet.getString("ItemName"),
                        resultSet.getTimestamp("OrderDate"),
                        resultSet.getDouble("Price"),
                        resultSet.getBoolean("IsCompleted")
                );
                ordersList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordersList;
    }
    public boolean createOrder(Orders order) {
        String sql = "INSERT INTO Orders (UserID, ItemID, OrderDate, Price, IsCompleted) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, order.getUserID());
            preparedStatement.setInt(2, order.getItemID());
            preparedStatement.setTimestamp(3, order.getOrderDate());
            preparedStatement.setDouble(4, order.getPrice());
            preparedStatement.setBoolean(5, order.getIsCompleted());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Orders> getOngoingOrdersByUserId(int userId) {
        List<Orders> ordersList = new ArrayList<>();
        String sql = "SELECT o.*, i.ItemName FROM Orders o INNER JOIN Items i ON o.ItemID = i.ItemID WHERE o.UserID = ? AND o.IsCompleted = FALSE";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Orders order = new Orders(
                            resultSet.getInt("OrderID"),
                            resultSet.getInt("UserID"),
                            resultSet.getInt("ItemID"),
                            resultSet.getString("ItemName"),
                            resultSet.getTimestamp("OrderDate"),
                            resultSet.getDouble("Price"),
                            resultSet.getBoolean("IsCompleted")
                    );
                    ordersList.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordersList;
    }
    public List<Orders> getCompletedOrdersByUserId(int userId) {
        List<Orders> ordersList = new ArrayList<>();
        String sql = "SELECT o.*, i.ItemName FROM Orders o INNER JOIN Items i ON o.ItemID = i.ItemID WHERE o.UserID = ? AND o.IsCompleted = TRUE";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Orders order = new Orders(
                            resultSet.getInt("OrderID"),
                            resultSet.getInt("UserID"),
                            resultSet.getInt("ItemID"),
                            resultSet.getString("ItemName"),
                            resultSet.getTimestamp("OrderDate"),
                            resultSet.getDouble("Price"),
                            resultSet.getBoolean("IsCompleted")
                    );
                    ordersList.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordersList;
    }
    public boolean updateOrder(Orders order) {
        String sql = "UPDATE Orders SET UserID = ?, ItemID = ?, OrderDate = ?, Price = ?, IsCompleted = ? WHERE OrderID = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, order.getUserID());
            preparedStatement.setInt(2, order.getItemID());
            preparedStatement.setTimestamp(3, order.getOrderDate());
            preparedStatement.setDouble(4, order.getPrice());
            preparedStatement.setBoolean(5, order.getIsCompleted());
            preparedStatement.setInt(6, order.getOrderID());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteOrder(int orderID) {
        String sql = "DELETE FROM Orders WHERE OrderID = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, orderID);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean markOrderAsComplete(int orderID) {
        String sql = "UPDATE Orders SET IsCompleted = TRUE WHERE OrderID = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, orderID);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Orders> getAllOngoingOrders() {
        List<Orders> ordersList = new ArrayList<>();
        String sql = "SELECT o.*, i.ItemName FROM Orders o INNER JOIN Items i ON o.ItemID = i.ItemID WHERE o.IsCompleted = FALSE;";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Orders order = new Orders(
                        resultSet.getInt("OrderID"),
                        resultSet.getInt("UserID"),
                        resultSet.getInt("ItemID"),
                        resultSet.getString("ItemName"),
                        resultSet.getTimestamp("OrderDate"),
                        resultSet.getDouble("Price"),
                        resultSet.getBoolean("IsCompleted")
                );
                ordersList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordersList;
    }
    public double getSumOfAllCompletedOrders() {
        double sum = 0.0;
        String sql = "SELECT SUM(Price) as Total FROM Orders WHERE IsCompleted = TRUE;";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                sum = resultSet.getDouble("Total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sum;
    }
    public double getSumOfAllPendingOrders() {
        double sum = 0.0;
        String sql = "SELECT SUM(Price) as Total FROM Orders WHERE IsCompleted = FALSE;";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                sum = resultSet.getDouble("Total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sum;
    }
    public List<Orders> getAllPastOrders() {
        List<Orders> ordersList = new ArrayList<>();
        String sql = "SELECT o.*, i.ItemName FROM Orders o INNER JOIN Items i ON o.ItemID = i.ItemID WHERE o.IsCompleted = TRUE;";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Orders order = new Orders(
                        resultSet.getInt("OrderID"),
                        resultSet.getInt("UserID"),
                        resultSet.getInt("ItemID"),
                        resultSet.getString("ItemName"),
                        resultSet.getTimestamp("OrderDate"),
                        resultSet.getDouble("Price"),
                        resultSet.getBoolean("IsCompleted")
                );
                ordersList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordersList;
    }
}
