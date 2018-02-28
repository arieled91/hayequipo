package org.arieled91.hayequipo.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.arieled91.hayequipo.common.AbstractEntity;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.*;

@Entity
public class Game extends AbstractEntity{

    private String description = "";

    @NotNull private LocalDateTime dateTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @Nullable
    private Location location;

    @NotNull private Integer capacity;

    @NotNull private GameStatus status = GameStatus.OPEN;

    @ManyToMany
    @JoinTable(
            name = "players",
            joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "player_id", referencedColumnName = "id"))
    private Set<Player> players;

    public Game() {}

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Nullable
    public Location getLocation() {
        return location;
    }

    public void setLocation(@Nullable Location location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public void close(){
        setStatus(GameStatus.CLOSED);
    }

    public void sortPlayers(){
        Comparator<Player> playerType = comparing(player -> player.getType().getOrder());
        Comparator<Player> joinDate = comparing(Player::getCreationTime);
        Comparator<Player> order = comparing(Player::getRosterOrder, nullsFirst(naturalOrder()));

        final Set<Player> sorted = getPlayers().stream()
                .sorted(order.thenComparing(playerType.thenComparing(joinDate)))
                .collect(Collectors.toSet());

        players = savePlayersOrder(sorted);
    }

    public boolean isFull(){
        return getPlayers().size() >= getCapacity();
    }

    @JsonIgnore
    private Set<Player> savePlayersOrder(Set<Player> players){
        int index = 0;
        for (Player player : players) {
            player.setRosterOrder(index++);
        }

        return players;
    }

    @Override
    public String toString() {
        return "Game{" +
                "description='" + description + '\'' +
                ", dateTime=" + dateTime +
                ", location=" + location +
                ", capacity=" + capacity +
                '}';
    }
}
