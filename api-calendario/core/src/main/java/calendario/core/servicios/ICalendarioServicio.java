package calendario.core.servicios;

import java.util.List;

import calendario.dominio.dtos.CalendarioDto;

public interface ICalendarioServicio {

    /**
     * Genera y almacena el calendario del año indicado.
     *
     * @return true cuando se completa la generación.
     */
    boolean generar(int anio);

    /**
     * Retorna los días almacenados del año en orden de fecha.
     * La lista está vacía cuando no hay registros para ese año.
     */
    List<CalendarioDto> listarPorAnio(int anio);
}
