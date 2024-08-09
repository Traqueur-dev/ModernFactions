package fr.traqueur.modernfactions.api.factions;

import fr.traqueur.modernfactions.api.dto.FactionDTO;
import fr.traqueur.modernfactions.api.storage.Data;

public interface Faction extends Data<FactionDTO> {

    String getName();

}
