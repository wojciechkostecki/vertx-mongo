package org.example.model;

import io.vertx.core.json.JsonObject;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;


public class User {

    private UUID id;

    private String login;

    private String password;

    public User(String login, String password) {
        this.id = UUID.randomUUID();
        this.login = login;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public JsonObject toJson() {
        return new JsonObject()
                .put("_id", id != null ? id.toString() : null)
                .put("login", login)
                .put("password", password);
    }
}
