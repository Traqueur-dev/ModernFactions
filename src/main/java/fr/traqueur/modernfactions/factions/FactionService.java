package fr.traqueur.modernfactions.factions;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.storage.service.Service;
import fr.traqueur.modernfactions.api.dto.FactionDTO;

public class FactionService extends Service<Faction, FactionDTO> {

    public FactionService(FactionsPlugin plugin, String table) {
        super(plugin, FactionDTO.class, table);
    }

    @Override
    public Faction deserialize(FactionDTO dto) {
        return dto == null ? null : new FFaction(dto.unique_id(), dto.name());
    }


}
