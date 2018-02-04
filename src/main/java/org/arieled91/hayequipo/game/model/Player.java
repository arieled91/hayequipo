package org.arieled91.hayequipo.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.arieled91.hayequipo.auth.model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull private String firstName = "";
    @Nullable private String lastName;
    @Nullable private String email;
    @Transient private boolean guest = true;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    public Player() {}

    // GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull public String getFirstName() {
        return user == null ? firstName : user.getFirstName();
    }

    public void setFirstName(@NotNull String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return user == null ? lastName : user.getLastName();
    }

    public void setLastName(@Nullable String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return user == null ? email : user.getEmail();
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(@NotNull User user) {
        this.user = user;
        guest = false;
    }

    public boolean isGuest() {
        return guest;
    }
}