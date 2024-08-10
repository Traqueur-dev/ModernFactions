package fr.traqueur.modernfactions.api.factions.exceptions;

import fr.traqueur.modernfactions.api.factions.Faction;

public class FactionAlreadyExistsException extends Exception {

    private final Faction faction;

    public FactionAlreadyExistsException(Faction faction) {
        super("Faction already exists.");
        this.faction = faction;
    }

    public Faction getFaction() {
        return faction;
    }
}
