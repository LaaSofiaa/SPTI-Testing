package edu.eci.cvds.task_back.services;

import edu.eci.cvds.task_back.Auth.AuthResponse;
import edu.eci.cvds.task_back.Auth.LoginRequest;
import edu.eci.cvds.task_back.Auth.RegisterRequest;
import edu.eci.cvds.task_back.Repositories.mysql.UserMySqlRepository;
import edu.eci.cvds.task_back.domain.Role;
import edu.eci.cvds.task_back.exception.SecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import edu.eci.cvds.task_back.domain.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//Para cambio
@Service

public class AuthService {
    private UserMySqlRepository userRepository;
    private JwtService jwtService;

    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    @Autowired
    public AuthService(UserMySqlRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder=passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * @param request Contiene los datos del usuario (nombre de usuario, email, contraseña).
     * @return AuthResponse con el token JWT generado y el ID del usuario registrado.
     * @throws Exception Si el nombre de usuario o el email ya están registrados, o si alguno de los campos no es válido.
     */
    public AuthResponse register(RegisterRequest request) throws Exception{

        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new SecurityException(SecurityException.USER_REQUIRED);
        }
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new SecurityException(SecurityException.EMAIL_REQUIRED);
        }
        if (request.getPasswd() == null || request.getPasswd().isEmpty()) {
            throw new SecurityException(SecurityException.PASSWORD_REQUIRED);
        }
        if (!isValidPassword(request.getPasswd())) {
            throw new SecurityException(SecurityException.PASSWORD_PARAMETERS);
        }
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new SecurityException(SecurityException.USERNAME_TAKEN);
        }
        Role userRole = (request.getUsername().equals("ADMIN")) ? Role.ADMIN : Role.USER;
        User user = new User(request.getUsername(), request.getEmail(), request.getPasswd(), userRole);
        this.createUser(user);
        AuthResponse authResponse = new AuthResponse(this.jwtService.getToken(user));
        String userId = userRepository.findByUsername(request.getUsername()).getId();
        authResponse.setUserId(userId);
        return authResponse;
    }

    /**
     * Verifica si la contraseña proporcionada cumple con las reglas de seguridad.
     * @param password Contraseña que se va a validar.
     * @return true si la contraseña es válida, false si no cumple con los requisitos.
     */
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=-])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }

    /**
     * Crea un usuario nuevo en la base de datos.
     * @param user El objeto User que se va a crear.
     * @throws Exception Si el email ya ha sido utilizado previamente.
     */
    public void createUser(User user) throws Exception{
            if(userRepository.findByEmail(user.getEmail())!=null) throw new SecurityException(SecurityException.EMAIL_USE);
            user.setPasswd(passwordEncoder.encode(user.getPassword()));
            this.userRepository.createUser(user);

    }

    /**
     * Autentica un usuario en el sistema.
     * @param request Contiene las credenciales del usuario (nombre de usuario y contraseña).
     * @return AuthResponse con el token JWT generado y el ID del usuario autenticado.
     */
    public AuthResponse login(LoginRequest request) throws Exception {
        UserDetails user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new SecurityException(SecurityException.USUARIO_SIN_ENCONTRAR);
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPasswd()));
        String token = jwtService.getToken(user);
        AuthResponse response = new AuthResponse(token);
        String userId = userRepository.findByUsername(request.getUsername()).getId();
        response.setUserId(userId);
        return response;
    }
}
