package pe.edu.cibertec.patitas.service;

import pe.edu.cibertec.patitas.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas.dto.LoginResponseDTO;

public interface LoginService {

    boolean validarRequest(LoginRequestDTO loginRequestDTO);

    LoginResponseDTO autenticar(LoginRequestDTO loginRequestDTO) throws Exception;

}
