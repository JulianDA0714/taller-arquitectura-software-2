package calendario.infraestructura.persistencia.repositorios.interfaces;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import calendario.infraestructura.persistencia.entidades.CalendarioEntidad;

public interface ICalendarioRepositorioJpa extends JpaRepository<CalendarioEntidad, Integer> {

    List<CalendarioEntidad> findByFechaBetweenOrderByFechaAsc(LocalDate inicio, LocalDate fin);

    boolean existsByFechaBetween(LocalDate inicio, LocalDate fin);
}
