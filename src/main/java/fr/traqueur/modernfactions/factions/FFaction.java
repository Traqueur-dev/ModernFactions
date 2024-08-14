package fr.traqueur.modernfactions.factions;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.dto.FactionDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.relations.RelationWish;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class FFaction implements Faction {

    private final FactionsPlugin plugin;

    private final Set<RelationWish> relationWishes;

    private final UUID id;
    private String name;
    private String description;
    private UUID leader;

    public FFaction(FactionsPlugin plugin, UUID uuid, String name, String description, UUID leader) {
        this.plugin = plugin;
        this.name = name;
        this.id = uuid;
        this.description = description;
        this.leader = leader;
        this.relationWishes = new HashSet<>();
    }

    public FFaction(FactionsPlugin plugin, String name, String description, UUID leader) {
        this(plugin, UUID.randomUUID(), name, description, leader);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public UUID getLeader() {
        return this.leader;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setLeader(UUID uuid) {
        this.leader = uuid;
    }

    @Override
    public boolean isWilderness() {
        return this.plugin.getManager(FactionsManager.class).getWilderness().getId().equals(this.id);
    }

    @Override
    public boolean isSystem() {
        FactionsManager manager = this.plugin.getManager(FactionsManager.class);
        return manager.getWilderness().getId().equals(this.id) || manager.getWarZone().getId().equals(this.id) || manager.getSafeZone().getId().equals(this.id);
    }

    @Override
    public void addRelationWish(Faction emitter, RelationsType type) {
        this.relationWishes.add(new RelationWish(plugin, type, emitter));
    }

    @Override
    public void removeRelationWish(Faction emitter, RelationsType type) {
        this.relationWishes.removeIf(relationWish -> relationWish.emitter().getId().equals(emitter.getId()) && relationWish.type().equals(type));
    }

    @Override
    public Optional<RelationWish> getRelationWish(Faction emitter, RelationsType type) {
        return this.relationWishes.stream().filter(relationWish ->
                relationWish.emitter().getId().equals(emitter.getId())
        && relationWish.type().equals(type)).findFirst();
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public void broadcast(String message) {
        UsersManager usersManager = this.plugin.getManager(UsersManager.class);
        usersManager.getUsersInFaction(this)
                .stream().filter(User::isOnline)
                .forEach(user -> user.sendMessage(message));
    }

    @Override
    public FactionDTO toDTO() {
        return new FactionDTO(this.id, this.name, this.description, this.leader);
    }
}
