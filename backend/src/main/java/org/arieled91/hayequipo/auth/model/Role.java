package org.arieled91.hayequipo.auth.model;

import org.arieled91.hayequipo.common.AbstractEntity;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role", schema = "auth")
public class Role extends AbstractEntity {

    private static final long serialVersionUID = 5633612503495481765L;
    private String name = null;

    @JoinTable(
            schema = "auth",
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    @ManyToMany
    private @NotNull Set<Privilege> privileges = new HashSet<>();

    public Role() {
    }

    // GETTERS & SETTERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @NotNull Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(@NotNull Set<Privilege> privileges) {
        this.privileges = privileges;
    }
}
