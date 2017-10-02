package org.dbpedia.ontologytracker.webservice.user;

import org.springframework.data.annotation.Id;
//import org.springframework.security.crypto.keygen.KeyGenerators;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

public class WebServiceUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String password;

    private String token;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*public WebServiceUser() {}

    public WebServiceUser(String username) {
        this.username = username;
        this.token = new String(KeyGenerators.secureRandom(24).generateKey());
    }*/

}
