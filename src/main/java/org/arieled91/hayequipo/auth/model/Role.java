package org.arieled91.hayequipo.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.arieled91.hayequipo.common.AbstractEntity;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Collection;
import java.util.Set;

@Entity
public class Role extends AbstractEntity{

    private static final long serialVersionUID = 5633612503495481765L;
    private String name = null;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = null;

    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    @ManyToMany
    private @NotNull Set<Privilege> privileges = Set.of();

    public Role() {
    }

    // GETTERS & SETTERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public @NotNull Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(@NotNull Set<Privilege> privileges) {
        this.privileges = privileges;
    }
}
