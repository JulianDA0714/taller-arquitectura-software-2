package calendario.infraestructura.integracion;

import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpServicio {

    @Bean
    public RestTemplate restTemplate(
            RestTemplateBuilder builder,
            ObjectMapper objectMapper,
            @Value("${festivos.timeout-conexion:3s}") Duration timeoutConexion,
            @Value("${festivos.timeout-lectura:10s}") Duration timeoutLectura) {
        return builder
                .setConnectTimeout(timeoutConexion)
                .setReadTimeout(timeoutLectura)
                .messageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }
}
