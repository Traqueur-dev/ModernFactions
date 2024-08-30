package fr.traqueur.modernfactions.lands;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.dto.LandDTO;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.lands.Land;
import fr.traqueur.modernfactions.api.storage.service.Service;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

public class LandService extends Service<Land, LandDTO> {

    public LandService(FactionsPlugin plugin, String tableName) {
        super(plugin, LandDTO.class, tableName);
    }

    @Override
    public Land deserialize(@Nullable LandDTO landDTO) {
        FactionsManager factionsManager = this.plugin.getManager(FactionsManager.class);
        return landDTO == null ? null :
                new FLand(landDTO.unique_id(),
                        Bukkit.getWorld(landDTO.world()),
                        landDTO.x(),
                        landDTO.z(),
                        factionsManager
                                .getFaction(landDTO.faction())
                                .orElseThrow(() -> new IllegalStateException("Faction not found")));
    }
}
