package fr.traqueur.factions.users;

import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.users.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public record FUser(FactionsPlugin plugin, UUID uuid) implements User {

    public FUser(FactionsPlugin plugin, Map<String, Object> objectMap) {
        this(plugin, UUID.fromString((String) objectMap.get("unique_id")));
    }

    @Override
    public UUID getId() {
        return this.uuid;
    }

    @Override
    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(this.uuid);
        plugin.getMessageUtils().sendMessage(player, message);
    }
}
