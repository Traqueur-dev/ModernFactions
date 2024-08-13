package fr.traqueur.modernfactions.relations;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.dto.RelationDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.relations.Relation;
import fr.traqueur.modernfactions.api.relations.RelationsManager;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import fr.traqueur.modernfactions.api.storage.service.Service;

public class FRelationsManager implements RelationsManager {

    private final FactionsPlugin plugin;
    private final Service<Relation, RelationDTO> service;


    public FRelationsManager(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.service = new RelationsService(plugin, TABLE_NAME);
    }

    @Override
    public FactionsPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public RelationsType getRelationBetween(Faction faction, Faction value) {
        if(faction.getId().equals(value.getId())) {
            return RelationsType.OWN;
        }

        return this.service.where(new String[] {"faction_emitter", "faction_receiver"}, new String[] {faction.getName(), value.getName()})
                .stream()
                .findFirst()
                .map(Relation::getRelation)
                .orElse(this.service.where(new String[] {"faction_emitter", "faction_receiver"}, new String[] {value.getName(), faction.getName()})
                        .stream()
                        .findFirst()
                        .map(Relation::getRelation)
                        .orElse(RelationsType.NEUTRAL));
    }
}
