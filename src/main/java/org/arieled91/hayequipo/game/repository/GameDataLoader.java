package org.arieled91.hayequipo.game.repository;

import org.arieled91.hayequipo.game.model.Game;
import org.arieled91.hayequipo.game.model.Location;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
public class GameDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final GameRepository gameRepository;
    private final LocationRepository locationRepository;


    @Autowired
    public GameDataLoader(GameRepository gameRepository, LocationRepository locationRepository) {
        this.gameRepository = gameRepository;
        this.locationRepository = locationRepository;
    }

    // API

    @Override
    @Transactional
    public void onApplicationEvent(@NotNull final ContextRefreshedEvent event) {
        if (alreadySetup) return;

        createGameIfEmpty();

        alreadySetup = true;
    }

    @Transactional
    public void createGameIfEmpty() {
        if(gameRepository.count() <= 0){
            Game game = new Game();
            game.setDateTime(LocalDateTime.now().plusHours(1));
            game.setDescription("Futbol 5 en la cancha");
            game.setLocation(findAnyOrCreateLocation());
            gameRepository.save(game);
        }
    }

    @Transactional
    public Location findAnyOrCreateLocation() {
        if(locationRepository.count() <= 0){
            Location location = new Location();
            location.setAddress("Test Address 123");
            location.setDescription("Test House");
            location.setLatitude(-34.584481);
            location.setLongitude(-58.445352);
            return locationRepository.save(location);
        }
        else return locationRepository.findAll().get(0);
    }


}