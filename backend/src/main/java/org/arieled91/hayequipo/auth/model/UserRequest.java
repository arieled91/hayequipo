package org.arieled91.hayequipo.auth.model;

import java.io.Serializable;

public class UserRequest implements Serializable {

    private static final long serialVersionUID = 6450145987158279962L;

    private String firstName = null;

    private String lastName = null;

    private String email = null;

    private String password = null;


    public UserRequest() {
    }

    public String getFirstName() {
        return firstName;
    }

    public UserRequest setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRequest setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}