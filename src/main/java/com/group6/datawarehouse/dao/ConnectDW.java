package com.group6.datawarehouse.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectDW implements IConnect {
    private static final Properties properties = new Properties();
    private String url, userName, password;
    private Connection connection;

    public ConnectDW() {
        {
            String resourceName = "database.properties"; // could also be a constant
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try  (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
                properties.load(resourceStream);
                this.setUrl(properties.getProperty("urlDW"));
                this.setUserName(properties.getProperty("usernameDW"));
                this.setPassword(properties.getProperty("passwordDW"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Connection connectToMySQL() {
        connection = null;

        try {
            // Đăng ký Driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Tạo kết nối
            connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Đã đã kết nốt đến DW");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đã đóng kết nối đến DW.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public static void main(String[] args) {
        ConnectDW connectDW = new ConnectDW();
        System.out.println(connectDW.connectToMySQL());
        System.out.println(connectDW.url + connectDW.userName + connectDW.password);
        connectDW.closeConnection();
    }
}
