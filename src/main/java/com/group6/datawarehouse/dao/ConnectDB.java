package com.group6.datawarehouse.dao;

import com.group6.datawarehouse.service.ControlService;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class ConnectDB {
    private String url, userName, password;
    private Connection connection;


    // Tao ket noi den database
    public Connection connectToMySQL() {
        connection = null;

        try {
            // Đăng ký Driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Tạo kết nối
            connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Đã kết nối MySQL.");
        } catch (ClassNotFoundException | SQLException e) {
            ControlService.insertLog(0, "Error", e.getMessage());
        }
        return connection;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
