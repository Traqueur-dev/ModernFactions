package fr.traqueur.modernfactions.api.factions;

import fr.traqueur.modernfactions.api.dto.FactionDTO;
import fr.traqueur.modernfactions.api.relations.RelationWish;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import fr.traqueur.modernfactions.api.storage.Data;

import java.util.Optional;
import java.util.UUID;

public interface Faction extends Data<FactionDTO> {

    String getName();

    String getDescription();

    UUID getLeader();

    void setName(String name);

    void setDescription(String description);

    void setLeader(UUID leader);

    boolean isWilderness();

    boolean isSystem();

    void addRelationWish(Faction emitter, RelationsType type);

    void removeRelationWish(Faction emitter, RelationsType type);

    Optional<RelationWish> getRelationWish(Faction emitter, RelationsType type);

    void broadcast(String message);
}
