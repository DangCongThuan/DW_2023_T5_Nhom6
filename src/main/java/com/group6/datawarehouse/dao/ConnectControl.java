package com.group6.datawarehouse.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectControl extends ConnectDB {
    private static final Properties properties = new Properties();

    public ConnectControl() {
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

    public static void main(String[] args) {
        ConnectControl connectControl = new ConnectControl();
        System.out.println(connectControl.getUrl() + connectControl.getUserName() + connectControl.getPassword());
        System.out.println(connectControl.connectToMySQL());
    }
}
