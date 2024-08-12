package fr.traqueur.modernfactions.api.platform.paper;

import fr.traqueur.modernfactions.api.messages.MessageUtils;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import java.time.Duration;

public class PaperMessageUtils implements MessageUtils {

    @Override
    public void sendMessage(Player player, String message) {
        player.sendMessage(MINI_MESSAGE.deserialize(message));
    }

    @Override
    public void sendActionBar(Player player, String message) {
        player.sendActionBar(MINI_MESSAGE.deserialize(message));
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        Duration fadeInDuration = Duration.ofSeconds(fadeIn / 20);
        Duration stayDuration = Duration.ofSeconds(stay / 20);
        Duration fadeOutDuration = Duration.ofSeconds(fadeOut / 20);
        Title.Times times = Title.Times.times(fadeInDuration, stayDuration, fadeOutDuration);
        player.showTitle(Title.title(MINI_MESSAGE.deserialize(title), MINI_MESSAGE.deserialize(subtitle), times));
    }
}
