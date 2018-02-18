package org.arieled91.hayequipo.game.controller;

import org.arieled91.hayequipo.game.model.Game;
import org.arieled91.hayequipo.game.model.dto.GuestJoin;
import org.arieled91.hayequipo.game.model.dto.UserJoin;
import org.arieled91.hayequipo.game.service.GameService;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/api/games")
public class GameController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(value = "/userJoin", method = RequestMethod.POST)
    public ResponseEntity userJoin(final UserJoin userJoinDto) {
        try {
            gameService.userJoin(userJoinDto);
            logger.info("GameController - User joined the game " + userJoinDto);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            logger.error("GameController - Error joining user to game " + userJoinDto, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/guestJoin", method = RequestMethod.POST)
    public ResponseEntity guestJoin(final GuestJoin guestJoinDto) {
        try {
            gameService.guestJoin(guestJoinDto);
            logger.info("GameController - User joined the game " + guestJoinDto);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            logger.error("GameController - Error joining user to game " + guestJoinDto, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<List<Game>> findByDate(final String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            return ResponseEntity.ok(gameService.findByDate(parsedDate));
        }catch (Exception e){
            logger.error("GameController - Error finding game by date:" + date, e);
            return ResponseEntity.badRequest().body(List.of());
        }
    }
}