package edu.eci.cvds.task_back.exception;

public class SecurityException extends Exception{
    public static final String USUARIO_SIN_ENCONTRAR = "Usuario no encontrado";
    public static final String EMAIL_USE = "The email has already been used";
    public static final String USER_REQUIRED = "User is required!";
    public static final String EMAIL_REQUIRED = "Email is required!";
    public static final String PASSWORD_REQUIRED = "Password is required!";
    public static final String PASSWORD_PARAMETERS = "Password must be at least 8 characters long, with one uppercase letter, one number, and one special character!";
    public static final String USERNAME_TAKEN = "Username is already taken!";
    public SecurityException(String message){
        super(message);
    }
}
