package org.arieled91.hayequipo.auth.repository;

import org.arieled91.hayequipo.auth.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);
}
