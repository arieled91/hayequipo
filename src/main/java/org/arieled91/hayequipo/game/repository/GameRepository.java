package org.arieled91.hayequipo.game.repository;

import org.arieled91.hayequipo.game.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long>{
}