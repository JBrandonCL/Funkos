package dev.jbcl.Controller;

import dev.jbcl.Model.Funkos;
import dev.jbcl.Model.Modelo;
import dev.jbcl.Service.Funkos.FunkoService;

import java.util.List;
import java.util.Map;

public class FunkoController {
    private final FunkoService funkoService;

    public FunkoController(FunkoService funkoService) {
        this.funkoService = funkoService;
    }

    public void leerCSV() {funkoService.leerfichero();}

    public Funkos funkoMasCaro() {return funkoService.funkoMasCaro();}

    public Double mediaDePrecio() {return funkoService.mediaDePrecio();}

    public Map<Modelo, List<Funkos>> funkosAgrupadosPorModelo() {return funkoService.funkosAgrupadosPorModelo();}

    public Map<Modelo, Long> numeroDeFunkosPorModelo() {return funkoService.numeroDeFunkosPorModelo();}

    public List<Funkos> funkosLanzados2023() {return funkoService.funkosLanzados2023();}

    public Long numeroDeFunkosDeStitch() {return funkoService.numeroDeFunkosDeStitch();}

    public List<Funkos> listadoFunkosDeStitch() {return funkoService.listadoFunkosDeStitch();}
}


