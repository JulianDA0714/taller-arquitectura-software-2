package calendario.infraestructura.persistencia.mapeadores;

import org.springframework.stereotype.Component;

import calendario.dominio.entidades.Tipo;
import calendario.infraestructura.persistencia.entidades.TipoEntidad;

@Component
public class TipoMapeador {

    public Tipo toModelo(TipoEntidad entidad) {
        if (entidad == null) {
            return null;
        }
        String nombre = entidad.getTipo();
        return new Tipo(entidad.getId(), nombre == null ? null : nombre.strip());
    }

    public TipoEntidad toEntidad(Tipo modelo) {
        return modelo == null ? null : new TipoEntidad(modelo.getId(), modelo.getTipo());
    }
}
