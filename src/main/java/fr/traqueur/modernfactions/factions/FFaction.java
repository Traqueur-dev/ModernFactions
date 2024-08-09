package fr.traqueur.modernfactions.factions;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.dto.FactionDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;

import java.util.UUID;

public class FFaction implements Faction {

    private final FactionsPlugin plugin;
    private final UUID id;
    private String name;
    private String description;
    private UUID leader;

    public FFaction(FactionsPlugin plugin, UUID uuid, String name, String description, UUID leader) {
        this.plugin = plugin;
        this.name = name;
        this.id = uuid;
        this.description = description;
        this.leader = leader;
    }

    public FFaction(FactionsPlugin plugin, String name, String description, UUID leader) {
        this(plugin, UUID.randomUUID(), name, description, leader);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public UUID getLeader() {
        return this.leader;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setLeader(UUID uuid) {
        this.leader = uuid;
    }

    @Override
    public boolean isWilderness() {
        return this.plugin.getManager(FactionsManager.class).getWilderness().getId().equals(this.id);
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public FactionDTO toDTO() {
        return new FactionDTO(this.id, this.name, this.description, this.leader);
    }
}
