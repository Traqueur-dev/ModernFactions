package fr.traqueur.modernfactions.api.events;

import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class FactionDisbandEvent extends UserEvent implements Cancellable {

    private static HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final Faction faction;
    private boolean cancel;

    public FactionDisbandEvent(@NotNull User who, Faction faction) {
        super(who);
        this.faction = faction;
    }

    public Faction getFaction() {
        return faction;
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
