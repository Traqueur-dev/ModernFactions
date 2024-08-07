package fr.traqueur.factions;

import fr.traqueur.factions.api.FactionsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final FactionsPlugin plugin;

    public JoinListener(FactionsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        fr.traqueur.factions.api.FPlayer fPlayer = new MFPlayer(this.plugin, player);
        fPlayer.sendMessage("<rainbow>Bienvenue sur le serveur!");
    }

}
