package org.arieled91.hayequipo.game.service;

import org.arieled91.hayequipo.auth.exception.UserNotFoundException;
import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.auth.service.UserService;
import org.arieled91.hayequipo.game.exception.GameNotFoundException;
import org.arieled91.hayequipo.game.model.Game;
import org.arieled91.hayequipo.game.model.Player;
import org.arieled91.hayequipo.game.model.dto.GuestJoinDto;
import org.arieled91.hayequipo.game.model.dto.UserJoinDto;
import org.arieled91.hayequipo.game.repository.GameRepository;
import org.arieled91.hayequipo.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final UserService userService;
    private final PlayerRepository playerRepository;

    @Autowired
    public GameService(GameRepository gameRepository, UserService userService, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.playerRepository = playerRepository;
    }

    public void userJoin(@Valid UserJoinDto playerDto) {
        User user = userService.findActiveUserByMail(playerDto.getEmail()).orElseThrow(UserNotFoundException::new);
        Player player = playerRepository.findByUser(user);
        if(player==null) throw new RuntimeException("Player cannot be null");

        join(player, playerDto.getGameId());
    }

    public void guestJoin(@Valid GuestJoinDto joinDto) {
        join(buildPlayer(joinDto), joinDto.getGameId());
    }

    public void join(Player player, long gameId){
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);
        game.getPlayers().add(player);
        gameRepository.save(game);
    }

    private Player buildPlayer(@Valid GuestJoinDto joinDto) {
        Player player = new Player();
        player.setEmail(joinDto.getEmail());
        player.setFirstName(joinDto.getFirstName());
        player.setLastName(joinDto.getLastName());
        return player;
    }
}
