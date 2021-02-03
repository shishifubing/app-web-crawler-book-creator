package com.borinskikh.book_creator;

import java.sql.*;

public class Database {
    public static void main(String[] args) {

        ResultSet resultSet = null;
        String connectionUrl = "jdbc:sqlserver://localhost;user=sa;password=Pa$$word;";

        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {
            // Create and execute a SELECT SQL statement.
            String query = "SELECT id, title, author FROM books";
            resultSet = statement.executeQuery(query);

            // Print results from select statement
            while (resultSet.next()) {
                System.out.println(
                        resultSet.getString(1) + " / " + resultSet.getString(2) + " / " + resultSet.getString(3));
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
