package edu.eci.cvds.task_back.Auth;


/**
 * Clase que representa una solicitud de inicio de sesión para un usuario.
 * Esta clase contiene la información necesaria para autenticar a un usuario,
 * incluyendo el nombre de usuario y la contraseña.
 */
public class LoginRequest {
    String username;
    String passwd;

    // Getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
