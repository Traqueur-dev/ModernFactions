package fr.traqueur.modernfactions.listeners;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.events.UserChunkMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    private final FactionsPlugin plugin;

    public MoveListener(FactionsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Chunk chunkFrom = event.getFrom().getChunk();
        Chunk chunkTo = event.getTo().getChunk();

        if(chunkFrom.equals(chunkTo)) {
            return;
        }

        UserChunkMoveEvent customEvent = new UserChunkMoveEvent(plugin, player, chunkFrom, chunkTo);
        Bukkit.getPluginManager().callEvent(customEvent);
    }
}
