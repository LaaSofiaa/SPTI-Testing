package edu.eci.cvds.task_back.Domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import edu.eci.cvds.task_back.Services.UserService;

@Configuration
public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                    .antMatchers("/taskManager/**").authenticated()  // Protege todas las rutas relacionadas con tareas
//                    .antMatchers("/auth/**").permitAll()  // Permite acceso libre a la autenticación
//                .and()
//                .formLogin()  // Habilita el inicio de sesión por formulario
//                .and()
//                .logout().permitAll();  // cerrar sesión
//        return http.build();
//    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}

