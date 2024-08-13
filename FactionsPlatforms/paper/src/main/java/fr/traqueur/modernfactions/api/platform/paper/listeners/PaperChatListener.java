package fr.traqueur.modernfactions.api.platform.paper.listeners;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.relations.RelationsManager;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;
import java.util.UUID;

public class PaperChatListener implements Listener {

    private final FactionsPlugin plugin;
    private final RelationsManager relationsManager;
    private final UsersManager usersManager;

    public PaperChatListener(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.usersManager = plugin.getManager(UsersManager.class);
        this.relationsManager = plugin.getManager(RelationsManager.class);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer((player, sourceDisplayName, message, audience) -> {
            User data = this.usersManager.getUser(player).orElseThrow(() -> new IllegalStateException("User not found"));
            Faction faction = data.getFaction();
            String prefix = data.getRole().prefix();
            Optional<UUID> uuidOpt = audience.get(Identity.UUID);
            RelationsType type = RelationsType.NEUTRAL;
            if (uuidOpt.isPresent()) {
                UUID uuid = uuidOpt.get();
                User target = this.usersManager.getUserById(uuid).orElseThrow(() -> new IllegalStateException("User not found"));
                type = this.relationsManager.getRelationBetween(faction, target.getFaction());
            }

            Component component = plugin.getMessageUtils().convertToComponent(type.getColor() + prefix+faction.getName() + " ");
            return component
                    .append(sourceDisplayName.color(NamedTextColor.WHITE))
                    .append(Component.text(" : ", NamedTextColor.WHITE))
                    .append(message.color(NamedTextColor.WHITE));
        });
    }
}
