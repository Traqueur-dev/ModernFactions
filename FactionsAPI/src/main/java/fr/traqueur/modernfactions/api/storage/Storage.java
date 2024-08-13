package fr.traqueur.modernfactions.api.storage;

import java.util.List;
import java.util.UUID;

public interface Storage {

    void createTable(String table);

    <DTO> void save(String table, UUID id, DTO data);

    <DTO> DTO get(String table, UUID id, Class<DTO> clazz);

    <DTO> List<DTO> values(String table, Class<DTO> clazz);

    void delete(String table, UUID id);

    <DTO> List<DTO> where(String tableName, Class<DTO> clazz, String[] key, String[] content);

    default <DTO> List<DTO> where(String tableName, Class<DTO> clazz, String key, String content) {
        return this.where(tableName, clazz, new String[] {key}, new String[] {content});
    }

    boolean isDebug();

    void onEnable();

    void onDisable();
}
