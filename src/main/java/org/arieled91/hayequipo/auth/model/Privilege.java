package org.arieled91.hayequipo.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.arieled91.hayequipo.common.AbstractEntity;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "privilege", schema = "auth")
public class Privilege extends AbstractEntity {

    private static final long serialVersionUID = 6384509731809068814L;
    private String name = null;

    @ManyToMany(mappedBy = "privileges")
    @JsonIgnore
    private @NotNull Set<Role> roles = Set.of();

    public Privilege() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @NotNull Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(@NotNull Set<Role> roles) {
        this.roles = roles;
    }
}
