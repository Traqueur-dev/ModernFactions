package fr.traqueur.modernfactions.users;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.chatmode.ChatMode;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.dto.UserDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.factions.roles.Role;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.configurations.MainConfiguration;
import fr.traqueur.modernfactions.configurations.RolesConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FUser implements User {

    private final FactionsPlugin plugin;
    private final UUID uuid;
    private final String name;
    private ChatMode chatMode;
    private String role;
    private UUID factionId;
    private int power;

    public FUser(FactionsPlugin plugin, UUID uuid, String name, UUID faction, String role, int power, ChatMode chatMode) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.name = name;
        this.factionId = faction;
        this.role = role;
        this.power = power;
        this.chatMode = chatMode;
    }

    @Override
    public UUID getId() {
        return this.uuid;
    }

    @Override
    public void setFaction(UUID uuid) {
        this.factionId = uuid;
    }

    @Override
    public void setRole(Role role) {
        this.role = role.name();
    }

    @Override
    public Faction getFaction() {
        return this.plugin.getManager(FactionsManager.class).getFactionById(this.factionId)
                .orElseThrow(() -> new IllegalStateException("Faction not found"));
    }

    @Override
    public Role getRole() {
        return Config.getConfiguration(RolesConfiguration.class).getRoleByName(this.role);
    }

    @Override
    public boolean isLeader() {
        return this.getRole().name().equals(Config.getConfiguration(RolesConfiguration.class).getMaxPriorityRole().name());
    }

    @Override
    public UserDTO toDTO() {
        return new UserDTO(this.uuid, this.name, this.factionId, this.role, this.power, this.chatMode.name());
    }

    @Override
    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(this.uuid);
        if(player != null)
            plugin.getMessageUtils().sendMessage(player, message);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public Location getLocation() {
        Player player = Bukkit.getPlayer(this.uuid);
        if(player == null) {
            throw new IllegalStateException("Player not found");
        }
        return player.getLocation();
    }

    @Override
    public void sendActionBar(String message) {
        Player player = Bukkit.getPlayer(this.uuid);
        plugin.getMessageUtils().sendActionBar(player, message);
    }

    @Override
    public void sendTitle(String notificationMessage, String subtitle, int fadeIn, int stay, int fadeOut) {
        Player player = Bukkit.getPlayer(this.uuid);
        plugin.getMessageUtils().sendTitle(player, notificationMessage, subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public boolean isOnline() {
        OfflinePlayer player = Bukkit.getOfflinePlayer(this.uuid);
        return player.isOnline();
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    @Override
    public boolean hasFaction() {
        return !this.getFaction().isWilderness();
    }

    @Override
    public int getPower() {
        return this.power;
    }

    @Override
    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public void addPower(int power) {
        this.power += power;
        int maxPower = Config.getConfiguration(MainConfiguration.class).getMaxUserPower();
        this.power = Math.min(this.power, maxPower);
    }

    @Override
    public void removePower(int power) {
        this.power -= power;
        int minPower = Config.getConfiguration(MainConfiguration.class).getMinUserPower();
        this.power = Math.max(this.power, minPower);
    }

    @Override
    public ChatMode getChatMode() {
        return this.chatMode;
    }

    @Override
    public void setChatMode(ChatMode chatMode) {
        this.chatMode = chatMode;
    }
}
