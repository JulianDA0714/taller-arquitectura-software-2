package calendario.core.repositorios;

import java.util.List;
import java.util.Optional;

import calendario.dominio.entidades.Tipo;

public interface ITipoRepositorio {

    List<Tipo> listar();

    /**
     * Retorna un Optional vacío cuando no existe el identificador.
     */
    Optional<Tipo> obtenerPorId(int id);
}
