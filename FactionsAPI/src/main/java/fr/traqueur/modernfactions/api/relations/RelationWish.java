package fr.traqueur.modernfactions.api.relations;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.Faction;

public record RelationWish(FactionsPlugin plugin, RelationsType type, Faction emitter) {

    public Relation conclude(Faction receiver) {
        return plugin.getManager(RelationsManager.class).createRelation(emitter, receiver, type);
    }
}
