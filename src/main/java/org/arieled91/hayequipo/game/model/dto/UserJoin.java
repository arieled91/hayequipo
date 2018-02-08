package org.arieled91.hayequipo.game.model.dto;

import java.io.Serializable;

public class UserJoin implements Serializable {

    private String email;
    private long gameId;

    public UserJoin() {
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

        UserJoin that = (UserJoin) o;

        return gameId == that.gameId && email.equals(that.email);
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + (int) (gameId ^ (gameId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "email='" + email + '\'' +
                ", gameId='" + gameId + '\'' +
                '}';
    }
}
