package org.arieled91.hayequipo.game.controller;

import org.arieled91.hayequipo.game.model.dto.GuestJoin;
import org.arieled91.hayequipo.game.model.dto.UserJoin;
import org.arieled91.hayequipo.game.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class GameController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final GameService gameService;

    private final MessageSource messages;

    private final JavaMailSender mailSender;

    private final ApplicationEventPublisher eventPublisher;

    private final Environment env;


    @Autowired
    public GameController(GameService gameService, MessageSource messages, JavaMailSender mailSender, ApplicationEventPublisher eventPublisher, Environment env) {
        super();
        this.gameService = gameService;
        this.messages = messages;
        this.mailSender = mailSender;
        this.eventPublisher = eventPublisher;
        this.env = env;
    }

    @RequestMapping(value = "/users/gameJoin", method = RequestMethod.POST)
    public ResponseEntity addUserToGame(final UserJoin userJoinDto) {
        try {
            gameService.userJoin(userJoinDto);
            logger.info("GameController - User joined the game " + userJoinDto);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            logger.error("GameController - Error joining user to game " + userJoinDto, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/guests/gameJoin", method = RequestMethod.POST)
    public ResponseEntity addGuestToGame(final GuestJoin guestJoinDto) {
        try {
            gameService.guestJoin(guestJoinDto);
            logger.info("GameController - User joined the game " + guestJoinDto);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            logger.error("GameController - Error joining user to game " + guestJoinDto, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}