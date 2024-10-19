package edu.eci.cvds.task_back.Controller;

import edu.eci.cvds.task_back.Auth.AuthResponse;
import edu.eci.cvds.task_back.Auth.LoginRequest;
import edu.eci.cvds.task_back.Auth.RegisterRequest;
import edu.eci.cvds.task_back.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import edu.eci.cvds.task_back.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;
    @CrossOrigin(origins = "*")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            return  ResponseEntity.ok(authService.register(request));
            //userService.createUser(user);
            //return new ResponseEntity<>(new AuthResponse(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            return  ResponseEntity.ok(authService.login(request));

            //String email = loginData.get("email");
            //String passwd = loginData.get("passwd");
            //String id = userService.authentication(email, passwd) ;
//            if(id!=null){
//                return new ResponseEntity<>(new AuthResponse(), HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(new AuthResponse(), HttpStatus.UNAUTHORIZED);
//            }
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

