package br.soares.application;

import br.soares.model.entities.Department;
import br.soares.model.entities.Seller;

import java.util.Date;


public class Main {

    public static void main(String[] args) {
        Department department = new Department(1, "Books");
        Seller seller = new Seller(1, "David", "david@gmail.com", new Date(), 5500.00, department);

        System.out.println(department);
        System.out.println(seller);
    }


}
