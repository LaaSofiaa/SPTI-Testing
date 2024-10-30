package edu.eci.cvds.task_back;

import edu.eci.cvds.task_back.services.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private UserDetails userDetails;

    /**
     * Configuración inicial para cada prueba. Se inicializan los objetos necesarios.
     */
    @BeforeEach
    void setUp() {
        // Inicializa los mocks
        MockitoAnnotations.openMocks(this);

        // Configura un usuario con un rol para las pruebas
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        List<GrantedAuthority> authorities = Collections.singletonList(authority);
        userDetails = new User("testUser", "password", authorities);
    }

    /**
     * Prueba para verificar que el método getToken() genera un token.
     */
    @Test
    void testGetToken() {
        String token = jwtService.getToken(userDetails);
        // Verifica que el token no sea nulo
        assertNotNull(token);
    }

    /**
     * Prueba para verificar que el método getUsernameFromToken() extrae el nombre de usuario del token.
     */
    @Test
    void testGetUsernameFromToken() {
        String token = jwtService.getToken(userDetails);
        String username = jwtService.getUsernameFromToken(token);
        // Verifica que el nombre de usuario extraído coincida con el esperado
        assertEquals("testUser", username);
    }

    /**
     * Prueba para verificar que el método isTokenValid() valida correctamente el token.
     */
    @Test
    void testIsTokenValid() {
        String token = jwtService.getToken(userDetails);
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        // Verifica que el token sea válido
        assertTrue(isValid);
    }

//    /**
//     * Prueba para verificar que el método getAllClaims() obtiene todos los claims del token.
//     */
//    @Test
//    void testGetAllClaims() {
//        String token = jwtService.getToken(userDetails);
//        Claims claims = jwtService.getAllClaims(token);
//        // Verifica que los claims no sean nulos y que el sujeto sea correcto
//        assertNotNull(claims);
//        assertEquals("testUser", claims.getSubject());
//    }

    /**
     * Prueba para verificar que el método getClaim() extrae un claim específico del token.
     */
    @Test
    void testGetClaim() {
        String token = jwtService.getToken(userDetails);
        String username = jwtService.getClaim(token, Claims::getSubject);
        // Verifica que el nombre de usuario extraído coincida con el esperado
        assertEquals("testUser", username);
    }

    /**
     * Prueba para verificar que el método isTokenExpired() determina correctamente si el token ha expirado.
     */
//    @Test
//    void testIsTokenExpired() {
//        String token = jwtService.getToken(userDetails);
//        boolean isExpired = jwtService.isTokenExpired(token);
//        // Verifica que el token no esté expirado
//        assertFalse(isExpired);
//    }

    /**
     * Prueba para verificar que el método getExpiration() obtiene la fecha de expiración del token.
     */
//    @Test
//    void testGetExpiration() {
//        String token = jwtService.getToken(userDetails);
//        Date expirationDate = jwtService.getExpiration(token);
//        // Verifica que la fecha de expiración no sea nula
//        assertNotNull(expirationDate);
//    }
}
