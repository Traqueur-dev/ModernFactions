package fr.traqueur.modernfactions.relations;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.dto.RelationDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.relations.Relation;
import fr.traqueur.modernfactions.api.relations.RelationsManager;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import fr.traqueur.modernfactions.api.storage.service.Service;

import java.util.Optional;

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
    public Relation createRelation(Faction emitter, Faction receiver, RelationsType type) {
        return new FRelation(emitter, receiver, type);
    }

    @Override
    public void addRelation(Relation relation) {
        this.service.add(relation);
    }

    @Override
    public void removeRelation(Relation relation) {
        this.service.delete(relation);
    }

    @Override
    public Optional<Relation> getRelation(Faction emitter, Faction receiver) {
        Optional<Relation> opt = this.service.where(new String[] {"faction_emitter", "faction_receiver"}, new String[] {emitter.getName(), receiver.getName()})
                .stream()
                .findFirst();

        if(opt.isEmpty()) {
            return this.service.where(new String[] {"faction_emitter", "faction_receiver"}, new String[] {receiver.getName(), emitter.getName()})
                    .stream()
                    .findFirst();
        }

        return opt;
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

    @Override
    public void deleteRelationWithFaction(Faction faction) {
        this.service.where("faction_emitter", faction.getName()).forEach(this.service::delete);
        this.service.where("faction_receiver", faction.getName()).forEach(this.service::delete);
    }
}
