package calendario.presentacion.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import calendario.dominio.excepciones.AnioInvalidoException;
import calendario.dominio.excepciones.CalendarioExistenteException;
import calendario.dominio.excepciones.ConfiguracionCalendarioException;
import calendario.dominio.excepciones.IntegracionFestivosException;
import calendario.presentacion.dtos.ErrorRespuesta;

@RestControllerAdvice
public class ExcepcionesGlobalesHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcepcionesGlobalesHandler.class);

    @ExceptionHandler(AnioInvalidoException.class)
    public ResponseEntity<ErrorRespuesta> manejarAnio(AnioInvalidoException error) {
        return respuesta(HttpStatus.BAD_REQUEST, error.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorRespuesta> manejarParametro(MethodArgumentTypeMismatchException error) {
        return respuesta(HttpStatus.BAD_REQUEST, "El año debe ser un número entero entre 1900 y 9999.");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorRespuesta> manejarRuta(NoResourceFoundException error) {
        return respuesta(HttpStatus.NOT_FOUND, "La ruta solicitada no existe.");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorRespuesta> manejarMetodo(HttpRequestMethodNotSupportedException error) {
        return respuesta(HttpStatus.METHOD_NOT_ALLOWED, "El método HTTP no está permitido para esta ruta.");
    }

    @ExceptionHandler(CalendarioExistenteException.class)
    public ResponseEntity<ErrorRespuesta> manejarExistente(CalendarioExistenteException error) {
        return respuesta(HttpStatus.CONFLICT, error.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorRespuesta> manejarRestriccion(DataIntegrityViolationException error) {
        LOGGER.warn("No fue posible guardar el calendario por una restricción de integridad.");
        return respuesta(HttpStatus.CONFLICT,
                "No se pudo guardar el calendario: existen fechas duplicadas o datos incompatibles con el modelo.");
    }

    @ExceptionHandler(IntegracionFestivosException.class)
    public ResponseEntity<ErrorRespuesta> manejarIntegracion(IntegracionFestivosException error) {
        HttpStatus estado = error.isTiempoAgotado() ? HttpStatus.GATEWAY_TIMEOUT : HttpStatus.BAD_GATEWAY;
        LOGGER.warn("Falló la consulta a la API Festivos: {}", error.getMessage());
        return respuesta(estado, error.getMessage());
    }

    @ExceptionHandler(ConfiguracionCalendarioException.class)
    public ResponseEntity<ErrorRespuesta> manejarConfiguracion(ConfiguracionCalendarioException error) {
        LOGGER.error("Configuración del calendario incompleta: {}", error.getMessage());
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> manejarGeneral(Exception error) {
        LOGGER.error("Error inesperado procesando el calendario.", error);
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno procesando el calendario.");
    }

    private ResponseEntity<ErrorRespuesta> respuesta(HttpStatus estado, String mensaje) {
        return ResponseEntity.status(estado)
                .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                .body(new ErrorRespuesta(estado.value(), mensaje));
    }
}
