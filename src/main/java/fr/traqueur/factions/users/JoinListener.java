package fr.traqueur.factions.users;

import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.users.User;
import fr.traqueur.factions.api.users.UsersManager;
import fr.traqueur.factions.api.utils.FactionsLogger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class JoinListener implements Listener {

    private final FactionsPlugin plugin;
    private final UsersManager usersManager;

    public JoinListener(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.usersManager = plugin.getManager(UsersManager.class);
    }

    @EventHandler
    public void onPlayerJoin(PluginEnableEvent event) {
        for (Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
            User user = this.usersManager.loadOrCreateUser(onlinePlayer);
            user.sendMessage("<rainbow>Bienvenue sur le serveur!");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = this.usersManager.loadOrCreateUser(player);
        user.sendMessage("<rainbow>Bienvenue sur le serveur!");
    }

}
