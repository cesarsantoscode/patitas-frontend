package pe.edu.cibertec.patitas.config;

import feign.Request;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class FeignClientConfig {

    @Bean
    public Request.Options requestOptions() {
        // configurar timeout de conexión y el timeout de lectura
        return new Request.Options(5000, 30000); // (conexión, lectura)
    }

}
