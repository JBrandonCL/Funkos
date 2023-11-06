package dev.jbcl.Utils;

import dev.jbcl.Model.Funkos;
import dev.jbcl.Model.Modelo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Lector {

    private static Lector instance;

    private Logger logger= LoggerFactory.getLogger(Lector.class);

    public static Lector getInstance(){
        if(instance == null){
            instance = new Lector();
        }
        return instance;
    }


   public List<Funkos> leerFunkos(){
        String dataFile ="funkos.csv";
        String directory= System.getProperty("user.dir");
        Path dir = Paths.get(directory+ File.separator+"data" +File.separator+dataFile);
        try{
        final List<String> lineas = Files.readAllLines(dir, StandardCharsets.UTF_8);
            return lineas.stream().skip(1).map(Lector::parseFunkos).collect(Collectors.toList());
        }catch (IOException e){
            logger.error("Error al leer funkos");
        }

        return null;
    }
    private static Funkos parseFunkos(String linea){
    String[] datos = linea.split(",");
    UUID cod =(UUID.fromString(datos[0].length()>36?datos[0].substring(0,35):datos[0]));
    String nombre = datos[1];
    Modelo modelo = Modelo.valueOf(datos[2]);
    double precio = Double.parseDouble(datos[3]);
    LocalDate fecha = LocalDate.parse(datos[4]);
    LocalDate fechaHoy= LocalDate.now();

        return Funkos.builder().cod(cod).nombre(nombre).modelo(modelo).precio(precio).fecha(fecha).created_at(fechaHoy).build();
    }
}
