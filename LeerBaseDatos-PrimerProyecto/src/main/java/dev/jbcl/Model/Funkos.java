package dev.jbcl.Model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder

public class Funkos {
    public int id;
    public UUID cod;
    public String nombre;
    public Modelo modelo;
    public double precio;
    public LocalDate fecha;
    public LocalDate created_at;
    public LocalDate updated_at;

    @Override
    public String toString() {
        return "Funkos{" +
                "cod=" + cod +
                ", nombre='" + nombre + '\'' +
                ", modelo='" + modelo + '\'' +
                ", precio=" + precio +
                ", fecha=" + fecha +
                '}';
    }
}
