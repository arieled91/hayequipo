package org.arieled91.hayequipo.auth.repository;


import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.auth.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);
//
    VerificationToken findByUser(User user);
//
//    Stream<VerificationToken> findAllByExpiryDateLessThan(LocalDateTime dateTime);
//
//    void deleteByExpiryDateLessThan(LocalDateTime dateTime);

    @Modifying
    @Query("delete from VerificationToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
}