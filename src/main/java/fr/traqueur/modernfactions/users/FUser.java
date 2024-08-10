package fr.traqueur.modernfactions.users;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.dto.UserDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.factions.roles.Role;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.factions.roles.RolesConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FUser implements User {

    private final FactionsPlugin plugin;
    private final UUID uuid;
    private final String name;
    private String role;
    private UUID factionId;

    public FUser(FactionsPlugin plugin, UUID uuid, String name, UUID faction, String role) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.name = name;
        this.factionId = faction;
        this.role = role;
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
        return new UserDTO(this.uuid, this.name, this.factionId, this.role);
    }

    @Override
    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(this.uuid);
        plugin.getMessageUtils().sendMessage(player, message);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
