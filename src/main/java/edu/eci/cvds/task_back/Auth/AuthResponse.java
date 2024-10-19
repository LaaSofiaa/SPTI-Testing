package edu.eci.cvds.task_back.Auth;

public class AuthResponse {
    public AuthResponse(){

    }
    public AuthResponse(String token){
        this.token = token;
    }
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
