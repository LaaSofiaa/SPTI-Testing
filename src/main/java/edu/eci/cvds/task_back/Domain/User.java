package edu.eci.cvds.task_back.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Generación automática del ID
    //@Column(columnDefinition = "string")
    private String id;
    private String name;
    private String email;
    private String passwd;
    public User(String name, String email, String passwd) {
        this.name = name;
        this.email = email;
        this.passwd = passwd;
    }
    public User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
