package edu.eci.cvds.task_back.Controller;

import edu.eci.cvds.task_back.Auth.LoginRequest;
import edu.eci.cvds.task_back.Auth.RegisterRequest;
import edu.eci.cvds.task_back.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import edu.eci.cvds.task_back.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para manejar las operaciones de autenticación de usuarios.
 * Incluye el registro de nuevos usuarios y el inicio de sesión.
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    /**
     * Maneja la solicitud de registro de un nuevo usuario.
     * @param request Contiene la información del usuario a registrar.
     * @return Respuesta HTTP con el estado de la operación.
     */
    @CrossOrigin(origins = "https://taskManager.com/R")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            return  ResponseEntity.ok(authService.register(request));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Maneja la solicitud de inicio de sesión de un usuario.
     *
     * @param request Contiene las credenciales del usuario para iniciar sesión.
     * @return Respuesta HTTP con el token de autenticación y el estado de la operación.
     */
    @CrossOrigin(origins = "https://taskManager.com/L")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return  ResponseEntity.ok(authService.login(request));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

