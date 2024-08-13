package fr.traqueur.modernfactions.api.events;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class UserChunkMoveEvent extends UserEvent {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final Chunk from;
    private final Chunk to;

    public UserChunkMoveEvent(FactionsPlugin plugin, @NotNull Player who, Chunk from, Chunk to) {
        super(plugin, who);
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
