package com.group6.datawarehouse.dao;

import com.group6.datawarehouse.service.DWIntoDM;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectDM implements IConnect {
    private static final Properties properties = new Properties();
    private String url, userName, password;
    private Connection connection;

    public ConnectDM() {// step 1
        {
            String resourceName = "database.properties"; // could also be a constant
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try  (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
                properties.load(resourceStream);
                this.setUrl(properties.getProperty("urlDM"));
                this.setUserName(properties.getProperty("usernameDM"));
                this.setPassword(properties.getProperty("passwordDM"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // step 8 : Connect DB DM?
    // step 9 : create  or insert File log log_yyyy_mm_dd.txt
    @Override
    public Connection connectToMySQL() {
        connection = null;

        try {
            // Đăng ký Driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // step 8 : Connect DB DM?
            connection = DriverManager.getConnection( url, userName, password);
            System.out.println("Đã kết nối đến DM");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            DWIntoDM dwIntoDM=new DWIntoDM();
            // step 9 :.insert_log?stt=Error ,update_configs?stt=Error
            dwIntoDM.insert_log(7,"Error","Không kết nối được database DM");
            dwIntoDM.update_config(7,"Error");
        }

        return connection;
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đã đóng kết nối đến DM.");
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
        ConnectDM connectDM=new ConnectDM();
        System.out.println(connectDM.url +connectDM.userName+connectDM.password);
        connectDM.connectToMySQL();
    }
}
