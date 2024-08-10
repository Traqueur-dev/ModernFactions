package fr.traqueur.modernfactions.api.platform.paper.listeners;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.Faction;
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

    private final UsersManager usersManager;

    public PaperChatListener(FactionsPlugin plugin) {
        this.usersManager = plugin.getManager(UsersManager.class);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {

        event.renderer((player, sourceDisplayName, message, audience) -> {
            User data = this.usersManager.getUser(player).orElseThrow(() -> new IllegalStateException("User not found"));
            Faction faction = data.getFaction();
            String prefix = data.getRole().prefix();
            Optional<UUID> uuidOpt = audience.get(Identity.UUID);
            //for audience get good color with futur relation
            return Component.text(prefix+faction.getName() + " ", NamedTextColor.GREEN)
                    .append(sourceDisplayName.color(NamedTextColor.WHITE))
                    .append(Component.text(" : ", NamedTextColor.WHITE))
                    .append(message.color(NamedTextColor.WHITE));
        });
    }
}
