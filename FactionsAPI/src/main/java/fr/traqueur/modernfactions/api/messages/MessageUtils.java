package fr.traqueur.modernfactions.api.messages;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public interface MessageUtils {

    MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    void sendMessage(Player player, String message);

    default String convertToLegacyFormat(String message) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(MINI_MESSAGE.deserialize(message));
    }

    void sendActionBar(Player player, String message);

    void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut);
}
