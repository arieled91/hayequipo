package org.arieled91.hayequipo.common;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
public abstract class AuditEntity implements Serializable {

    private LocalDateTime creationTime = LocalDateTime.now();


    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditEntity that = (AuditEntity) o;
        return Objects.equals(creationTime, that.creationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creationTime);
    }
}
