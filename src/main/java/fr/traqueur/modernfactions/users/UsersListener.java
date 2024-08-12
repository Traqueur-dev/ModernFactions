package fr.traqueur.modernfactions.users;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.events.UserChunkMoveEvent;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionPersistentDataType;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.lands.LandsManager;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import fr.traqueur.modernfactions.configurations.RolesConfiguration;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;

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
        user.sendMessage("<rainbow>Bienvenue sur le serveur!");
    }

    @EventHandler
    public void onChunkMove(UserChunkMoveEvent event) {
        Chunk to = event.getTo();
        Chunk from = event.getFrom();
        User user = event.getUser();
        PersistentDataContainer fromContainer = from.getPersistentDataContainer();
        PersistentDataContainer toContainer = to.getPersistentDataContainer();
        Faction fromFaction = fromContainer.getOrDefault(LandsManager.LAND_OWNER_KEY, FactionPersistentDataType.INSTANCE, this.factionsManager.getWilderness());
        Faction toFaction = toContainer.getOrDefault(LandsManager.LAND_OWNER_KEY, FactionPersistentDataType.INSTANCE, this.factionsManager.getWilderness());
        if(!fromFaction.getId().equals(toFaction.getId())) {
            landsManager.sendNotification(user, toFaction);
        }
    }

}
