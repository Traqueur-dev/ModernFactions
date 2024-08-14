package fr.traqueur.modernfactions.api.events;

import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class UserChunkMoveEvent extends UserEvent {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final Chunk from;
    private final Chunk to;

    public UserChunkMoveEvent(@NotNull User who, Chunk from, Chunk to) {
        super(who);
        this.from = from;
        this.to = to;
    }

    public Chunk getFrom() {
        return from;
    }

    public Chunk getTo() {
        return to;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
