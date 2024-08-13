package fr.traqueur.modernfactions.api.relations;

import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.managers.Manager;

public interface RelationsManager extends Manager {

    String TABLE_NAME = "relations";

    Relation createRelation(Faction emitter, Faction receiver, RelationsType type);

    void addRelation(Relation relation);

    RelationsType getRelationBetween(Faction faction, Faction value);

}
