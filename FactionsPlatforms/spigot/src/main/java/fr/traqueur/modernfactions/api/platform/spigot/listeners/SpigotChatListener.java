package fr.traqueur.modernfactions.api.platform.spigot.listeners;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SpigotChatListener implements Listener {

    private final UsersManager usersManager;

    public SpigotChatListener(FactionsPlugin plugin) {
        this.usersManager = plugin.getManager(UsersManager.class);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        User data = this.usersManager.getUser(player).orElseThrow(() -> new IllegalStateException("User not found"));
        Faction faction = data.getFaction();
        String prefix = data.getRole().prefix();
        event.setFormat(prefix + faction.getName() + " " + event.getFormat());
    }

}
