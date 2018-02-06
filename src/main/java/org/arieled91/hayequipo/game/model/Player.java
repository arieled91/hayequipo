package org.arieled91.hayequipo.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.common.AbstractEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
public class Player extends AbstractEntity {

    @NotNull private String firstName = "";
    @Nullable private String lastName;
    @Nullable private String email;
    @NotNull private PlayerType type = PlayerType.GUEST;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    public Player() {}

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
    }

    @NotNull public PlayerType getType() {
        return type;
    }

    public void setType(@NotNull PlayerType type) {
        this.type = type;
    }
}