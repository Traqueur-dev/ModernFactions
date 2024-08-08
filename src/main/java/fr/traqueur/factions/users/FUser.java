package fr.traqueur.factions.users;

import fr.traqueur.factions.api.users.User;
import fr.traqueur.factions.api.FactionsPlugin;
import org.bukkit.entity.Player;

public record FUser(FactionsPlugin plugin, Player player) implements User {

    @Override
    public int getId() {
        return player.getUniqueId().hashCode();
    }

    @Override
    public void sendMessage(String message) {
        plugin.getMessageUtils().sendMessage(player, message);
    }
}
