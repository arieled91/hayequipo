package org.arieled91.hayequipo.auth.model.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.util.Base64Utils;

import java.io.Serializable;
import java.util.Objects;

public class AuthToken implements Serializable {
    private String username;
    private String token;

    public AuthToken() {}

    public AuthToken(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return Objects.equals(username, authToken.username) &&
                Objects.equals(token, authToken.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, token);
    }

    public String toJson(){
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String toBase64(){
        return Base64Utils.encodeToString(toJson().getBytes());
    }
}
