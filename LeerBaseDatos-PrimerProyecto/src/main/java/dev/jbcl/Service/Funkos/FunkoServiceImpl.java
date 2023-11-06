package dev.jbcl.Service.Funkos;

import dev.jbcl.Excepcion.FunkoDontSave;
import dev.jbcl.Model.Funkos;
import dev.jbcl.Model.Modelo;
import dev.jbcl.Service.BBDD.DatabaseManager;
import dev.jbcl.Service.Backup.Backup;
import dev.jbcl.Service.Backup.BackupImpl;
import dev.jbcl.Utils.Lector;
import dev.jbcl.repository.FunkoRepository;
import dev.jbcl.repository.FunkoRepositoryImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FunkoServiceImpl implements FunkoService {
//Aqui deberia hacerse uso del Cache dado que esta mal gestionado , esto se deberia hacer en un Controller y no en Service en el controller
    //Se usa el metodo backup y restore para guardar y restaurar los funkos de la base de datos
    private List<Funkos> funkos;
    private final Lector leer ;

    private static  FunkoServiceImpl instance ;

    private final FunkoRepository funkoRepository;

    public static FunkoServiceImpl getInstance(){
        if(instance == null){
            instance = new FunkoServiceImpl(FunkoRepositoryImpl.getInstance(DatabaseManager.getInstance(), BackupImpl.getInstance()));
        }
        return instance;
    }

    private FunkoServiceImpl(FunkoRepository funkoRepository) {

        this.funkoRepository = funkoRepository;
        this.leer = Lector.getInstance();
        this.funkos = leer.leerFunkos();
    }


    public void leerfichero() {
        for (int i = 0; i < funkos.size(); i++) {
            try {
                funkoRepository.save(funkos.get(i));
            } catch (SQLException e) {
                throw new FunkoDontSave("Error al leer funko " + funkos.get(i).getNombre());
            }
        }
    }

    @Override
    public Funkos funkoMasCaro() {
        List<Funkos> funkos = funkoRepository.findAll();

        return funkos.stream().max(Comparator.comparingDouble(value -> value.getPrecio())).orElse(null);
    }

    @Override
    public Double mediaDePrecio() {
        List<Funkos> funkos = funkoRepository.findAll();
        return funkos.stream().mapToDouble(value -> value.getPrecio()).average().orElse(0);
    }

    @Override
    public Map<Modelo,List<Funkos>> funkosAgrupadosPorModelo() {
        List<Funkos> funkos = funkoRepository.findAll();

        return funkos.stream().collect(Collectors.groupingBy(Funkos::getModelo));
    }

    @Override
    public Map<Modelo,Long> numeroDeFunkosPorModelo() {
        List<Funkos> funkos=funkoRepository.findAll();
        return funkos.stream().collect(Collectors.groupingBy(funkos1 -> funkos1.getModelo(), Collectors.counting()));
    }

    @Override
    public List<Funkos> funkosLanzados2023() {
        List<Funkos> funkos = funkoRepository.findAll();
        return funkos.stream().filter(value -> value.getFecha().getYear() == 2023).toList();
    }

    @Override
    public Long numeroDeFunkosDeStitch() {
        List<Funkos>funkos=funkoRepository.findAll();
        return funkos.stream().filter(value -> value.getNombre().contains("Stich")).count();
    }

    @Override
    public List<Funkos> listadoFunkosDeStitch() {
        List<Funkos> funkos=funkoRepository.findAll();
        return funkos.stream().filter(value -> value.getNombre().contains("Stich")).toList();
    }
    public void backup() throws SQLException, IOException {
        funkoRepository.backup();
    }

    public void restore() throws SQLException, IOException {
        funkoRepository.restore();
    }
//
//    @Override Ejemplo de uso del Cache dado que esta mal gestionado
//    public Funko save(Funko funko) throws SQLException, SQLException {
//        funko = funkoRepository.save(funko);
//        if(funko.getId() != null){
//            logger.debug("Añadiendo funko {} a la cache", funko.getCOD());
//            CACHE.put(funko.getId(), funko);
//        }else{
//            logger.error("El funko {} no se puedo añadir a la cache", funko.getCOD());
//        }
//
//        return funko;
//    }

//    @Override
//    public Funkos update(Funkos funko) throws SQLException, SQLException {
//        funko = funkoRepository.update(funko);
//        if(funko != null){
//            logger.debug("El funko {} se pudo añadir a la cache", funko.getCOD());
//            CACHE.put(funko.getId(), funko);
//        }else{
//            logger.error("El funko {} no se pudo añadir a la cache", funko.getCOD());
//        }
//
//        return funko;
//    }
//
//    @Override
//    public Optional<Funkos> findById(Integer id) throws SQLException {
//        Funkos funko = CACHE.get(id);
//        if(funko != null){
//            logger.debug("Obteniendo funko {} desde la cache", funko.getCOD());
//            return Optional.of(funko);
//        }else{
//            logger.error("Obteniendo funko de la base de datos");
//            return funkoRepository.findById(id);
//        }
//
//    }
}
