package fr.traqueur.modernfactions.factions;

import fr.maxlego08.sarah.Column;
import fr.traqueur.modernfactions.api.factions.Faction;

import java.util.Map;
import java.util.UUID;

public class FFaction implements Faction {

    @Column(value = "name")
    private final String name;
    @Column(value = "unique_id")
    private final UUID id;

    public FFaction(Map<String, Object> map) {
        this.name = (String) map.get("name");
        this.id = UUID.fromString((String) map.get("unique_id"));
    }

    public FFaction(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
