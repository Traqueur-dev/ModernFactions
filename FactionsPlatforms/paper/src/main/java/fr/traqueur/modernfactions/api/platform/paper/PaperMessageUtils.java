package fr.traqueur.modernfactions.api.platform.paper;

import fr.traqueur.modernfactions.api.messages.MessageUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class PaperMessageUtils implements MessageUtils {

    @Override
    public void sendMessage(Player player, String message) {
        player.sendMessage(MINI_MESSAGE.deserialize(message));
    }
}
