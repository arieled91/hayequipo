package org.arieled91.hayequipo.game;


import org.arieled91.hayequipo.auth.OnRegistrationConfirmEvent;
import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.game.model.Player;
import org.arieled91.hayequipo.game.repository.PlayerRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RegistrationConfirmListener implements ApplicationListener<OnRegistrationConfirmEvent> {

    private final PlayerRepository playerRepo;


    @Autowired
    public RegistrationConfirmListener(PlayerRepository playerRepo) {
        this.playerRepo = playerRepo;
    }


    @Override
    public void onApplicationEvent(@NotNull final OnRegistrationConfirmEvent event) {
        playerRepo.save(buildPlayer(event.getUser()));
    }

    private Player buildPlayer(User user) {
        Player player = new Player();
        player.setUser(user);
        return player;
    }

}