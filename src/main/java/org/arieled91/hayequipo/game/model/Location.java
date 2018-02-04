package org.arieled91.hayequipo.game.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty @NotNull
    private String address = "";
    @Nullable private Long latitude;
    @Nullable private Long longitude;

    public Location() {
    }

    @NotNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NotNull String address) {
        this.address = address;
    }

    @Nullable
    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(@Nullable Long latitude) {
        this.latitude = latitude;
    }

    @Nullable
    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(@Nullable Long longitude) {
        this.longitude = longitude;
    }
}
