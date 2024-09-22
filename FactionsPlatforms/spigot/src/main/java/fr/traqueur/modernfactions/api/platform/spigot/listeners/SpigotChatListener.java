package fr.traqueur.modernfactions.api.platform.spigot.listeners;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.chatmode.ChatMode;
import fr.traqueur.modernfactions.api.chatmode.UserScopeMessageException;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SpigotChatListener implements Listener {

    private final FactionsPlugin plugin;
    private final UsersManager usersManager;

    public SpigotChatListener(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.usersManager = plugin.getManager(UsersManager.class);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        User data = this.usersManager.getUser(player).orElseThrow(() -> new IllegalStateException("User not found"));

        ChatMode mode = data.getChatMode();

        event.getRecipients().stream().map(usersManager::getUser).forEach(userOpt -> {
            if(userOpt.isEmpty()) {
               throw new IllegalStateException("User not found");
            }
            User target = userOpt.get();
            try {
                String message = mode.format(plugin, data, target, event.getMessage());
                target.sendMessage(message);
            } catch (UserScopeMessageException ignored) {}
        });

        event.getRecipients().clear();
    }

}
