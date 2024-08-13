package fr.traqueur.modernfactions.api.relations;

import fr.traqueur.modernfactions.api.dto.RelationDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.storage.Data;

public interface Relation extends Data<RelationDTO> {

    Faction getEmitter();

    Faction getReceiver();

    RelationsType getRelation();

}
