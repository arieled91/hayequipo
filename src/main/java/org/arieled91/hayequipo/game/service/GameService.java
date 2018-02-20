package org.arieled91.hayequipo.game.service;

import org.arieled91.hayequipo.auth.exception.UserNotFoundException;
import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.auth.service.UserService;
import org.arieled91.hayequipo.game.exception.GameNotFoundException;
import org.arieled91.hayequipo.game.model.Game;
import org.arieled91.hayequipo.game.model.Player;
import org.arieled91.hayequipo.game.model.dto.GuestJoin;
import org.arieled91.hayequipo.game.model.dto.UserJoin;
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
import static org.arieled91.hayequipo.game.model.PlayerType.MODERATOR;
import static org.arieled91.hayequipo.game.model.PlayerType.NORMAL;

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

    public void userJoin(UserJoin join) {
        User user = userService.findActiveUserByMail(join.getEmail()).orElseThrow(UserNotFoundException::new);
        Player player = buildPlayer(user);
        if(player==null) throw new RuntimeException("Player cannot be null");

        join(player, join.getGameId());
    }

    public void guestJoin(GuestJoin join) {
        join(buildPlayer(join), join.getGameId());
    }

    public void join(Player player, long gameId){
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);
        game.getPlayers().add(player);
        gameRepository.save(game);
    }

    private Player buildPlayer(GuestJoin join) {
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
        player.setType(userService.hasPrivilege(user, GAME_PRIORITY) ? MODERATOR : NORMAL);
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
}
