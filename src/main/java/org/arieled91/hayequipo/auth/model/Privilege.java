package org.arieled91.hayequipo.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.arieled91.hayequipo.common.AbstractEntity;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Privilege extends AbstractEntity {

    private String name;

    @NotNull @JsonIgnore @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles = Set.of();

    public Privilege() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(@NotNull Set<Role> roles) {
        this.roles = roles;
    }
}
