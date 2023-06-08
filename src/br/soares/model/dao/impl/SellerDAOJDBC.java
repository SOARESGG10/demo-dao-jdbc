package br.soares.model.dao.impl;

import br.soares.database.Database;
import br.soares.database.exceptions.DatabaseException;
import br.soares.model.dao.SellerDAO;
import br.soares.model.entities.Department;
import br.soares.model.entities.Seller;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDAOJDBC implements SellerDAO {

    private final Connection connection;

    public SellerDAOJDBC(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void insert(Seller obj) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO seller" +
                            "(name, email, birth_date, salary, department_id) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, obj.getName());
            preparedStatement.setString(2, obj.getEmail());
            preparedStatement.setDate(3, new Date(obj.getBirthDate().getTime()));
            preparedStatement.setDouble(4, obj.getSalary());
            preparedStatement.setInt(5, obj.getDepartment().getId());

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
    public void update(Seller obj) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE seller " +
                            "SET name = ?, email = ?, birth_date = ?, salary = ?, department_id = ? " +
                            "WHERE id = ?",
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, obj.getName());
            preparedStatement.setString(2, obj.getEmail());
            preparedStatement.setDate(3, new Date(obj.getBirthDate().getTime()));
            preparedStatement.setDouble(4, obj.getSalary());
            preparedStatement.setInt(5, obj.getDepartment().getId());
            preparedStatement.setInt(6, obj.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                System.out.println("Done! Rows affected " + rowsAffected);
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
                    "DELETE FROM seller WHERE id = ?",
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0) {
                System.out.println("Done! Rows affected " + rowsAffected);
                Database.closeStatement(preparedStatement);
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
    public Seller findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller.*, department.name as department_name " +
                            "FROM seller JOIN department " +
                            "ON seller.department_id = department.id " +
                            "WHERE seller.Id = ?"
            );

            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                Department department = instantiateDepartment(resultSet);
                return instantiateSeller(resultSet, department);
            }

            return null;
        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        } finally {
            Database.closeStatement(preparedStatement);
            Database.closeResultSet(resultSet);
        }
    }

    private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(resultSet.getInt("id"));
        seller.setName(resultSet.getString("name"));
        seller.setEmail(resultSet.getString("email"));
        seller.setBirthDate(resultSet.getDate("birth_date"));
        seller.setSalary(resultSet.getDouble("salary"));
        seller.setDepartment(department);
        return seller;
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setId(resultSet.getInt("department_id"));
        department.setName(resultSet.getString("department_name"));
        return department;
    }

    @Override
    public List<Seller> findAll() {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(
                    "SELECT seller.*, department.name as department_name " +
                    "FROM seller JOIN department " +
                    "ON seller.department_id = department.id"
            );

            return associateSellerToDepartment(resultSet);
        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        } finally {
            Database.closeStatement(statement);
            Database.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller.*, department.name as department_name " +
                            "FROM seller JOIN department " +
                            "ON seller.department_id = department.id " +
                            "WHERE department_id = ?"
            );

            preparedStatement.setInt(1, department.getId());
            resultSet = preparedStatement.executeQuery();

            return associateSellerToDepartment(resultSet);

        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        } finally {
            Database.closeStatement(preparedStatement);
            Database.closeResultSet(resultSet);
        }
    }

    private List<Seller> associateSellerToDepartment(ResultSet resultSet) throws SQLException {
        List<Seller> sellers = new ArrayList<>();
        Map<Integer, Department> departmentMap = new HashMap<>();

        while(resultSet.next()) {

            Department auxDepartment = departmentMap.get(resultSet.getInt("department_id"));

            if(auxDepartment == null) {
                auxDepartment = instantiateDepartment(resultSet);
                departmentMap.put(resultSet.getInt("department_id"), auxDepartment);
            }
            Seller instantiateSeller = instantiateSeller(resultSet, auxDepartment);
            sellers.add(instantiateSeller);
        }

        return sellers;
    }
}
