package dev.jbcl.Service.Funkos;

import dev.jbcl.Model.Funkos;
import dev.jbcl.Model.Modelo;

import java.util.List;
import java.util.Map;

public interface FunkoService {
    void leerfichero() ;
    Funkos funkoMasCaro();
    Double mediaDePrecio();

    Map<Modelo,List<Funkos>> funkosAgrupadosPorModelo();

    Map<Modelo,Long> numeroDeFunkosPorModelo();

    List<Funkos> funkosLanzados2023();

    Long numeroDeFunkosDeStitch();

    List<Funkos> listadoFunkosDeStitch();


}
