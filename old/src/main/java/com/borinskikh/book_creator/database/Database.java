package com.borinskikh.book_creator.database;

import java.sql.*;

public class Database {
    public static void main(String[] args) {

        ResultSet resultSet = null;
        String connectionUrl = "jdbc:sqlserver://localhost;user=sa;password=Pa$$word;";

        for (int i = 3; i < 15; i++) {
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                    Statement statement = connection.createStatement();) {
                String query = "use books alter table chapters add chapter_" + i + " text null";
                System.out.println("" + i);
                String query2 = "alter table chapters add chapter_9 text null";
                resultSet = statement.executeQuery(query);

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
}
