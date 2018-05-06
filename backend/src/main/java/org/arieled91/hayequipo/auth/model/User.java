package org.arieled91.hayequipo.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.arieled91.hayequipo.common.AbstractEntity;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "`user`", schema = "auth")
public class User extends AbstractEntity {

    private static final long serialVersionUID = -9078135951670183508L;

    private @NotEmpty @NotNull String firstName = "";

    private @NotEmpty @NotNull String lastName = "";

    private @NotEmpty @NotNull String email = "";

    @JsonIgnore
    private @NotEmpty @NotNull String password = "";

    @JsonIgnore
    private boolean enabled;

    @JsonIgnore
    private boolean tokenExpired;

    @ManyToMany
    @JoinTable(
            schema = "auth",
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @JsonIgnore
    private @NotNull Set<Role> roles = new HashSet<>();

    public User() {}

    public @NotNull String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotNull String firstName) {
        this.firstName = firstName;
    }

    public @NotNull String getLastName() {
        return lastName;
    }

    public void setLastName(@NotNull String lastName) {
        this.lastName = lastName;
    }

    public @NotNull String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public @NotNull String getPassword() {
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

    public void setRoles(@NotNull Set<Role> roles) {
        this.roles = roles;
    }

    @JsonIgnore
    public @NotNull String getUsername(){
        return getEmail();
    }

    public Set<String> getPrivileges(){
        return getRoles().stream()
                .flatMap(role -> role.getPrivileges().stream().map(Privilege::getName))
                .collect(Collectors.toSet());
    }
}