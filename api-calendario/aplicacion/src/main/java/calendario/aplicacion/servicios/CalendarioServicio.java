package calendario.aplicacion.servicios;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Service;

import calendario.core.integracion.IFestivoServicioExterno;
import calendario.core.repositorios.ICalendarioRepositorio;
import calendario.core.repositorios.ITipoRepositorio;
import calendario.core.servicios.ICalendarioServicio;
import calendario.dominio.dtos.CalendarioDto;
import calendario.dominio.dtos.FestivoDto;
import calendario.dominio.entidades.Calendario;
import calendario.dominio.entidades.Tipo;
import calendario.dominio.excepciones.AnioInvalidoException;
import calendario.dominio.excepciones.CalendarioExistenteException;
import calendario.dominio.excepciones.ConfiguracionCalendarioException;
import calendario.dominio.excepciones.IntegracionFestivosException;

@Service
public class CalendarioServicio implements ICalendarioServicio {

    private static final Locale ESPANOL_COLOMBIA = Locale.forLanguageTag("es-CO");

    private final ICalendarioRepositorio calendarioRepositorio;
    private final ITipoRepositorio tipoRepositorio;
    private final IFestivoServicioExterno festivoServicioExterno;

    public CalendarioServicio(ICalendarioRepositorio calendarioRepositorio,
            ITipoRepositorio tipoRepositorio, IFestivoServicioExterno festivoServicioExterno) {
        this.calendarioRepositorio = calendarioRepositorio;
        this.tipoRepositorio = tipoRepositorio;
        this.festivoServicioExterno = festivoServicioExterno;
    }

    @Override
    public boolean generar(int anio) {
        validarAnio(anio);

        if (calendarioRepositorio.existePorAnio(anio)) {
            throw new CalendarioExistenteException(anio);
        }

        Tipo laboral = obtenerTipo(1, "Día laboral");
        Tipo finDeSemana = obtenerTipo(2, "Fin de Semana");
        Tipo festivo = obtenerTipo(3, "Día festivo");
        Set<LocalDate> fechasFestivas = obtenerFechasFestivas(anio);

        LocalDate inicio = LocalDate.of(anio, 1, 1);
        List<Calendario> dias = new ArrayList<>(inicio.lengthOfYear());

        for (int indice = 0; indice < inicio.lengthOfYear(); indice++) {
            LocalDate fecha = inicio.plusDays(indice);
            DayOfWeek diaSemana = fecha.getDayOfWeek();
            Tipo tipo;

            if (fechasFestivas.contains(fecha)) {
                tipo = festivo;
            } else if (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) {
                tipo = finDeSemana;
            } else {
                tipo = laboral;
            }

            String nombreDia = diaSemana.getDisplayName(TextStyle.FULL, ESPANOL_COLOMBIA);
            String descripcion = nombreDia.substring(0, 1).toUpperCase(ESPANOL_COLOMBIA)
                    + nombreDia.substring(1);
            dias.add(new Calendario(0, fecha, tipo, descripcion));
        }

        calendarioRepositorio.guardarTodos(dias);
        return true;
    }

    @Override
    public List<CalendarioDto> listarPorAnio(int anio) {
        validarAnio(anio);
        return calendarioRepositorio.listarPorAnio(anio).stream()
                .map(dia -> new CalendarioDto(dia.getId(), dia.getFecha(), dia.getTipo(),
                        dia.getDescripcion()))
                .toList();
    }

    private void validarAnio(int anio) {
        if (anio < 1900 || anio > 9999) {
            throw new AnioInvalidoException(anio);
        }
    }

    private Tipo obtenerTipo(int id, String nombre) {
        return tipoRepositorio.obtenerPorId(id)
                .filter(tipo -> tipo.getId() == id && nombre.equals(tipo.getTipo()))
                .orElseThrow(() -> new ConfiguracionCalendarioException(
                        "La categoría '" + nombre + "' (id " + id + ") no está configurada correctamente. "
                                + "Revisa los datos iniciales de la tabla tipo."));
    }

    private Set<LocalDate> obtenerFechasFestivas(int anio) {
        List<FestivoDto> festivos = festivoServicioExterno.listarPorAnio(anio);

        if (festivos == null || festivos.isEmpty()) {
            throw new IntegracionFestivosException("La API Festivos no devolvió festivos para el año " + anio + ".");
        }

        Set<LocalDate> fechas = new HashSet<>();
        for (FestivoDto festivo : festivos) {
            if (festivo == null || festivo.getFecha() == null || festivo.getFecha().getYear() != anio
                    || festivo.getNombre() == null || festivo.getNombre().isBlank()
                    || festivo.getTipo() < 1 || festivo.getTipo() > 4) {
                throw new IntegracionFestivosException(
                        "La API Festivos devolvió datos inválidos para el año " + anio + ".");
            }
            fechas.add(festivo.getFecha());
        }
        return fechas;
    }
}
