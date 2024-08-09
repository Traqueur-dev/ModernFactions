package fr.traqueur.modernfactions.api.factions;

import fr.traqueur.modernfactions.api.dto.FactionDTO;
import fr.traqueur.modernfactions.api.storage.Data;

import java.util.UUID;

public interface Faction extends Data<FactionDTO> {

    String getName();

    String getDescription();

    UUID getLeader();

    void setName(String name);

    void setDescription(String description);

    void setLeader(UUID leader);

    boolean isWilderness();

}
