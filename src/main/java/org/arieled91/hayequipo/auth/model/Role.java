package org.arieled91.hayequipo.auth.model;

import org.arieled91.hayequipo.common.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Collection;
import java.util.Set;

@Entity
public class Role extends AbstractEntity{

    private String name;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @ManyToMany
    @JoinTable(
        name = "roles_privileges",
        joinColumns = @JoinColumn(
            name = "role_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
            name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges;

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

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }
}
