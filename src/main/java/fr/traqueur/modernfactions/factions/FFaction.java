package fr.traqueur.modernfactions.factions;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.dto.FactionDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.factions.PendingInvitation;
import fr.traqueur.modernfactions.api.relations.RelationWish;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import fr.traqueur.modernfactions.configurations.RolesConfiguration;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class FFaction implements Faction {

    private final FactionsPlugin plugin;

    private final Set<PendingInvitation> pendingInvitations;
    private final Set<RelationWish> relationWishes;

    private final UUID id;
    private String name;
    private String description;
    private UUID leader;
    private int nbLands;
    private Location home;

    public FFaction(FactionsPlugin plugin, UUID uuid, String name, String description, UUID leader, int nbLands, Location home) {
        this.plugin = plugin;
        this.name = name;
        this.id = uuid;
        this.description = description;
        this.leader = leader;
        this.relationWishes = new HashSet<>();
        this.pendingInvitations = new HashSet<>();
        this.nbLands = nbLands;
        this.home = home;
    }

    public FFaction(FactionsPlugin plugin, String name, String description, UUID leader) {
        this(plugin, UUID.randomUUID(), name, description, leader, 0, null);
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
    public boolean hasInvitation(User user) {
        return this.pendingInvitations.stream().anyMatch(invitation -> invitation.invited().getId().equals(user.getId()));
    }

    @Override
    public Set<PendingInvitation> getInvitations() {
        return this.pendingInvitations;
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
    public void inviteUser(User invited) {
        this.pendingInvitations.add(new PendingInvitation(invited, System.currentTimeMillis()));
    }

    @Override
    public void addMember(User user) {
        this.pendingInvitations.removeIf(invitation -> invitation.invited().getId().equals(user.getId()));
        user.setFaction(this.id);
        user.setRole(Config.getConfiguration(RolesConfiguration.class).getDefaultRole());
    }

    @Override
    public void removeMember(User user) {
        this.plugin.getManager(FactionsManager.class).getWilderness().addMember(user);
    }

    @Override
    public int getPowers() {
        return this.plugin.getManager(UsersManager.class)
                .getUsersInFaction(this)
                .stream()
                .mapToInt(User::getPower)
                .sum();
    }

    @Override
    public int getLands() {
        return this.nbLands;
    }

    @Override
    public void removeLand() {
        this.nbLands--;
    }

    @Override
    public void addLand() {
        this.nbLands++;
    }

    @Override
    public Optional<Location> getHome() {
        return Optional.ofNullable(this.home);
    }

    @Override
    public void setHome(Location location) {
        this.home = location;
    }

    @Override
    public FactionDTO toDTO() {
        String locationSerialized = this.home == null ? null : this.plugin.getManager(FactionsManager.class).serializeLocation(this.home);
        return new FactionDTO(this.id, this.name, this.description, this.leader, this.nbLands, locationSerialized);
    }
}
