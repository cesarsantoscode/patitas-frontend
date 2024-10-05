package pe.edu.cibertec.patitas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.cibertec.patitas.config.FeignClientConfig;
import pe.edu.cibertec.patitas.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas.dto.LoginResponseDTO;
import pe.edu.cibertec.patitas.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas.dto.LogoutResponseDTO;

@FeignClient(name = "autenticacion", url = "http://localhost:8081/autenticacion", configuration = FeignClientConfig.class)
public interface AutenticacionClient {

    @PostMapping("/login")
    ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO);

    @PostMapping("/logout")
    ResponseEntity<LogoutResponseDTO> logout(@RequestBody LogoutRequestDTO logoutRequestDTO);

}
