package fr.traqueur.factions;

import fr.traqueur.factions.api.FPlayer;
import fr.traqueur.factions.api.FactionsPlugin;
import org.bukkit.entity.Player;

public record MFPlayer(FactionsPlugin plugin, Player player) implements FPlayer {

    @Override
    public void sendMessage(String message) {
        plugin.getMessageUtils().sendMessage(player, message);
    }
}
