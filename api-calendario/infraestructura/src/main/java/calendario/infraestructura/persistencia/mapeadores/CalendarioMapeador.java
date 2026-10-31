package calendario.infraestructura.persistencia.mapeadores;

import org.springframework.stereotype.Component;

import calendario.dominio.entidades.Calendario;
import calendario.infraestructura.persistencia.entidades.CalendarioEntidad;

@Component
public class CalendarioMapeador {

    private final TipoMapeador tipoMapeador;

    public CalendarioMapeador(TipoMapeador tipoMapeador) {
        this.tipoMapeador = tipoMapeador;
    }

    public Calendario toModelo(CalendarioEntidad entidad) {
        return entidad == null ? null : new Calendario(
                entidad.getId(), entidad.getFecha(), tipoMapeador.toModelo(entidad.getTipo()),
                entidad.getDescripcion());
    }

    public CalendarioEntidad toEntidad(Calendario modelo) {
        return modelo == null ? null : new CalendarioEntidad(
                modelo.getId(), modelo.getFecha(), tipoMapeador.toEntidad(modelo.getTipo()),
                modelo.getDescripcion());
    }
}
