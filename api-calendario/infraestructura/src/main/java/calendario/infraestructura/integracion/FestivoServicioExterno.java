package calendario.infraestructura.integracion;

import java.net.SocketTimeoutException;
import java.net.http.HttpTimeoutException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import calendario.core.integracion.IFestivoServicioExterno;
import calendario.dominio.dtos.FestivoDto;
import calendario.dominio.excepciones.IntegracionFestivosException;

@Service
public class FestivoServicioExterno implements IFestivoServicioExterno {

    private final RestTemplate restTemplate;
    private final String url;

    public FestivoServicioExterno(
            RestTemplate restTemplate,
            @Value("${festivos.url:http://localhost:3030/api/festivos}") String url) {
        this.restTemplate = restTemplate;
        this.url = url.replaceAll("/+$", "");
    }

    @Override
    public List<FestivoDto> listarPorAnio(int anio) {
        try {
            ResponseEntity<FestivoDto[]> respuesta = restTemplate.getForEntity(
                    url + "/{anio}", FestivoDto[].class, anio);
            if (!respuesta.getStatusCode().is2xxSuccessful()) {
                throw new IntegracionFestivosException(
                        "La API Festivos respondió con un estado HTTP inesperado "
                                + respuesta.getStatusCode().value() + ".");
            }
            FestivoDto[] festivos = respuesta.getBody();
            if (festivos == null) {
                throw new IntegracionFestivosException("La API Festivos devolvió una respuesta sin datos.");
            }
            return Arrays.asList(festivos);
        } catch (ResourceAccessException excepcion) {
            boolean tiempoAgotado = esTiempoAgotado(excepcion);
            String mensaje = tiempoAgotado
                    ? "La API Festivos superó el tiempo de espera."
                    : "No fue posible conectar con la API Festivos.";
            throw new IntegracionFestivosException(mensaje, excepcion, tiempoAgotado);
        } catch (RestClientResponseException excepcion) {
            throw new IntegracionFestivosException(
                    "La API Festivos respondió con un error HTTP " + excepcion.getStatusCode().value() + ".",
                    excepcion);
        } catch (RestClientException excepcion) {
            throw new IntegracionFestivosException(
                    "No fue posible interpretar la respuesta de la API Festivos.", excepcion);
        }
    }

    private boolean esTiempoAgotado(Throwable excepcion) {
        for (Throwable causa = excepcion; causa != null; causa = causa.getCause()) {
            if (causa instanceof SocketTimeoutException || causa instanceof HttpTimeoutException) {
                return true;
            }
        }
        return false;
    }
}
