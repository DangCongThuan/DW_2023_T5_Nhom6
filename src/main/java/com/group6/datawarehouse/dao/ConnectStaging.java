package com.group6.datawarehouse.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectStaging extends ConnectDB {
    private static final Properties properties = new Properties();
    public ConnectStaging() {
        {
            // Cau hinh properties
            String resourceName = "database.properties";
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try  (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
                // Load cau hinh tu properties
                properties.load(resourceStream);
                this.setUrl(properties.getProperty("urlStaging"));
                this.setUserName(properties.getProperty("usernameStaging"));
                this.setPassword(properties.getProperty("passwordStaging"));
            } catch (IOException e) {
                System.err.println("Error config properties to the database: " + e.getMessage());
            }
        }
    }


    public static void main(String[] args) {
        ConnectStaging connectStaging=new ConnectStaging();
        System.out.println(connectStaging.getUrl() +connectStaging.getUserName()+connectStaging.getPassword());
        System.out.println(connectStaging.connectToMySQL());
    }
}