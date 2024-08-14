package fr.traqueur.modernfactions.listeners;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.events.UserChunkMoveEvent;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    private final FactionsPlugin plugin;
    private final UsersManager usersManager;

    public MoveListener(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.usersManager = plugin.getManager(UsersManager.class);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        User user = this.usersManager.getUser(player).orElseThrow(() -> new NullPointerException("User not found"));
        Chunk chunkFrom = event.getFrom().getChunk();
        Chunk chunkTo = event.getTo().getChunk();

        if(chunkFrom.equals(chunkTo)) {
            return;
        }

        UserChunkMoveEvent customEvent = new UserChunkMoveEvent(user, chunkFrom, chunkTo);
        Bukkit.getPluginManager().callEvent(customEvent);
    }
}
