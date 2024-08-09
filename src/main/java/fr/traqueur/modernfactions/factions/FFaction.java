package fr.traqueur.modernfactions.factions;

import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.dto.FactionDTO;

import java.util.UUID;

public class FFaction implements Faction {

    private final String name;
    private final UUID id;

    public FFaction(UUID uuid, String name) {
        this.name = name;
        this.id = uuid;
    }

    public FFaction(String name) {
        this(UUID.randomUUID(), name);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public FactionDTO toDTO() {
        return new FactionDTO(this.id, this.name);
    }
}
