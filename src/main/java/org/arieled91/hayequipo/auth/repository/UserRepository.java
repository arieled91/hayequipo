package org.arieled91.hayequipo.auth.repository;


import org.arieled91.hayequipo.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
