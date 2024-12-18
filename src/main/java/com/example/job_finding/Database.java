package com.example.job_finding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {


        // Database credentials
        private static final String URL = "jdbc:mysql://127.0.0.1:3306/signup_new";
        private static final String USERNAME = "root";
        private static final String PASSWORD = "Babarmeye123sql";

        // Method to establish a connection
        public static Connection getConnection() {
            Connection connection = null;
            try {
                // Register MySQL driver (optional with newer Java versions)
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establish the connection
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connection to MySQL database established successfully!");
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found. Include the library in your project.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("Failed to connect to the database. Check your URL, username, or password.");
                e.printStackTrace();
            }
            return connection;
        }

        // Main method for testing
        public static void main(String[] args) {
            Connection connection = getConnection();
            if (connection != null) {
                try {
                    // Close the connection after testing
                    connection.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    System.err.println("Failed to close the connection.");
                    e.printStackTrace();
                }
            }
        }
    }


