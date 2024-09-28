package pe.edu.cibertec.patitas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.edu.cibertec.patitas.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas.dto.LoginResponseDTO;
import pe.edu.cibertec.patitas.service.LoginService;
import pe.edu.cibertec.patitas.viewmodel.LoginModel;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    LoginService loginService;

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

        // preparar request
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(tipoDocumento, numeroDocumento, password);

        // validar campos de entrada
        if (!loginService.validarRequest(loginRequestDTO)) {

            LoginModel loginModel = new LoginModel("01", "Error: Debe completar correctamente sus credenciales", "");
            model.addAttribute("loginModel", loginModel);
            return "inicio";

        }

        // consumir servicio de autenticacion
        try {

            // invocar servicio de autenticación
            LoginResponseDTO loginResponseDTO = loginService.autenticar(loginRequestDTO);

            // validar respuesta de servicio
            if (loginResponseDTO.codigo().equals("00")){

                LoginModel loginModel = new LoginModel("00", "", loginResponseDTO.nombreUsuario());
                model.addAttribute("loginModel", loginModel);
                return "principal";

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

    }

}
