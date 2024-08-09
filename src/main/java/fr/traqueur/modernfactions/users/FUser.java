package fr.traqueur.modernfactions.users;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.dto.UserDTO;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
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
    public UserDTO toDTO() {
        return new UserDTO(this.uuid, this.factionId);
    }

    @Override
    public void setFaction(UUID uuid) {
        this.factionId = uuid;
    }

    @Override
    public UUID getFaction() {
        return this.factionId;
    }

    @Override
    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(this.uuid);
        plugin.getMessageUtils().sendMessage(player, message);
    }
}
