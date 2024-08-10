package fr.traqueur.modernfactions.api.platform.paper;

import fr.traqueur.modernfactions.api.messages.MessageUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class PaperMessageUtils implements MessageUtils {

    private final MiniMessage miniMessage;

    public PaperMessageUtils() {
        miniMessage = MiniMessage.miniMessage();
    }

    @Override
    public void sendMessage(Player player, String message) {
        player.sendMessage(miniMessage.deserialize(message));
    }

    @Override
    public String convertToLegacyFormat(String message) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(miniMessage.deserialize(message));
    }
}
