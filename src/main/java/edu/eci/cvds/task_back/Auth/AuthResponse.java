package edu.eci.cvds.task_back.Auth;

/**
 * Clase que representa la respuesta de autenticación.
 * Contiene el token JWT y el ID del usuario asociado a la sesión.
 */
public class AuthResponse {
    /**
     * Constructor vacío de la clase AuthResponse.
     */
    public AuthResponse(){}

    /**
     * Constructor de la clase AuthResponse que inicializa el token.
     * @param token El token JWT que se devolverá al cliente.
     */
    public AuthResponse(String token){
        this.token = token;
    }

    // Getters y setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId;
}
