package pe.edu.cibertec.patitas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.patitas.client.AutenticacionClient;
import pe.edu.cibertec.patitas.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas.dto.LoginResponseDTO;
import pe.edu.cibertec.patitas.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas.dto.LogoutResponseDTO;
import pe.edu.cibertec.patitas.viewmodel.LoginModel;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AutenticacionClient autenticacionClient;

    @GetMapping("/inicio")
    public String login(Model model) {
        LoginModel loginModel = new LoginModel("00", "", "");
        model.addAttribute("loginModel", loginModel);
        return "inicio";
    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam("tipoDocumento") String tipoDocumento,
                             @RequestParam("numeroDocumento") String numeroDocumento,
                             @RequestParam("password") String password,
                             Model model) {

        // validar campos de entrada
        if (tipoDocumento == null || tipoDocumento.trim().length() == 0 ||
                numeroDocumento == null || numeroDocumento.trim().length() == 0 ||
                password == null || password.trim().length() == 0) {

            LoginModel loginModel = new LoginModel("01", "Error: Debe completar correctamente sus credenciales", "");
            model.addAttribute("loginModel", loginModel);
            return "inicio";

        }

        try {

            // Invocar servicio de autenticación
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(tipoDocumento, numeroDocumento, password);
            LoginResponseDTO loginResponseDTO = restTemplate.postForObject("/login", loginRequestDTO, LoginResponseDTO.class);

            // Validar respuesta de servicio
            if (loginResponseDTO.codigo().equals("00")) {

                LoginModel loginModel = new LoginModel("00", "", loginResponseDTO.nombreUsuario());
                model.addAttribute("loginModel", loginModel);

            } else {

                LoginModel loginModel = new LoginModel("02", "Error: Autenticación fallida", "");
                model.addAttribute("loginModel", loginModel);
                return "inicio";

            }

        } catch (Exception e) {

            LoginModel loginModel = new LoginModel("99", "Error: Ocurrió un problema en la autenticación", "");
            model.addAttribute("loginModel", loginModel);
            System.out.println(e.getMessage());
            return "inicio";

        }

        return "principal";

    }

    @PostMapping("/autenticar-client")
    public String autenticarClient(@RequestParam("tipoDocumento") String tipoDocumento,
                                   @RequestParam("numeroDocumento") String numeroDocumento,
                                   @RequestParam("password") String password,
                                   Model model) {

        System.out.println("Ejecutando FeignClient...");

        // validar campos de entrada
        if (tipoDocumento == null || tipoDocumento.trim().length() == 0 ||
                numeroDocumento == null || numeroDocumento.trim().length() == 0 ||
                password == null || password.trim().length() == 0) {

            LoginModel loginModel = new LoginModel("01", "Error: Debe completar correctamente sus credenciales", "");
            model.addAttribute("loginModel", loginModel);
            return "inicio";

        }

        try {

            // consumir servicio de autenticacion
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(tipoDocumento, numeroDocumento, password);
            ResponseEntity<LoginResponseDTO> responseEntity = autenticacionClient.login(loginRequestDTO);

            // validar respuesta del servicio
            if (responseEntity.getStatusCode().is2xxSuccessful()) {

                LoginResponseDTO loginResponseDTO = responseEntity.getBody();
                if (loginResponseDTO.codigo().equals("00")) {
                    LoginModel loginModel = new LoginModel("00", "", loginResponseDTO.nombreUsuario());
                    model.addAttribute("loginModel", loginModel);
                    model.addAttribute("tipoDocumento", tipoDocumento);
                    model.addAttribute("numeroDocumento", numeroDocumento);
                    return "principal";
                } else {
                    LoginModel loginModel = new LoginModel("02", "Error: Autenticación fallida", "");
                    model.addAttribute("loginModel", loginModel);
                    return "inicio";
                }

            } else {

                LoginModel loginModel = new LoginModel("99", "Error: Ocurrió un problema en la autenticación", "");
                model.addAttribute("loginModel", loginModel);
                return "inicio";

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
            LoginModel loginModel = new LoginModel("99", "Error: Ocurrió un problema en la autenticación", "");
            model.addAttribute("loginModel", loginModel);
            return "inicio";

        }

    }

    @PostMapping("/cerrar-sesion-client")
    public String cerrarSesionClient(@RequestParam("tipoDocumento") String tipoDocumento,
                                     @RequestParam("numeroDocumento") String numeroDocumento,
                                     Model model) {

        System.out.println("Cerrando sesión con FeignClient...");

        try {

            // consumir servicio de autenticacion
            LogoutRequestDTO logoutRequestDTO = new LogoutRequestDTO(tipoDocumento, numeroDocumento);
            ResponseEntity<LogoutResponseDTO> responseEntity = autenticacionClient.logout(logoutRequestDTO);

            // validar respuesta del servicio
            if (responseEntity.getStatusCode().is2xxSuccessful()) {

                LogoutResponseDTO logoutResponseDTO = responseEntity.getBody();
                if (logoutResponseDTO != null && logoutResponseDTO.resultado()) {
                    LoginModel loginModel = new LoginModel("00", "", "");
                    model.addAttribute("loginModel", loginModel);
                    return "inicio";
                } else {
                    LoginModel loginModel = new LoginModel("02", "Error: No se pudo cerrar sesión", "");
                    model.addAttribute("loginModel", loginModel);
                    return "principal";
                }

            } else {

                LoginModel loginModel = new LoginModel("99", "Error: Ocurrió un problema al cerrar sesión", "");
                model.addAttribute("loginModel", loginModel);
                return "principal";

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
            LoginModel loginModel = new LoginModel("99", "Error: Ocurrió un problema al cerrar sesión", "");
            model.addAttribute("loginModel", loginModel);
            return "principal";

        }

    }

}
