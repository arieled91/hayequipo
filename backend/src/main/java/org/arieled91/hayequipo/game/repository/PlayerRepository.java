package org.arieled91.hayequipo.game.repository;


import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.game.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByUser(User user);
}
