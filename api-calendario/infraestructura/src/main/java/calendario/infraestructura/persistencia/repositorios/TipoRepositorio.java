package calendario.infraestructura.persistencia.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import calendario.core.repositorios.ITipoRepositorio;
import calendario.dominio.entidades.Tipo;
import calendario.infraestructura.persistencia.mapeadores.TipoMapeador;
import calendario.infraestructura.persistencia.repositorios.interfaces.ITipoRepositorioJpa;

@Repository
@Transactional(readOnly = true)
public class TipoRepositorio implements ITipoRepositorio {

    private final ITipoRepositorioJpa repositorio;
    private final TipoMapeador mapeador;

    public TipoRepositorio(ITipoRepositorioJpa repositorio, TipoMapeador mapeador) {
        this.repositorio = repositorio;
        this.mapeador = mapeador;
    }

    @Override
    public List<Tipo> listar() {
        return repositorio.findAll(Sort.by("id")).stream().map(mapeador::toModelo).toList();
    }

    @Override
    public Optional<Tipo> obtenerPorId(int id) {
        return repositorio.findById(id).map(mapeador::toModelo);
    }
}
