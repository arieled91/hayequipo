package org.arieled91.hayequipo.game.task;

import org.arieled91.hayequipo.game.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GameTask {

    private final GameService gameService;

    @Autowired
    public GameTask(GameService gameService){
        this.gameService = gameService;
    }

    @Scheduled(fixedRate = ONE_MINUTE)
    private void closeOldGame(){
        gameService.closePastGames();

        logger.info("GameTask closeOldGame - task run OK");
    }

    private static final int ONE_MINUTE = 60000;
    private final Logger logger = LoggerFactory.getLogger(getClass());

}
