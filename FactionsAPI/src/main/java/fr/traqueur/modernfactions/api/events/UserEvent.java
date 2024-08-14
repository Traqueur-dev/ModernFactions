package fr.traqueur.modernfactions.api.events;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public abstract class UserEvent extends PlayerEvent {

    protected final User user;

    public UserEvent(@NotNull User who) {
        super(who.getPlayer());
        this.user = who;
    }

    public User getUser() {
        return user;
    }
}
