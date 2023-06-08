package br.soares.model.dao.impl;

import br.soares.database.Database;
import br.soares.database.exceptions.DatabaseException;
import br.soares.model.dao.DepartmentDAO;
import br.soares.model.entities.Department;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAOJDBC implements DepartmentDAO {

    private final Connection connection;

    public DepartmentDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO department (name) " +
                            "VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, obj.getName());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()) {
                    int id = resultSet.getInt(1);
                    obj.setId(id);
                } else {
                    throw new DatabaseException("Unexpected error! No rows affected");
                }
                Database.closeResultSet(resultSet);
            }
        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        } finally {
            Database.closeStatement(preparedStatement);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement preparedStatement = null;

        try {

            preparedStatement = connection.prepareStatement(
                    "UPDATE department " +
                            "SET name = ?" +
                            "WHERE id = ?",
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, obj.getName());
            preparedStatement.setInt(2, obj.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()) {
                    int id = resultSet.getInt(1);
                    obj.setId(id);
                }
                Database.closeResultSet(resultSet);
            } else {
                throw new DatabaseException("Unexpected error! No rows affected");
            }

        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        } finally {
            Database.closeStatement(preparedStatement);
        }

    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "DELETE FROM department" +
                            "WHERE id = ?"
            );

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                System.out.println("Done! rows affected " + rowsAffected);
                Database.closeResultSet(resultSet);
            } else {
                throw new DatabaseException("Unexpected error! No rows affected");
            }
        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        } finally {
            Database.closeStatement(preparedStatement);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * " +
                    "FROM department " +
                            "WHERE id = ?"
            );

            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                Department department = new Department();
                department.setId(resultSet.getInt("id"));
                department.setName(resultSet.getString("name"));
                return department;
            }

            return null;
        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        } finally {
            Database.closeStatement(preparedStatement);
            Database.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Department> findAll() {
        Statement statement = null;
        ResultSet resultSet = null;
        List<Department> departments = new ArrayList<>();
        try {
            statement = connection.createStatement();

            resultSet = statement.executeQuery(
                    "SELECT * FROM department"
            );

            while(resultSet.next()) {
                Department department = new Department();
                department.setId(resultSet.getInt("id"));
                department.setName(resultSet.getString("name"));
                departments.add(department);
            }
            return departments;

        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
        finally {
            Database.closeStatement(statement);
            Database.closeResultSet(resultSet);
            Database.closeConnection();
        }
    }
}
