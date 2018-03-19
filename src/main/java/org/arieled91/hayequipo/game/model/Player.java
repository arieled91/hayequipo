package org.arieled91.hayequipo.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.common.AbstractEntity;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Player extends AbstractEntity {

    private static final long serialVersionUID = 5865761740543149559L;
    private @NotNull String firstName = "";
    private @Nullable String lastName = null;
    private @Nullable String email = null;
    private @NotNull PlayerType type = PlayerType.GUEST;
    private @Nullable Integer rosterOrder = null;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private User user = null;

    public Player() {}

    public String getFirstName() {
        return user == null ? firstName : user.getFirstName();
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public @Nullable String getLastName() {
        return user == null ? lastName : user.getLastName();
    }

    public void setLastName(@Nullable String lastName) {
        this.lastName = lastName;
    }

    public @Nullable String getEmail() {
        return user == null ? email : user.getEmail();
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }

    public PlayerType getType() {
        return type;
    }

    public void setType(PlayerType type) {
        this.type = type;
    }

    public @Nullable Integer getRosterOrder() {
        return rosterOrder;
    }

    public void setRosterOrder(@Nullable Integer rosterOrder) {
        this.rosterOrder = rosterOrder;
    }
}