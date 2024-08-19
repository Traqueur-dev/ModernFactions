package fr.traqueur.modernfactions.api.events;

import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class FactionCreateEvent extends UserEvent implements Cancellable {

    private static HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final String factionName;
    private boolean cancel;

    public FactionCreateEvent(@NotNull User who, String factionName) {
        super(who);
        this.factionName = factionName;
    }

    public String getFactionName() {
        return factionName;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancel = b;
    }
}
