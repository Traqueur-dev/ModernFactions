package fr.traqueur.factions.api.storage;

import java.util.Map;
import java.util.UUID;

public interface Storage {

    void createTable(String table);

    void save(String table, UUID id, Map<String, Object> data);

    Map<String, Object> get(String table, UUID id);

    void delete(String table, UUID id);

    void onEnable();

    void onDisable();
}
