package com.group6.datawarehouse.service;

import com.group6.datawarehouse.dao.ConnectControl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControlService {
    public static void insertLog(int configID, String status, String description) {
        try (Connection connection = new ConnectControl().connectToMySQL()) {
            // Truy vấn SQL để chèn dữ liệu vào bảng log
            String insertQuery = "INSERT INTO log (config_id, status, description) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                // Thêm các giá trị vào truy vấn
                preparedStatement.setInt(1, configID); // Thay thế bằng giá trị thực tế
                preparedStatement.setString(2, status);
                preparedStatement.setString(3, description);

                // Thực hiện truy vấn chèn dữ liệu
                preparedStatement.executeUpdate();

                System.out.println("Log entry inserted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean checkLogExists(int configId, String status) {
        try (Connection connection = new ConnectControl().connectToMySQL()) {
            // Truy vấn SQL để kiểm tra sự tồn tại của bản ghi trong bảng log
            String selectQuery = "SELECT COUNT(*) AS count FROM log WHERE config_id = ? AND status = ? AND DATE(time) = CURRENT_DATE()";

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                // Thêm các giá trị vào truy vấn
                preparedStatement.setInt(1, configId);
                preparedStatement.setString(2, status);

                // Thực hiện truy vấn kiểm tra sự tồn tại
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt("count");
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
