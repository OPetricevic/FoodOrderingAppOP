package com.omarpetricevic.example.model;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class UserTableModel extends AbstractTableModel {
    private final List<User> users;
    private final String[] columnNames = {"ID", "Full Name",  "Email", "Password", "Phone Number", "User Type", "Preferred Role"};

    public UserTableModel(List<User> users) {
        this.users = users;
    }

    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = users.get(rowIndex);
        switch (columnIndex) {
            case 0: return user.getId();
            case 1: return user.getFullName();
            case 2: return user.getEmail();
            case 3: return user.getPasswordHash();
            case 4: return user.getPhoneNumber();
            case 5: return user.getUserType();
            case 6: return user.getPreferredRole();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
