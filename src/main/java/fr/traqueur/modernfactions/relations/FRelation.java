package fr.traqueur.modernfactions.relations;

import fr.traqueur.modernfactions.api.dto.RelationDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.relations.Relation;
import fr.traqueur.modernfactions.api.relations.RelationsType;

import java.util.UUID;

public class FRelation implements Relation {

    private final UUID uuid;
    private final Faction emitter;
    private final Faction receiver;
    private final RelationsType relation;

    public FRelation(UUID uuid, Faction emitter, Faction receiver, RelationsType relation) {
        this.uuid = uuid;
        this.emitter = emitter;
        this.receiver = receiver;
        this.relation = relation;
    }

    public FRelation(Faction emitter, Faction receiver, RelationsType relation) {
        this(UUID.randomUUID(), emitter, receiver, relation);
    }

    @Override
    public Faction getEmitter() {
        return this.emitter;
    }

    @Override
    public Faction getReceiver() {
        return this.receiver;
    }

    @Override
    public RelationsType getRelation() {
        return this.relation;
    }

    @Override
    public UUID getId() {
        return this.uuid;
    }

    @Override
    public RelationDTO toDTO() {
        return new RelationDTO(this.uuid, this.emitter.getName(), this.receiver.getName(), this.relation.name());
    }
}
