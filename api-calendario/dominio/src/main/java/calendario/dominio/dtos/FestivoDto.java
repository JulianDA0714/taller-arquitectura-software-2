package calendario.dominio.dtos;

import java.time.LocalDate;

public class FestivoDto {

    private String nombre;
    private LocalDate fecha;
    private int tipo;

    public FestivoDto() {
    }

    public FestivoDto(String nombre, LocalDate fecha, int tipo) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
