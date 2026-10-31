package calendario.presentacion.controladores;

import java.util.List;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import calendario.core.servicios.ICalendarioServicio;
import calendario.dominio.dtos.CalendarioDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api/calendario", produces = "application/json;charset=UTF-8")
@Tag(name = "Calendario", description = "Generación y consulta del calendario laboral anual")
public class CalendarioControlador {

    private final ICalendarioServicio servicio;

    public CalendarioControlador(ICalendarioServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/generar/{anio}")
    @Operation(summary = "Generar y almacenar todos los días de un año",
            description = "Usa GET como el ejemplo del enunciado. Si el año ya tiene registros, responde 409.")
    public ResponseEntity<Boolean> generar(@PathVariable int anio) {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(servicio.generar(anio));
    }

    @GetMapping("/listar/{anio}")
    @Operation(summary = "Consultar el calendario de un año en orden de fecha")
    public List<CalendarioDto> listar(@PathVariable int anio) {
        return servicio.listarPorAnio(anio);
    }
}
