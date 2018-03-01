package org.arieled91.hayequipo.game.repository;

import org.arieled91.hayequipo.game.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long>{

    @Query("select g from Game g where " +
            "function('year', g.dateTime) = function('year', :date) and " +
            "function('month', g.dateTime) = function('month', :date) and " +
            "function('day', g.dateTime) = function('day', :date) order by g.dateTime")
    List<Game> findByDate(@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);

    List<Game> findByDateTimeBetween(
            @Param("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @Param("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    );

    Page<Game> findByDateTimeGreaterThanOrderByDateTime(
            @Param("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            Pageable pageable
    );
}