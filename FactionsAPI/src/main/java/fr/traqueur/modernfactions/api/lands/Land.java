package fr.traqueur.modernfactions.api.lands;

import fr.traqueur.modernfactions.api.dto.LandDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.storage.Data;
import org.bukkit.Chunk;
import org.bukkit.World;

public interface Land extends Data<LandDTO> {

    World getWorld();

    void setFaction(Faction faction);

    Faction getFaction();

    int getX();

    int getZ();

    default Chunk getChunk() {
        return this.getWorld().getChunkAt(this.getX(), this.getZ());
    }

}
