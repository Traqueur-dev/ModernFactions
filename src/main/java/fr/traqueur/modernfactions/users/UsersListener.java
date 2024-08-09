package fr.traqueur.modernfactions.users;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UsersListener implements Listener {

    private final UsersManager usersManager;

    public UsersListener(FactionsPlugin plugin) {
        this.usersManager = plugin.getManager(UsersManager.class);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = this.usersManager.loadOrCreateUser(player);
        user.sendMessage("<rainbow>Bienvenue sur le serveur!");
    }

}
