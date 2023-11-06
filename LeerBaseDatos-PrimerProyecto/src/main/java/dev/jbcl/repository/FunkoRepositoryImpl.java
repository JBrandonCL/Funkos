package dev.jbcl.repository;

import dev.jbcl.Excepcion.FunkoDontFound;
import dev.jbcl.Excepcion.FunkoDontSave;

import dev.jbcl.Model.Funkos;
import dev.jbcl.Model.Modelo;
import dev.jbcl.Service.BBDD.DatabaseManager;
import dev.jbcl.Service.Backup.Backup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FunkoRepositoryImpl implements FunkoRepository {

    private Backup<Funkos> backup;

    private Logger logger = LoggerFactory.getLogger(FunkoRepositoryImpl.class);
    private static FunkoRepositoryImpl instance;

    private final DatabaseManager databaseManager;

    private FunkoRepositoryImpl(DatabaseManager databaseManager ,Backup backup) {
        this.databaseManager = databaseManager;
        this.backup = backup;
    }

    public static FunkoRepositoryImpl getInstance(DatabaseManager databaseManager,Backup backup) {
        if (instance == null) {
            instance = new FunkoRepositoryImpl(databaseManager, backup);
        }
        return instance;
    }

    @Override
    public Funkos save(Funkos funkos)  {
        logger.info("añadiendo un funko");
        String Query = "INSERT INTO Funko (uuid, name, modelo, precio, fecha_lanzamiento) VALUES (?, ?, ?, ?, ?)";
        try (
                var conection = databaseManager.getConnection();
                var statement = conection.prepareStatement(Query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setObject(1, funkos.getCod());
            statement.setString(2, funkos.getNombre());
            statement.setString(3, funkos.getModelo().toString());
            statement.setDouble(4, funkos.getPrecio());
            statement.setObject(5, funkos.getFecha());
            funkos.setCreated_at(LocalDate.now());
            funkos.setUpdated_at(LocalDate.now());
            var res = statement.executeUpdate();
            if (res > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                while (rs.next()) {
                    funkos.setId(rs.getInt(1));
                }
                rs.close();
            } else {
                logger.error("Funko no guardado");
                throw new SQLException("Funko no guardado");
            }

            return funkos;
        } catch (SQLException e) {
            logger.error("Error al añadir funko");
            throw new FunkoDontSave("Error al guardar el funko "+funkos.getNombre());
        }
    }

    @Override
    public Funkos update(Funkos funkos)  {
        logger.info("actualizando un funko");
        String Query = "UPDATE Funko SET name =?, modelo =?, precio =?, created_at =? WHERE id =?";
        try (var conection = databaseManager.getConnection();
            var statement = conection.prepareStatement(Query)) {

            statement.setString(1, funkos.getNombre());
            statement.setString(2, funkos.getModelo().toString());
            statement.setDouble(3, funkos.getPrecio());
            statement.setObject(4, LocalDateTime.now());
            statement.setInt(5, funkos.getId());

            var res = statement.executeUpdate();

            if (res > 0) {
                logger.info("Funko actualizado");
            } else {
                logger.error("Funko no actualizado");
                throw new SQLException("Funko no actualizado");
            }
        } catch (SQLException e) {
            logger.error("Errror al actualizar funko");
            throw new FunkoDontSave("Error al actualizar el funko "+funkos.getNombre());
        }

        return funkos;
    }

    @Override
    public boolean deleteById(Integer id)  {
        logger.info("eliminando un funko");
        String Query = "DELETE FROM Funko WHERE id =?";
        try (var conection = databaseManager.getConnection();
            var statement = conection.prepareStatement(Query)) {
            statement.setInt(1, id);
            var res = statement.executeUpdate();
            if (res > 0) {
                logger.info("Funko eliminado");
                return true;
            }else{
                throw new SQLException("Funko con ID " + id + " no encontrado en la BD");
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar funko");
            throw new FunkoDontFound("Error al eliminar el funko "+id);
        }
    }

    @Override
    public void deleteAll()  {
        logger.info("eliminando todos los funkos");
        String Query = "DELETE FROM Funko";
        try (var conection = databaseManager.getConnection();
            var statement = conection.prepareStatement(Query)) {
             statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error al eliminar todos los funkos");
            throw new FunkoDontFound("Error al eliminar todos los funkos");
        }
    }

//    @Override
//    public boolean backup(String file) throws SQLException {
//        return false;
//    }

    @Override
    public List<Funkos> findAll() {
        logger.info("obteniendo todos los funkos");
        List<Funkos> funkos = new ArrayList<>();
        String Query = "SELECT * FROM Funko";
        try (var conection = databaseManager.getConnection();
            var statement = conection.prepareStatement(Query)) {
            var res = statement.executeQuery();
            while (res.next()) {
                Funkos funko = Funkos.builder()
                        .id(res.getInt("id"))
                        .cod(UUID.fromString(res.getString("uuid")))
                        .nombre(res.getString("name"))
                        .modelo(Modelo.valueOf(res.getString("modelo")))
                        .precio(res.getDouble("precio"))
                        .fecha(res.getDate("fecha_lanzamiento").toLocalDate())
                        .build();
                funkos.add(funko);
            }

        } catch (SQLException e) {
            logger.error("Error al obtener todos los funkos");
            throw new FunkoDontFound("Error al obtener todos los funkos");
        }

        return funkos;
    }

    @Override
    public List<Funkos> findByName(String nombre)  {
        logger.info("Buscando Funkos por nombre");
        List<Funkos> funkos = new ArrayList<>();
        String Query = "SELECT * FROM Funko WHERE name =?";
        try (var conection = databaseManager.getConnection();
            var statement = conection.prepareStatement(Query)) {
            statement.setString(1, nombre);
            var res = statement.executeQuery();
            while (res.next()) {
                Funkos funko = Funkos.builder()
                        .id(res.getInt("id"))
                        .cod(UUID.fromString(res.getString("uuid")))
                        .nombre(res.getString("name"))
                        .modelo(Modelo.valueOf(res.getString("modelo")))
                        .precio(res.getDouble("precio"))
                        .fecha(res.getDate("fecha_lanzamiento").toLocalDate())
                        .build();
                funkos.add(funko);
            }
        } catch (SQLException e) {
            logger.error("Error al buscar funko por nombre");   
            throw new FunkoDontFound("Error al buscar funko por nombre");
        }

        return funkos;
    }

    @Override
    public Optional<Funkos> findById(Integer id) {
        logger.info("Buscando Funkos por ID");
        String Query = "SELECT * FROM Funko WHERE id =?";
        try (var conection = databaseManager.getConnection();
            var statement = conection.prepareStatement(Query)) {
            statement.setInt(1, id);
            var res = statement.executeQuery();
            if (res.next()) {
                Funkos funko = Funkos.builder()
                        .id(res.getInt("id"))
                        .cod(UUID.fromString(res.getString("uuid")))
                        .nombre(res.getString("name"))
                        .modelo(Modelo.valueOf(res.getString("modelo")))
                        .precio(res.getDouble("precio"))
                        .fecha(res.getDate("fecha_lanzamiento").toLocalDate())
                        .build();

                return Optional.of(funko);
            }

        } catch (SQLException e) {
            logger.error("Error al buscar funko por ID");
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public void backup() throws SQLException, IOException {
        logger.debug("Realizando backup de todos los funkos");
        backup.backup(this.findAll());
    }

    public void restore() throws SQLException, IOException {
        logger.debug("Reseteando backup y borrando todos los funkos de la base de datos");
        var personas = backup.restore();
        this.deleteAll();

    }

}
