package edu.eci.cvds.task_back.Services;

import edu.eci.cvds.task_back.Auth.AuthResponse;
import edu.eci.cvds.task_back.Auth.LoginRequest;
import edu.eci.cvds.task_back.Auth.RegisterRequest;
import edu.eci.cvds.task_back.Repositories.mysql.UserMySqlRepository;
import edu.eci.cvds.task_back.Domain.Role;

import org.springframework.beans.factory.annotation.Autowired;
import edu.eci.cvds.task_back.Domain.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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
        try {


            if (request.getUsername() == null || request.getUsername().isEmpty()) {
                throw new Exception("Username is required!");
            }
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                throw new Exception("Email is required!");
            }
            if (request.getPasswd() == null || request.getPasswd().isEmpty()) {
                throw new Exception("Password is required!");
            }
            if (!isValidPassword(request.getPasswd())) {
                throw new Exception("Password must be at least 8 characters long, with one uppercase letter, one number, and one special character!");
            }
            if (userRepository.findByUsername(request.getUsername()) != null) {
                throw new Exception("Username is already taken!");
            }
            Role userRole = (request.getUsername().equals("ADMIN")) ? Role.ADMIN : Role.USER;
//            User user = User.builder()
//                    .username(request.getUsername())
//                    .email(request.getEmail())
//                    .passwd(request.getPasswd())
//                    .role(Role.USER)
//                    .build();

            User user = new User(request.getUsername(), request.getEmail(), request.getPasswd(), userRole);
//            return  AuthResponse.builder()
//                    .token(this.jwtService.getToken(user))
//                    .build();
            this.createUser(user);
            AuthResponse authResponse = new AuthResponse(this.jwtService.getToken(user));
            String userId = userRepository.findByUsername(request.getUsername()).getId();
            authResponse.setUserId(userId);
            return authResponse;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }

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
        try{

            if(userRepository.findByEmail(user.getEmail())!=null) throw new Exception("The email has already been used");
            user.setPasswd(passwordEncoder.encode(user.getPasswd())); //encripta la contraseña
            this.userRepository.createUser(user);
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    /**
     * Autentica un usuario en el sistema.
     * @param request Contiene las credenciales del usuario (nombre de usuario y contraseña).
     * @return AuthResponse con el token JWT generado y el ID del usuario autenticado.
     */
    public AuthResponse login(LoginRequest request) throws Exception{

        UserDetails user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new Exception("User does not exist!");
        }
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPasswd()));
            String token = jwtService.getToken(user);
            AuthResponse response = new AuthResponse(token);
            String userId = userRepository.findByUsername(request.getUsername()).getId();
            response.setUserId(userId);
            return response;
        }catch(Exception e){
            throw new Exception("Invalid credentials!");
        }
    }
}
