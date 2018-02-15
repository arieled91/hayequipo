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
    @Nullable private Double latitude;
    @Nullable private Double longitude;
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
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(@Nullable Double latitude) {
        this.latitude = latitude;
    }

    @Nullable
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(@Nullable Double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
