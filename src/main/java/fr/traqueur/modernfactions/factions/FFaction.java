package fr.traqueur.modernfactions.factions;

import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.dto.FactionDTO;

import java.util.UUID;

public class FFaction implements Faction {

    private final String name;
    private final UUID id;

    public FFaction(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
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
