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

        createGamesIfEmpty();

        alreadySetup = true;
    }

    @Transactional
    public void createGamesIfEmpty() {
        if(gameRepository.count() <= 0){
            Game game1 = new Game();
            game1.setDateTime(LocalDateTime.now().plusMinutes(1));
            game1.setDescription("Futbol 1");
            game1.setLocation(findAnyOrCreateLocation());
            gameRepository.save(game1);

            Game game2 = new Game();
            game2.setDateTime(LocalDateTime.now().plusHours(1));
            game2.setDescription("Futbol 2");
            game2.setLocation(findAnyOrCreateLocation());
            gameRepository.save(game2);

            Game game3 = new Game();
            game3.setDateTime(LocalDateTime.now().plusHours(24));
            game3.setDescription("Futbol 3");
            game3.setLocation(findAnyOrCreateLocation());
            gameRepository.save(game3);

            Game game4 = new Game();
            game4.setDateTime(LocalDateTime.now().plusHours(48));
            game4.setDescription("Futbol 4");
            game4.setLocation(findAnyOrCreateLocation());
            gameRepository.save(game4);
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