package dev.jbcl;

import dev.jbcl.Controller.FunkoController;
import dev.jbcl.Model.Funkos;
import dev.jbcl.Service.Funkos.FunkoServiceImpl;
import dev.jbcl.Utils.Lector;
import dev.jbcl.repository.FunkoRepositoryImpl;

import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Press Alt+Intro with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        FunkoController funkoController= new FunkoController(FunkoServiceImpl.getInstance());
        funkoController.leerCSV(); //Imporante leer antes que nada falta aplicar el cache
        System.out.println("--------------------------------");
        System.out.println(funkoController.funkoMasCaro());
        System.out.println("--------------------------------");



    }
}