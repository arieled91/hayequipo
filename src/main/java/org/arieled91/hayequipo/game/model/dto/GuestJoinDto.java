package org.arieled91.hayequipo.game.model.dto;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class GuestJoinDto implements Serializable{

    @NotNull private String firstName = "";
    private String lastName;
    private String email;
    private long gameId;

    public GuestJoinDto() {
    }

    @NotNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotNull String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GuestJoinDto that = (GuestJoinDto) o;

        if (gameId != that.gameId) return false;
        if (!firstName.equals(that.firstName)) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        return email.equals(that.email);
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + email.hashCode();
        result = 31 * result + (int) (gameId ^ (gameId >>> 32));
        return result;
    }
}
