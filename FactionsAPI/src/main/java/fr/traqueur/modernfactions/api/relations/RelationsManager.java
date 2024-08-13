package fr.traqueur.modernfactions.api.relations;

import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.managers.Manager;

public interface RelationsManager extends Manager {

    String TABLE_NAME = "relations";

    RelationsType getRelationBetween(Faction faction, Faction value);

}
