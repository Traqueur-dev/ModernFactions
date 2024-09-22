package fr.traqueur.modernfactions.users;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.events.UserChunkMoveEvent;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.lands.LandsManager;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class UsersListener implements Listener {

    private final FactionsPlugin plugin;
    private final UsersManager usersManager;
    private final FactionsManager factionsManager;
    private final LandsManager landsManager;

    public UsersListener(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.usersManager = plugin.getManager(UsersManager.class);
        this.factionsManager = plugin.getManager(FactionsManager.class);
        this.landsManager = plugin.getManager(LandsManager.class);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = this.usersManager.loadOrCreateUser(player);
        //TODO : remove welcome message
        user.sendMessage("<rainbow>Bienvenue sur le serveur!");
        PersistentDataContainer playerContainer = player.getPersistentDataContainer();
        String factionId = playerContainer.get(UsersManager.FACTION_KEY, PersistentDataType.STRING);
        if (factionId != null && !factionId.equals(user.getFaction().getId().toString())) {
            user.sendMessage(Messages.KICK_OR_DISBAND_MESSAGE.translate());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = this.usersManager.getUser(player).orElseThrow(() -> new IllegalStateException("User not found"));
        PersistentDataContainer playerContainer = player.getPersistentDataContainer();
        playerContainer.set(UsersManager.FACTION_KEY, PersistentDataType.STRING, user.getFaction().getId().toString());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        User user = this.usersManager.getUser(event.getPlayer()).orElseThrow(() -> new IllegalStateException("User not found"));
        if (event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }
        if(this.usersManager.cancelTeleportation(user)) {
            user.sendMessage(Messages.HOME_TELEPORT_ERROR_MESSAGE.translate());
        }
    }

    @EventHandler
    public void onChunkMove(UserChunkMoveEvent event) {
        Chunk to = event.getTo();
        Chunk from = event.getFrom();
        User user = event.getUser();
        Faction fromFaction = this.landsManager.getLandOwner(from);
        Faction toFaction = this.landsManager.getLandOwner(to);
        if(!fromFaction.getId().equals(toFaction.getId())) {
            landsManager.sendNotification(user, toFaction);
        }
    }

}
