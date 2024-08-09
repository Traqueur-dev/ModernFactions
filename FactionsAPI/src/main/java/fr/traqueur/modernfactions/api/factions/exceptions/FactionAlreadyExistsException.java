package fr.traqueur.modernfactions.api.factions.exceptions;

public class FactionAlreadyExistsException extends Exception {

    public FactionAlreadyExistsException() {
        super("Faction already exists.");
    }
}
