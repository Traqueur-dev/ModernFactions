package fr.traqueur.modernfactions.factions;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.dto.FactionDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.storage.service.Service;

public class FactionService extends Service<Faction, FactionDTO> {

    public FactionService(FactionsPlugin plugin, String table) {
        super(plugin, FactionDTO.class, table);
    }



    @Override
    public Faction deserialize(FactionDTO dto) {
        FactionsManager manager = plugin.getManager(FactionsManager.class);
        return dto == null ? null :
                new FFaction(plugin, dto.unique_id(),
                        dto.name(),
                        dto.description(),
                        dto.leader(),
                        dto.nbLands() == null ? 0 : dto.nbLands(),
                        manager.deserializeLocation(dto.home()));
    }


}
