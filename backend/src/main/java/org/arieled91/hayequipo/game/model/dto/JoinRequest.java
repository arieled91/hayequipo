package org.arieled91.hayequipo.game.model.dto;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class JoinRequest implements Serializable{

    private static final long serialVersionUID = 7867190867582929733L;

    @Nullable private String firstName = null;
    @Nullable private String lastName = null;
    @Nullable private String email = null;
    @NotNull private Long gameId = 0L;

    public JoinRequest() {
    }

    @Nullable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotNull String firstName) {
        this.firstName = firstName;
    }

    @Nullable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(@Nullable String lastName) {
        this.lastName = lastName;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @NotNull public Long getGameId() {
        return gameId;
    }

    public void setGameId(@NotNull Long gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "JoinRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gameId=" + gameId +
                '}';
    }
}
