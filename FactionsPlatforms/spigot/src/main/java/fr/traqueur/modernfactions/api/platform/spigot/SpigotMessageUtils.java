package fr.traqueur.modernfactions.api.platform.spigot;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.messages.MessageUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class SpigotMessageUtils implements MessageUtils {

    private final BukkitAudiences adventure;

    public SpigotMessageUtils(FactionsPlugin plugin) {
        adventure = BukkitAudiences.create(plugin);
    }

    @Override
    public void sendMessage(Player player, String message) {
        adventure.player(player).sendMessage(MINI_MESSAGE.deserialize(message));
    }

    public void close() {
        adventure.close();
    }
}
