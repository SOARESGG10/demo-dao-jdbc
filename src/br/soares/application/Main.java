package br.soares.application;

import br.soares.model.entities.Department;

public class Main {

    public static void main(String[] args) {
        Department department = new Department(1, "Books");
        System.out.println(department);
    }


}
