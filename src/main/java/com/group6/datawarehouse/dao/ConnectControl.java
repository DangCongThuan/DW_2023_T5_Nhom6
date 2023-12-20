package com.group6.datawarehouse.dao;

import com.group6.datawarehouse.service.DWIntoDM;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectControl implements IConnect {
    private static final Properties properties = new Properties();
    private String url, userName, password;
    private Connection connection;

    public ConnectControl() { // step 1 : load initialization from file database.properties get (severName,userID,password portNumber,dbName)
        {
            // Cau hinh properties
            String resourceName = "database.properties";
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try  (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
                // Load cau hinh tu properties
                properties.load(resourceStream);
                this.setUrl(properties.getProperty("urlControl"));
                this.setUserName(properties.getProperty("usernameControl"));
                this.setPassword(properties.getProperty("passwordControl"));
            } catch (IOException e) {
                System.err.println("Error config properties to the database: " + e.getMessage());
            }
        }
    }

    // Tao ket noi den database
    @Override
    public Connection connectToMySQL() {  // step 2 : 2.Connect DB control
        connection = null;

        try {
            // Đăng ký Driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Tạo kết nối
            connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Đã kết nối đến Control.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error connecting to the database Control: " + e.getMessage());
            DWIntoDM dwIntoDM=new DWIntoDM();
            // step 3 :create or insert File log log_yyyy_mm_dd.txt
            dwIntoDM.createLogFile(0, "Không kết nối được đến database Control");
        }
        return connection;
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đã đóng kết nối đến Control.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing to the database Control: " + e.getMessage());
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

    public static void main(String[] args) {
        ConnectControl connectControl = new ConnectControl();
        System.out.println(connectControl.url + connectControl.userName + connectControl.password);
        System.out.println(connectControl.connectToMySQL());
    }
}
