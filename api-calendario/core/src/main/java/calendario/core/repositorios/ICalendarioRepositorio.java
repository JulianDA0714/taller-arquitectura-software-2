package calendario.core.repositorios;

import java.util.List;

import calendario.dominio.entidades.Calendario;

public interface ICalendarioRepositorio {

    /**
     * Retorna los días almacenados del año en orden de fecha.
     * La lista está vacía cuando no hay registros para ese año.
     */
    List<Calendario> listarPorAnio(int anio);

    /**
     * Indica si existe al menos un día almacenado para el año.
     */
    boolean existePorAnio(int anio);

    /**
     * Guarda los días recibidos y retorna los registros persistidos.
     */
    List<Calendario> guardarTodos(List<Calendario> calendario);
}
