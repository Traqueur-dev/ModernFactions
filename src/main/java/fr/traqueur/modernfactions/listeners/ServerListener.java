package fr.traqueur.modernfactions.listeners;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class ServerListener implements Listener {

    private final FactionsPlugin plugin;
    private final UsersManager usersManager;
    private final FactionsManager factionsManager;

    public ServerListener(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.usersManager = plugin.getManager(UsersManager.class);
        this.factionsManager = plugin.getManager(FactionsManager.class);
    }

    @EventHandler
    public void onEnable(PluginEnableEvent event) {
        this.plugin.getScheduler().runNextTick(t ->  {
            for (Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
                this.usersManager.loadOrCreateUser(onlinePlayer);
            }
        });
        this.factionsManager.loadFactions();
    }

}
