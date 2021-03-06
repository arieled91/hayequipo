package org.arieled91.hayequipo.game.controller;

import org.arieled91.hayequipo.game.exception.GameClosedException;
import org.arieled91.hayequipo.game.model.Game;
import org.arieled91.hayequipo.game.model.dto.GameResponse;
import org.arieled91.hayequipo.game.model.dto.JoinRequest;
import org.arieled91.hayequipo.game.service.GameService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/api/controller/games")
public class GameController {

    private final GameService gameService;
    private final MessageSource messages;

    @Autowired
    public GameController(GameService gameService, @Qualifier("messageSource") MessageSource messages) {
        this.gameService = gameService;
        this.messages = messages;
    }

    @RequestMapping(value = "/{id}/join", method = RequestMethod.GET)
    public ResponseEntity<?> userJoin(@PathVariable(value="id") final Long id) {
        try {
            gameService.userJoin(id);
            logger.info("GameController - User joined the game " + id);
            return ResponseEntity.ok().build();
        }catch (final Exception e){
            logger.error("GameController - Error joining user to game " + id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public ResponseEntity<?> join(final @NotNull JoinRequest joinRequest) {
        try {
            gameService.commonJoin(joinRequest);
            logger.info("GameController - User joined the game " + joinRequest);
            return ResponseEntity.ok().build();
        }catch (final Exception e){
            logger.error("GameController - Error joining user to game " + joinRequest, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @RequestMapping(value = "/{id}/remove", method = RequestMethod.GET)
    public ResponseEntity<?> userRemove(@PathVariable(value="id") final Long id) {
        try {
            gameService.userRemove(id);
            logger.info("GameController - User removed from game " + id);
            return ResponseEntity.ok().build();
        }catch (final Exception e){
            logger.error("GameController - Error removing user from game " + id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable(value="id") final Long id) {
        try {
            return gameService.findById(id)
                    .map(game -> ResponseEntity.ok(new GameResponse(game)))
                    .orElse(ResponseEntity.notFound().build());
        }catch (final Exception e){
            logger.error("GameController - Error removing user from game " + id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<List<GameResponse>> findByDate(@RequestParam(required = false) final @Nullable String date) {
        try {
            final List<Game> games = date == null ? gameService.listNextGames(PageRequest.of(0, 20)).getContent() : gameService.findByDate(LocalDate.parse(date));

            return ResponseEntity.ok(games.stream()
                                        .map(game -> new GameResponse(game, gameService.isCurrentUserJoined(game)))
                                        .collect(Collectors.toList()));

        }catch (final Exception e){
            logger.error("GameController - Error finding game by date:" + date, e);
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody final Game game) {
        try {
            return ResponseEntity.ok(new GameResponse(gameService.save(game)));
        }catch (final Exception e){
            logger.error("GameController - Error adding new game: " + game, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @RequestMapping(value = "/{id}/close", method = RequestMethod.GET)
    public ResponseEntity<?> close(@PathVariable Long id, final Locale locale) {
        try {
            return ResponseEntity.ok(gameService.close(id));
        }catch (final GameClosedException e){
            logCloseError(id, e);
            return ResponseEntity.badRequest().body(messages.getMessage("game.closed.error",null,locale));
        }catch (final Exception e){
            logCloseError(id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}/players", method = RequestMethod.GET)
    public ResponseEntity<?> listPlayers(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(gameService.listPlayersOrdered(id));
        }catch (final Exception e){
            logCloseError(id, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void logCloseError(Long id, Exception e){
        logger.error("GameController - Error closing game with id: "+id, e);
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());
}