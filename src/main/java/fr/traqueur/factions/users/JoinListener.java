package fr.traqueur.factions.users;

import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.users.User;
import fr.traqueur.factions.api.users.UsersManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final FactionsPlugin plugin;
    private final UsersManager usersManager;

    public JoinListener(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.usersManager = plugin.getManager(UsersManager.class);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = this.usersManager.loadOrCreateUser(player);
        user.sendMessage("<rainbow>Bienvenue sur le serveur!");
    }

}
