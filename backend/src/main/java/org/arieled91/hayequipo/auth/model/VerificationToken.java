package org.arieled91.hayequipo.auth.model;


import org.arieled91.hayequipo.common.AbstractEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_token", schema = "auth")
public class VerificationToken extends AbstractEntity {

    private static final long serialVersionUID = -9215865487791910405L;

    private static final int EXPIRATION_IN_DAYS = 365;

    private String token = null;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id", foreignKey = @ForeignKey(name = "FK_VERIFY_USER"))
    private User user = null;

    private LocalDateTime expiryDate = null;

    public VerificationToken() {}

    public VerificationToken(final String token) {
        this.token = token;
        expiryDate = calculateExpiryDate(EXPIRATION_IN_DAYS);
    }

    @Deprecated
    public VerificationToken(final String token, final User user) {
        this(token);
        this.user = user;
        expiryDate = calculateExpiryDate(EXPIRATION_IN_DAYS);
    }

    public VerificationToken(final String token, final User user, LocalDateTime expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(final LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Deprecated
    private LocalDateTime calculateExpiryDate(final int days) {
        return LocalDateTime.now().plusDays(days);
    }

    @Deprecated
    public void updateToken(final String newToken) {
        token = newToken;
        expiryDate = calculateExpiryDate(EXPIRATION_IN_DAYS);
    }
    //

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expiryDate == null) ? 0 : expiryDate.hashCode());
        result = prime * result + ((token == null) ? 0 : token.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VerificationToken other = (VerificationToken) obj;
        if (expiryDate == null) {
            if (other.expiryDate != null) {
                return false;
            }
        } else if (!expiryDate.equals(other.expiryDate)) {
            return false;
        }
        if (token == null) {
            if (other.token != null) {
                return false;
            }
        } else if (!token.equals(other.token)) {
            return false;
        }
        if (user == null) {
            return other.user == null;
        } else return user.equals(other.user);
    }

    @Override
    public String toString() {
        return "Token [String=" + token + "]" + "[Expires" + expiryDate + "]";
    }

    public boolean isValid(){
        return getExpiryDate().isBefore(LocalDateTime.now()) && getUser().isEnabled();
    }

}