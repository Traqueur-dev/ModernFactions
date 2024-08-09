package fr.traqueur.modernfactions.api.platform.paper;

import fr.traqueur.modernfactions.api.utils.MessageUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
}
