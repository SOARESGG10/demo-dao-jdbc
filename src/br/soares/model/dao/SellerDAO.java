package br.soares.model.dao;

import br.soares.model.entities.Department;
import br.soares.model.entities.Seller;

import java.util.List;

public interface SellerDAO extends DAO<Seller> {
    List<Seller> findByDepartment(Department department);
}
