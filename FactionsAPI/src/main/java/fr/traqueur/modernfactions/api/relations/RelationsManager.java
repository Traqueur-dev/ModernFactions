package fr.traqueur.modernfactions.api.relations;

import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.managers.Manager;

import java.util.Optional;

public interface RelationsManager extends Manager {

    String TABLE_NAME = "relations";

    Relation createRelation(Faction emitter, Faction receiver, RelationsType type);

    void addRelation(Relation relation);

    void removeRelation(Relation relation);

    Optional<Relation> getRelation(Faction emitter, Faction receiver);

    RelationsType getRelationBetween(Faction faction, Faction value);

}
