package org.arieled91.hayequipo.auth.model;

import org.arieled91.hayequipo.common.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Privilege extends AbstractEntity {

    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles;

    public Privilege() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
