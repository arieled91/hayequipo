package org.arieled91.hayequipo.auth.repository;


import org.arieled91.hayequipo.auth.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndEnabledIsTrue(@Param("email") String email);
    Page<User> findAllByEnabled(@Param("enabled") boolean enabled, Pageable pageable);
    User findByEmail(@Param("email") String email);

    @Query("from User u where u.enabled = :enabled and concat(trim(lower(u.email)),' ', trim(lower(u.firstName)),' ', trim(lower(u.lastName))) like lower(concat('%',:query,'%'))")
    Page<User> findAllByEnabledAndQuery(@Param("enabled") boolean enabled, @Param("query") String query, Pageable pageable);
}
