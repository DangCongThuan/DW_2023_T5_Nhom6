package com.group6.datawarehouse.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectDW extends ConnectDB {
    private static final Properties properties = new Properties();
    public ConnectDW() {
        // Cau hinh properties
        String resourceName = "database.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try  (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            // Load cau hinh tu properties
            this.properties.load(resourceStream);
            this.setUrl(properties.getProperty("urlDW"));
            this.setUserName(properties.getProperty("usernameDW"));
            this.setPassword(properties.getProperty("passwordDW"));
        } catch (IOException e) {
            System.err.println("Error config properties to the database: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        ConnectDW connectDW = new ConnectDW();
        System.out.println(connectDW.connectToMySQL());
        System.out.println(connectDW.getUrl() + connectDW.getUserName() + connectDW.getPassword());
    }
}
