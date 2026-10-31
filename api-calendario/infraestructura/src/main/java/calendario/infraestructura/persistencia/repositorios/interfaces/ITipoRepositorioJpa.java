package calendario.infraestructura.persistencia.repositorios.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import calendario.infraestructura.persistencia.entidades.TipoEntidad;

public interface ITipoRepositorioJpa extends JpaRepository<TipoEntidad, Integer> {
}
