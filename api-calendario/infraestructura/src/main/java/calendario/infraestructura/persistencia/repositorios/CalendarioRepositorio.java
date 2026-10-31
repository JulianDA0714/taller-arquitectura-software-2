package calendario.infraestructura.persistencia.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import calendario.core.repositorios.ICalendarioRepositorio;
import calendario.dominio.entidades.Calendario;
import calendario.infraestructura.persistencia.mapeadores.CalendarioMapeador;
import calendario.infraestructura.persistencia.repositorios.interfaces.ICalendarioRepositorioJpa;

@Repository
@Transactional(readOnly = true)
public class CalendarioRepositorio implements ICalendarioRepositorio {

    private final ICalendarioRepositorioJpa repositorio;
    private final CalendarioMapeador mapeador;

    public CalendarioRepositorio(ICalendarioRepositorioJpa repositorio, CalendarioMapeador mapeador) {
        this.repositorio = repositorio;
        this.mapeador = mapeador;
    }

    @Override
    public List<Calendario> listarPorAnio(int anio) {
        return repositorio.findByFechaBetweenOrderByFechaAsc(inicio(anio), fin(anio))
                .stream().map(mapeador::toModelo).toList();
    }

    @Override
    public boolean existePorAnio(int anio) {
        return repositorio.existsByFechaBetween(inicio(anio), fin(anio));
    }

    @Override
    @Transactional
    public List<Calendario> guardarTodos(List<Calendario> calendario) {
        var entidades = calendario.stream().map(mapeador::toEntidad).toList();
        return repositorio.saveAllAndFlush(entidades).stream().map(mapeador::toModelo).toList();
    }

    private LocalDate inicio(int anio) {
        return LocalDate.of(anio, 1, 1);
    }

    private LocalDate fin(int anio) {
        return LocalDate.of(anio, 12, 31);
    }
}
