package edu.eci.cvds.task_back.Domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Generación automática del ID
    //@Column(columnDefinition = "string")
    private String id;
    private String userName;
    private String email;
    private String passwd;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> tasks;
    public User(String userName, String email, String passwd) {
        this.userName = userName;
        this.email = email;
        this.passwd = passwd;
    }
    public User(){

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = userName;
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
