package org.arieled91.hayequipo.game.service;

import org.arieled91.hayequipo.auth.exception.UserNotFoundException;
import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.auth.service.UserService;
import org.arieled91.hayequipo.game.exception.GameClosedException;
import org.arieled91.hayequipo.game.exception.GameNotFoundException;
import org.arieled91.hayequipo.game.model.Game;
import org.arieled91.hayequipo.game.model.GameStatus;
import org.arieled91.hayequipo.game.model.Player;
import org.arieled91.hayequipo.game.model.dto.JoinRequest;
import org.arieled91.hayequipo.game.repository.GameRepository;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.arieled91.hayequipo.auth.model.PrivilegeType.GAME_PRIORITY;
import static org.arieled91.hayequipo.game.model.PlayerType.NORMAL;
import static org.arieled91.hayequipo.game.model.PlayerType.VIP;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final UserService userService;

    @Autowired
    public GameService(GameRepository gameRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    public void userJoin(Long gameId) {
        final User user = userService.getCurrentUser().orElseThrow(UserNotFoundException::new);
        userJoin(user, gameId);
    }

    public void userJoin(User user, Long gameId) {
        Player player = buildPlayer(user);
        if(player==null) throw new RuntimeException("Player cannot be null");

        join(player, gameId);
    }

    public void commonJoin(JoinRequest request) {
        final User currentUser = userService.getCurrentUser().orElse(null);
        final User userJoin = userService.findActiveUserByMail(request.getEmail()).orElse(null);

        //If an user is trying to add another user to the game
        // only users can add other users to the game
        if(currentUser!=null && userJoin!=null){
            userJoin(userJoin, request.getGameId());
            return;
        }

        //the player is a guest
        join(buildPlayer(request), request.getGameId());
    }

    public void join(Player player, long gameId){
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);
        validateJoin(player, game);
        game.getPlayers().add(player);
        gameRepository.save(game);
    }

    private void validateJoin(Player player, Game game){
        if(game.getStatus() == GameStatus.CLOSED) throw new GameClosedException();

        if(player.getFirstName()==null) throw new RuntimeException("Player firstName cannot be null");
    }

    private void validateClose(Game game){
        if(game.getStatus() == GameStatus.CLOSED) throw new GameClosedException();
    }

    private Player buildPlayer(JoinRequest join) {
        Player player = new Player();
        player.setEmail(join.getEmail());
        player.setFirstName(join.getFirstName());
        player.setLastName(join.getLastName());
        return player;
    }

    public List<Player> listPlayersOrdered(Game game){
        Comparator<Player> playerType = Comparator.comparing(player -> player.getType().getOrder());
        Comparator<Player> joinDate = Comparator.comparing(Player::getCreationTime);

        return game.getPlayers().stream()
                .sorted(playerType.thenComparing(joinDate))
                .collect(Collectors.toList());
    }

    private Player buildPlayer(User user) {
        Player player = new Player();
        player.setUser(user);
        player.setType(userService.hasPrivilege(user, GAME_PRIORITY) ? VIP : NORMAL);
        return player;
    }

    public List<Game> findByDate(@Nullable LocalDate date){
        return date !=null ? gameRepository.findByDate(date) : List.of();
    }

    public boolean isCurrentUserJoined(Game game) {
        return userService.getCurrentUser().map(user -> isUserInGame(user, game)).orElse(false);
    }

    public static boolean isUserInGame(User user, Game game){
        return game.getPlayers().stream()
                .filter(p -> p.getUser() != null && p.getUser().getId().equals(user.getId()))
                .count() > 0;
    }

    public Page<Game> listNextGames(Pageable pageable){
        return gameRepository.findByDateTimeGreaterThanOrderByDateTime(LocalDateTime.now(), pageable);
    }

    public Game addGame(Game newGame){
        return gameRepository.save(newGame);
    }

    public Game close(Long gameId){
        final Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);
        validateClose(game);
        game.close();
        return game;
    }
}
