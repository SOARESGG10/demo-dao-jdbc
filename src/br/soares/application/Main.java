package br.soares.application;

import br.soares.model.dao.DAOFactory;
import br.soares.model.dao.DepartmentDAO;
import br.soares.model.dao.SellerDAO;
import br.soares.model.entities.Department;

import java.text.ParseException;


public class Main {

    public static void main(String[] args) throws ParseException {
       SellerDAO sellerDAO = DAOFactory.createSellerDAO();
       DepartmentDAO departmentDAO = DAOFactory.createDepartmentDAO();

       Department getDepartment = departmentDAO.findById(3);
       getDepartment.setName("Fashion");
       departmentDAO.update(getDepartment);
       System.out.println(getDepartment.getName());

    }
}
