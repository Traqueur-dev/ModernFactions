package fr.traqueur.modernfactions.api.platform.spigot;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.messages.MessageUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;

public class SpigotMessageUtils implements MessageUtils {

    private final BukkitAudiences adventure;

    public SpigotMessageUtils(FactionsPlugin plugin) {
        adventure = BukkitAudiences.create(plugin);
    }

    @Override
    public void sendMessage(Player player, String message) {
        adventure.player(player).sendMessage(MINI_MESSAGE.deserialize(message));
    }

    @Override
    public void sendMessage(CommandSender sender, String message) {
        adventure.sender(sender).sendMessage(MINI_MESSAGE.deserialize(message));
    }

    @Override
    public void sendActionBar(Player player, String message) {
        adventure.player(player).sendActionBar(MINI_MESSAGE.deserialize(message));
    }

    @Override
    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        Duration fadeInDuration = Duration.ofSeconds(fadeIn / 20);
        Duration stayDuration = Duration.ofSeconds(stay / 20);
        Duration fadeOutDuration = Duration.ofSeconds(fadeOut / 20);
        Title.Times times = Title.Times.times(fadeInDuration, stayDuration, fadeOutDuration);
        adventure.player(player).showTitle(Title.title(MINI_MESSAGE.deserialize(title), MINI_MESSAGE.deserialize(subtitle), times));
    }


    public void close() {
        adventure.close();
    }
}
