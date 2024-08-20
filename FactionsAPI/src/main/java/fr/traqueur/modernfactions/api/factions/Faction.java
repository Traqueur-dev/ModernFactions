package fr.traqueur.modernfactions.api.factions;

import fr.traqueur.modernfactions.api.dto.FactionDTO;
import fr.traqueur.modernfactions.api.relations.RelationWish;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import fr.traqueur.modernfactions.api.storage.Data;
import fr.traqueur.modernfactions.api.users.User;

import java.util.Optional;
import java.util.Set;
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

    boolean hasInvitation(User user);

    Set<PendingInvitation> getInvitations();

    void broadcast(String message);

    void inviteUser(User invited);

    void addMember(User user);

    void removeMember(User user);
}
