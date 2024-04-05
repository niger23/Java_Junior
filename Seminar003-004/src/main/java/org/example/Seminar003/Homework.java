package org.example.Seminar003;

import java.sql.*;

public class Homework {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "admin")) {
            acceptConnection(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void acceptConnection (Connection connection) throws SQLException {
        createTable(connection);
        insertData(connection);
        readResult(connection);
        System.out.println("Удаляем пользователя с ID = 3");
        deleteRandomRow(connection, 3);
        System.out.println("Переименовываем пользователя с ID = 1");
        updateRow(connection, 1, "Unnamed");
        readResult(connection);


    }

    private static void readResult(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select id, firstName, secondName, age from Student");

            while (resultSet.next()) {
                System.out.println("id = " + resultSet.getInt("id") +
                        ", first name = " + resultSet.getString("firstName") +
                        ", second name = " + resultSet.getString("secondName") +
                        ", age = " + resultSet.getInt("age"));
            }

        }
    }

    private static void deleteRandomRow(Connection connection, int personDelete) throws SQLException {
        connection.createStatement().execute("delete from Student where id = " + personDelete);
    }

    private static void updateRow(Connection connection, int id, String firstName) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("update Student set firstName = ?, secondName = ? where id = ?")) {
            stmt.setInt(3, id);
            stmt.setString(1, firstName);
            stmt.setString(2, firstName);

            stmt.executeUpdate();
        }
    }

    private static void insertData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    insert into Student(id, firstName, secondName, age) values
                    (1, 'Igor', 'Petrov', 30),
                    (2, 'Semen', 'Ivanov', 31),
                    (3, 'Petr', 'Smith', 32),
                    (4, 'Ivan', 'Swetliy', 33),
                    (5, 'Nick', 'Semenov', 34),
                    (6, 'Till', 'Schweiger', 36)
                    """);
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("drop database test;");
            statement.execute("create database test;");
            statement.execute("use test;");
            statement.execute("""
                create table Student (
                    id bigint,
                    firstName varchar(256),
                    secondName varchar(256),
                    age int)
                """);
        }
    }
}
