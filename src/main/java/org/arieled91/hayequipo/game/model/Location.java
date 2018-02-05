package org.arieled91.hayequipo.game.model;

import org.arieled91.hayequipo.common.AbstractEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Entity
public class Location extends AbstractEntity {

    @NotEmpty @NotNull
    private String address = "";
    @Nullable private Long latitude;
    @Nullable private Long longitude;
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
