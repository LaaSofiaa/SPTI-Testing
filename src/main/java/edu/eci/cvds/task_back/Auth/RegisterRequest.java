package edu.eci.cvds.task_back.Auth;


/**
 * Clase que representa una solicitud de registro de usuario.
 * Esta clase contiene la información necesaria para crear una nueva cuenta de usuario,
 * incluyendo el nombre de usuario, el correo electrónico y la contraseña.
 */
public class RegisterRequest {
    String username;
    String email;
    String passwd;

    // Getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
