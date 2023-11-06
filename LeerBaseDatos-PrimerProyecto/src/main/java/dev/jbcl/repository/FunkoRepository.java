package dev.jbcl.repository;

import dev.jbcl.Model.Funkos;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface FunkoRepository extends CRUDrepository<Funkos,Integer> {
    List<Funkos> findAll();
    List<Funkos> findByName(String funko) throws SQLException;

    Optional<Funkos> findById(Integer id);

    void backup() throws  SQLException, IOException;

    void restore() throws SQLException, IOException;

}
