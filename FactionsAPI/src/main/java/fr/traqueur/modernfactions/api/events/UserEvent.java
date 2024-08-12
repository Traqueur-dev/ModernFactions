package fr.traqueur.modernfactions.api.events;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public abstract class UserEvent extends PlayerEvent {

    protected final FactionsPlugin plugin;
    protected final User user;

    public UserEvent(FactionsPlugin plugin, @NotNull Player who) {
        super(who);
        this.plugin = plugin;
        this.user = plugin.getManager(UsersManager.class).getUser(who).orElseThrow(() -> new IllegalStateException("User not found"));
    }

    public User getUser() {
        return user;
    }
}
