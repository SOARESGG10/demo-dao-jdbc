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

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller.*, department.Name as DeptName " +
                            "FROM seller JOIN department " +
                            "ON seller.DepartmentId = department.ID " +
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
        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBirthDate(resultSet.getDate("BirthDate"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setDepartment(department);
        return seller;
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("DeptName"));
        return  department;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Seller> sellers = new ArrayList<>();
        Map<Integer, Department> departmentMap = new HashMap<>();

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller.*, department.Name as DeptName " +
                            "FROM seller JOIN department " +
                            "ON seller.DepartmentId = department.ID "
            );

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {

                Department auxDepartment = departmentMap.get(resultSet.getInt("DepartmentId"));

                if(auxDepartment == null) {
                    auxDepartment = instantiateDepartment(resultSet);
                    departmentMap.put(resultSet.getInt("DepartmentId"), auxDepartment);
                }
                Seller instantiateSeller = instantiateSeller(resultSet, auxDepartment);
                sellers.add(instantiateSeller);
            }

            return sellers;
        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        } finally {
            Database.closeStatement(preparedStatement);
            Database.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Seller> sellers = new ArrayList<>();
        Map<Integer, Department> departmentMap = new HashMap<>();

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller.*, department.Name as DeptName " +
                            "FROM seller JOIN department " +
                            "ON seller.DepartmentId = department.ID " +
                            "WHERE DepartmentId = ?"
            );

            preparedStatement.setInt(1, department.getId());
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {

                Department auxDepartment = departmentMap.get(resultSet.getInt("DepartmentId"));

                if(auxDepartment == null) {
                    auxDepartment = instantiateDepartment(resultSet);
                    departmentMap.put(resultSet.getInt("DepartmentId"), auxDepartment);
                }
                Seller instantiateSeller = instantiateSeller(resultSet, auxDepartment);
                sellers.add(instantiateSeller);
            }

            return sellers;
        } catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        } finally {
            Database.closeStatement(preparedStatement);
            Database.closeResultSet(resultSet);
        }
    }
}
