package org.arieled91.hayequipo.game.repository;

import org.arieled91.hayequipo.game.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long>{
    List<Game> findByDate(LocalDateTime date);
}