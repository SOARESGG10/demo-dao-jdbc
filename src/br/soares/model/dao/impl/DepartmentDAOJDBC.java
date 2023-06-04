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

    }

    @Override
    public void update(Department obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller.*, department.Name as DeptName " +
                    "FROM seller " +
                    "JOIN department ON seller.DepartmentId = department.Id " +
                    "WHERE department.Id = ?"
            );

            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                Department department = new Department();
                department.setId(resultSet.getInt("Id"));
                department.setName(resultSet.getString("Name"));
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

            resultSet = statement.executeQuery("SELECT * FROM department");

            while(resultSet.next()) {
                Department department = new Department();
                department.setId(resultSet.getInt("Id"));
                department.setName(resultSet.getString("Name"));
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
