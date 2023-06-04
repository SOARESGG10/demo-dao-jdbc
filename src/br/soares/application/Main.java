package br.soares.application;

import br.soares.model.dao.DAOFactory;
import br.soares.model.dao.DepartmentDAO;
import br.soares.model.dao.SellerDAO;
import br.soares.model.entities.Seller;

import java.util.List;


public class Main {

    public static void main(String[] args) {
       SellerDAO sellerDAO = DAOFactory.createSellerDAO();
       DepartmentDAO departmentDAO = DAOFactory.createDepartmentDAO();

       List<Seller> sellersDepartment = sellerDAO.findAll();
       List<Seller> sellers = sellerDAO.findAll();

       sellersDepartment.forEach(System.out::println);
    }


}
