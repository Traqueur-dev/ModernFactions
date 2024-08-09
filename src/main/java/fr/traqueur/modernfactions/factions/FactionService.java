package fr.traqueur.modernfactions.factions;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.storage.service.Service;

import java.util.HashMap;
import java.util.Map;

public class FactionService extends Service<Faction> {

    public FactionService(FactionsPlugin plugin, String table) {
        super(plugin, table);
    }

    @Override
    public Faction deserialize(Map<String, Object> map) {
        return map == null ? null : new FFaction(map);
    }
}
