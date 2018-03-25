package org.arieled91.hayequipo.auth.model;

import org.arieled91.hayequipo.common.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "privilege", schema = "auth")
public class Privilege extends AbstractEntity {

    private static final long serialVersionUID = 6384509731809068814L;

    private String name = null;

    public Privilege() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
