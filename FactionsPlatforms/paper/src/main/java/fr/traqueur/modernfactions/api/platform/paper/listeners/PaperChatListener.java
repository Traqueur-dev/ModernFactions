package fr.traqueur.modernfactions.api.platform.paper.listeners;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.chatmode.ChatMode;
import fr.traqueur.modernfactions.api.chatmode.UserScopeMessageException;
import fr.traqueur.modernfactions.api.messages.MessageUtils;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class PaperChatListener implements Listener {

    private final FactionsPlugin plugin;
    private final UsersManager usersManager;

    public PaperChatListener(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.usersManager = plugin.getManager(UsersManager.class);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        User data = this.usersManager.getUser(player).orElseThrow(() -> new IllegalStateException("User not found"));
        ChatMode mode = data.getChatMode();

        event.viewers().removeIf(viewer -> {
            if (viewer.get(Identity.UUID).isPresent()) {
                UUID uuid = viewer.get(Identity.UUID).get();
                User target = this.usersManager.getUserById(uuid)
                        .orElseThrow(() -> new IllegalStateException("User not found"));
                try {
                    mode.format(this.plugin, data, target, "");
                    return false;
                } catch (UserScopeMessageException ignored) {
                    return true;
                }
            }
            return false;
        });

        event.renderer((sender, sourceDisplayName, message, audience) -> {
            if (audience.get(Identity.UUID).isEmpty()) {
                // This is a console message
                return Component.text(data.getFaction().getName() + " ")
                        .append(sourceDisplayName)
                        .append(Component.text(": "))
                        .append(message);

            }
            UUID uuid = audience.get(Identity.UUID).get();
            User target = this.usersManager.getUserById(uuid)
                    .orElseThrow(() -> new IllegalStateException("User not found"));
            try {
                String messageFormatted
                        = mode.format(this.plugin, data, target, MessageUtils.MINI_MESSAGE.serialize(message));
                return MessageUtils.MINI_MESSAGE.deserialize(messageFormatted);
            } catch (UserScopeMessageException ignored) {
                return Component.empty();
            }
        });

    }
}
