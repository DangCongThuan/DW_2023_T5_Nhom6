package com.group6.datawarehouse.service;

import com.group6.datawarehouse.dao.ConnectControl;
import com.group6.datawarehouse.dao.ConnectDW;
import com.group6.datawarehouse.dao.ConnectStaging;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StagingToDW {

    public static void main(String[] args) throws SQLException {

        if (ControlService.checkLogExists(5, "Done")) {
            System.out.println("Hôm nay đã load dữ liệu từ staging sang data warehouse");
            return;
        }
        ControlService.insertLog(5, "Prepared", "Chuẩn bị load dữ liệu từ staging vào data warehouse");
        Connection connectStaging = new ConnectStaging().connectToMySQL();
        Connection connectDW = new ConnectDW().connectToMySQL();
        connectDW.setAutoCommit(false);
        try {
            ControlService.insertLog(5, "Crawling", "Đang load dữ liệu từ staging vào data warehouse");
            // Load dữ liệu từ bảng staging vào bảng data warehouse
            loadStagingToDataWarehouse(connectStaging, connectDW);
            ControlService.insertLog(5, "Done", "Load dữ liệu từ staging vào data warehouse thành công");
        } catch (SQLException e) {
            connectDW.rollback();
            ControlService.insertLog(5, "Error", "Chuẩn bị load dữ liệu từ staging vào data warehouse");
            e.printStackTrace();
        } finally {
            connectDW.setAutoCommit(true);
            // Đóng kết nối
            closeConnection(connectStaging);
            closeConnection(connectDW);
        }
    }

    private static void loadStagingToDataWarehouse(Connection connectStaging, Connection connectDW) throws SQLException {
        String selectQuery = "SELECT * FROM tghd_staging";
        String insertQuery = "INSERT INTO exchange_rates (bank_id, date_id, to_currency, from_currency, buy_cash, buy_transfer, sell_cash, sell_transfer) VALUES (?, ?, ?,?, ?, ?, ?, ?)";

        try (PreparedStatement selectStatement = connectStaging.prepareStatement(selectQuery);
             ResultSet resultSet = selectStatement.executeQuery();
             PreparedStatement insertStatement = connectDW.prepareStatement(insertQuery)) {

            while (resultSet.next()) {
                // Lấy dữ liệu từ bảng staging
                String bankCode= resultSet.getString("bank_code");
                String date = resultSet.getString("date");
                String toCurrencySymbol = resultSet.getString("to_currency_symbol");
                String fromCurrencySymbol = resultSet.getString("from_currency_symbol");
                double buyCash = convertStringToDouble(resultSet.getString("buy_cash"));
                double buyTransfer = convertStringToDouble(resultSet.getString("buy_transfer"));
                double sellCash = convertStringToDouble(resultSet.getString("sell_cash"));
                double sellTransfer = convertStringToDouble(resultSet.getString("sell_transfer"));

                // TODO: Lấy id từ bảng dim_bank, dim_date, dim_currency sử dụng thông tin từ bảng staging
                String getBankIdQuery = "SELECT id FROM dim_bank WHERE bank_code = ?";
                String getDateIdQuery = "SELECT id FROM dim_date WHERE full_date = ?";
                String getCurrencyIdQuery = "SELECT id FROM dim_currency WHERE currency_code = ?";
                int bankId = getDimTableId(connectDW, bankCode, getBankIdQuery);
                int dateId = getDimTableId(connectDW, date, getDateIdQuery);
                int toCurrencyId = getDimTableId(connectDW, toCurrencySymbol, getCurrencyIdQuery);
                int fromCurrencyId = getDimTableId(connectDW, fromCurrencySymbol, getCurrencyIdQuery);

                // Thêm dữ liệu vào bảng data warehouse
                insertStatement.setInt(1, bankId); // Thay thế bằng id từ bảng dim_bank
                insertStatement.setInt(2, dateId); // Thay thế bằng id từ bảng dim_date
                insertStatement.setInt(3, toCurrencyId); // Thay thế bằng id từ bảng dim_currency
                insertStatement.setInt(4, fromCurrencyId); // Thay thế bằng id từ bảng dim_currency
                insertStatement.setDouble(5, buyCash);
                insertStatement.setDouble(6, buyTransfer);
                insertStatement.setDouble(7, sellCash);
                insertStatement.setDouble(8, sellTransfer);

                insertStatement.executeUpdate();
            }
        }
    }

    private static double convertStringToDouble(String numberString) {
        // Thay thế dấu phẩy bằng dấu chấm
        numberString = numberString.replace(",", "");

        // Chuyển đổi chuỗi thành kiểu double
        try {
            return Double.parseDouble(numberString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0; // hoặc giá trị mặc định khác nếu có lỗi
        }
    }

    private static int getDimTableId(Connection connection, String value, String query) throws SQLException {


        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, value);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        }

        return -1; // Trả về -1 nếu không tìm thấy bank code trong bảng
    }
    private static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
