package fr.traqueur.modernfactions.lands;

import fr.traqueur.modernfactions.api.dto.LandDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.lands.Land;
import org.bukkit.World;

import java.util.UUID;

public class FLand implements Land {

    private final UUID uuid;
    private final World world;
    private final int x;
    private final int z;
    private Faction faction;


    public FLand(UUID uuid, World world, int x, int z, Faction faction) {
        this.uuid = uuid;
        this.world = world;
        this.x = x;
        this.z = z;
        this.faction = faction;
    }

    public FLand(World world, int x, int z, Faction faction) {
        this(UUID.randomUUID(), world, x, z, faction);
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    @Override
    public Faction getFaction() {
        return this.faction;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getZ() {
        return this.z;
    }

    @Override
    public UUID getId() {
        return this.uuid;
    }

    @Override
    public LandDTO toDTO() {
        return new LandDTO(this.uuid, this.faction.getName(), this.world.getUID(), this.x, this.z);
    }
}
