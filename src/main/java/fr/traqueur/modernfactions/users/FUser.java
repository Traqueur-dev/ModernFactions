package fr.traqueur.modernfactions.users;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.dto.UserDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FUser implements User {

    private final FactionsPlugin plugin;
    private final UUID uuid;
    private UUID factionId;

    public FUser(FactionsPlugin plugin, UUID uuid, UUID faction) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.factionId = faction;
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
    public Faction getFaction() {
        return this.plugin.getManager(FactionsManager.class).getFactionById(this.factionId)
                .orElseThrow(() -> new IllegalStateException("Faction not found"));
    }

    @Override
    public UserDTO toDTO() {
        return new UserDTO(this.uuid, this.factionId);
    }

    @Override
    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(this.uuid);
        plugin.getMessageUtils().sendMessage(player, message);
    }

    @Override
    public String getName() {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(this.uuid);
        return offlinePlayer.getName();
    }
}
