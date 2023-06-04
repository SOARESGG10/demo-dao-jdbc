package br.soares.database;

import br.soares.database.exceptions.DatabaseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Database {
    private static Connection connection = null;
    public static Connection getConnection() {

        try {
            if (connection == null) {
                Properties properties = loadProperties();
                String url = properties.getProperty("dburl");
                connection = DriverManager.getConnection(url, properties);
            }
            return connection;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
    private static Properties loadProperties() {
        Properties properties = new Properties();
        try(FileInputStream fileInputStream = new FileInputStream("database.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new DatabaseException(e.getMessage());
        }
        return properties;
    }

    public static void closeStatement(Statement statement) {
        if(statement != null) {
            try {
                statement.close();
            }
            catch (SQLException e) {
                throw new DatabaseException(e.getMessage());
            }
        }
    }
    public static void closeResultSet(ResultSet resultSet) {
        if(resultSet != null) {
            try {
                resultSet.close();
            }
            catch (SQLException e) {
                throw new DatabaseException(e.getMessage());
            }
        }
    }

}
