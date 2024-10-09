package edu.eci.cvds.task_back.Domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Generación automática del ID
    //@Column(columnDefinition = "string")
    private String id;
    private String username;
    private String email;
    private String passwd;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> tasks;
    public User(String username, String email, String passwd) {
        this.username = username;
        this.email = email;
        this.passwd = passwd;
    }
    public User(){

    }

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

    public String getId(){
        return this.id;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
