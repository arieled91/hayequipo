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

    private static final long serialVersionUID = -5091912854447609619L;

    private String description = "";

    private @NotNull LocalDateTime dateTime = null;

    @ManyToOne(cascade = CascadeType.ALL)
    private @Nullable Location location = null;

    private @NotNull Integer capacity = null;

    private @NotNull Status status = Status.OPEN;

    @ManyToMany
    @JoinTable(
            name = "players",
            joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "player_id", referencedColumnName = "id"))
    private Set<Player> players = null;

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

    public @Nullable Location getLocation() {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status!=null ? status : Status.OPEN;
    }

    public void close(){
        setStatus(Status.CLOSED);
    }

    public void sortPlayers(){
        final Comparator<Player> playerType = comparing(player -> player.getType().getOrder());
        final Comparator<Player> joinDate = comparing(Player::getCreationTime);
        final Comparator<Player> order = comparing(Player::getRosterOrder, nullsFirst(naturalOrder()));

        final Set<Player> sorted = getPlayers().stream()
                .sorted(order.thenComparing(playerType.thenComparing(joinDate)))
                .collect(Collectors.toSet());

        players = savePlayersOrder(sorted);
    }

    public boolean isFull(){
        return getPlayers().size() >= getCapacity();
    }

    public enum Status{
        OPEN, CLOSED
    }

    @JsonIgnore
    private Set<Player> savePlayersOrder(Set<Player> playersToSave){
        int index = 0;
        for (final Player player : playersToSave) {
            player.setRosterOrder(index++);
        }

        return playersToSave;
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
