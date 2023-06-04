package br.soares.model.dao;

import br.soares.database.Database;
import br.soares.model.dao.impl.DepartmentDAOJDBC;
import br.soares.model.dao.impl.SellerDAOJDBC;

public abstract class DAOFactory {

    public static SellerDAO createSellerDAO() {
        return new SellerDAOJDBC(Database.getConnection());
    }
    public static DepartmentDAO createDepartmentDAO() { return new DepartmentDAOJDBC(Database.getConnection()); }

}
