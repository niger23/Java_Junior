package org.example.Seminar003;

import java.sql.*;

public class JDBC {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {
            acceptConnection(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
    private static void acceptConnection (Connection connection) throws SQLException {
        createTable(connection);
        insertData(connection);
        deleteRandomRow(connection);
        updateRow(connection, 1, "Unnamed");

        readResult(connection);


    }

    private static void readResult(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select id, name from Person");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                System.out.println("id = " + id + ", name = " + name);
            }

        }
    }

    private static void deleteRandomRow(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            System.out.println("DELETED: " + statement.executeUpdate("delete from person where id = 2"));
        }
    }

    private static void updateRow(Connection connection, int id, String name) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("update person set name = $2 where id = $1")) {
            stmt.setInt(1, id);
            stmt.setString(2, name);

            stmt.executeUpdate();
        }
//        try (Statement statement = connection.createStatement()) {
//            statement.executeUpdate("update person set name = '" + name + "' where id = " + id);
//        }
    }

    private static void insertData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    insert into Person(id, name) values
                    (1, 'Igor'),
                    (2, 'Petr'),
                    (3, 'Mary'),
                    (4, 'Alex'),
                    (5, 'John')
                    """);
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                create table Person (
                    id bigint,
                    name varchar(256)
                )
                
                """);
        }
    }


}
