package fr.traqueur.factions.api.platform.spigot;

import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.utils.MessageUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class SpigotMessageUtils implements MessageUtils {

    private final MiniMessage miniMessage;
    private final BukkitAudiences adventure;

    public SpigotMessageUtils(FactionsPlugin plugin) {
        miniMessage = MiniMessage.miniMessage();
        adventure = BukkitAudiences.create(plugin);
    }

    @Override
    public void sendMessage(Player player, String message) {
        adventure.player(player).sendMessage(miniMessage.deserialize(message));
    }

    public void close() {
        adventure.close();
    }
}
