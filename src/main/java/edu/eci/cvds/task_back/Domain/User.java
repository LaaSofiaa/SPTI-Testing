package edu.eci.cvds.task_back.Domain;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Generación automática del ID
    //@Column(columnDefinition = "string")
    private String id;
    private String username;
    private String email;
    private String passwd;
    @Enumerated(EnumType.STRING)
    Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> tasks;
    public User(String username, String email, String passwd) {
        this.username = username;
        this.email = email;
        this.passwd = passwd;
    }
    public User(String username, String email, String passwd, Role role) {
        this.username = username;
        this.email = email;
        this.passwd = passwd;
        this.role = role;
    }
    public User(){

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return passwd;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    public void setId(String id) {
        this.id = id;
    }
}
