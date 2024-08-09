package fr.traqueur.modernfactions.api.storage;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Storage {

    void createTable(String table);

    void save(String table, UUID id, Map<String, Object> data);

    Map<String, Object> get(String table, UUID id);

    List<Map<String, Object>> values(String table);

    void delete(String table, UUID id);

    void onEnable();

    void onDisable();
}
