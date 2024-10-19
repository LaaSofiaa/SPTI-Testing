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
    public AuthResponse register(RegisterRequest request) throws Exception{
        try{
//            User user = User.builder()
//                    .username(request.getUsername())
//                    .email(request.getEmail())
//                    .passwd(request.getPasswd())
//                    .role(Role.USER)
//                    .build();

            User user = new User(request.getUsername(), request.getEmail(), request.getPasswd(), Role.USER);
//            return  AuthResponse.builder()
//                    .token(this.jwtService.getToken(user))
//                    .build();
            this.createUser(user);
            AuthResponse authResponse = new AuthResponse(this.jwtService.getToken(user));
            return authResponse;
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }
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

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPasswd()));
        UserDetails user=userRepository.findByUsername(request.getUsername());
        String token=jwtService.getToken(user);
        AuthResponse response = new AuthResponse(token);
        String userId = userRepository.findByUsername(request.getUsername()).getId();
        response.setUserId(userId);
        return response;
    }
}
