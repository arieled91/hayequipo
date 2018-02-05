package org.arieled91.hayequipo.auth.model;

import org.arieled91.hayequipo.common.AbstractEntity;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.Set;

@Entity
public class User extends AbstractEntity {

    @NotEmpty @NotNull private String firstName = "";
    @NotEmpty @NotNull private String lastName = "";
    @NotEmpty @NotNull private String email = "";
    @NotEmpty @NotNull private String password = "";
    private boolean enabled;
    private boolean tokenExpired;

    @ManyToMany @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    public User() {}

    @NotNull public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotNull String firstName) {
        this.firstName = firstName;
    }

    @NotNull public String getLastName() {
        return lastName;
    }

    public void setLastName(@NotNull String lastName) {
        this.lastName = lastName;
    }

    @NotNull public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    @NotNull public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isTokenExpired() {
        return tokenExpired;
    }

    public void setTokenExpired(boolean tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}