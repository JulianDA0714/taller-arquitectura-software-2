package calendario.core.integracion;

import java.util.List;

import calendario.dominio.dtos.FestivoDto;

public interface IFestivoServicioExterno {

    /**
     * Consulta los festivos del año en la API Festivos.
     */
    List<FestivoDto> listarPorAnio(int anio);
}
