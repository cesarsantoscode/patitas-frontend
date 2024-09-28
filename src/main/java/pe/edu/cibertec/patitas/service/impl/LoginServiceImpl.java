package pe.edu.cibertec.patitas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.patitas.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas.dto.LoginResponseDTO;
import pe.edu.cibertec.patitas.service.LoginService;
import pe.edu.cibertec.patitas.util.ValidationUtil;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    RestTemplate restTemplateAutenticacion;

    @Override
    public boolean validarRequest(LoginRequestDTO loginRequestDTO) {

        return (ValidationUtil.isValid(loginRequestDTO.tipoDocumento()) &&
                ValidationUtil.isValid(loginRequestDTO.numeroDocumento()) &&
                ValidationUtil.isValid(loginRequestDTO.password()));

    }

    @Override
    public LoginResponseDTO autenticar(LoginRequestDTO loginRequestDTO) throws Exception {

        return restTemplateAutenticacion.postForObject("/login", loginRequestDTO, LoginResponseDTO.class);

    }

}
