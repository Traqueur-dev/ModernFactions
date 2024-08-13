package fr.traqueur.modernfactions.relations;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.dto.RelationDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.relations.Relation;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import fr.traqueur.modernfactions.api.storage.service.Service;

public class RelationsService extends Service<Relation, RelationDTO> {

    public RelationsService(FactionsPlugin plugin, String table) {
        super(plugin, RelationDTO.class, table);
    }

    @Override
    public Relation deserialize(RelationDTO relationDTO) {
        if(relationDTO == null) {
            return null;
        }
        FactionsManager factionsManager = this.plugin.getManager(FactionsManager.class);
        Faction emitter = factionsManager.getFaction(relationDTO.faction_emitter()).orElseThrow(() -> new IllegalStateException("Faction emitter not found"));
        Faction receiver = factionsManager.getFaction(relationDTO.faction_receiver()).orElseThrow(() -> new IllegalStateException("Faction receiver not found"));
        return new FRelation(relationDTO.unique_id(), emitter, receiver, RelationsType.valueOf(relationDTO.relation()));
    }
}
