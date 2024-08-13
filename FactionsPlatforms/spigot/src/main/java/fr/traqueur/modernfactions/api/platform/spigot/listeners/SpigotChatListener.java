package fr.traqueur.modernfactions.api.platform.spigot.listeners;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.relations.RelationsManager;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SpigotChatListener implements Listener {

    private final FactionsPlugin plugin;
    private final UsersManager usersManager;
    private final RelationsManager relationsManager;

    public SpigotChatListener(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.usersManager = plugin.getManager(UsersManager.class);
        this.relationsManager = plugin.getManager(RelationsManager.class);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        User data = this.usersManager.getUser(player).orElseThrow(() -> new IllegalStateException("User not found"));
        Faction faction = data.getFaction();
        String prefix = data.getRole().prefix();

        event.setCancelled(true);
        event.getRecipients().stream().map(usersManager::getUser).forEach(userOpt -> {
            if(userOpt.isEmpty()) {
               throw new IllegalStateException("User not found");
            }
            User user = userOpt.get();
            RelationsType type = relationsManager.getRelationBetween(faction, user.getFaction());
            String formatted = String.format(event.getFormat(), player.getDisplayName(), event.getMessage());
            user.sendMessage(type.getColor() + prefix + faction.getName() + " <reset>" + formatted);

        });
    }

}
