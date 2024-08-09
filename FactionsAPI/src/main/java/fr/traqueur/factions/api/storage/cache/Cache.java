package fr.traqueur.factions.api.storage.cache;

import fr.traqueur.factions.api.storage.Data;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Cache<T extends Data> {

    void addData(T object);

    void remove(UUID id);

    Optional<T> get(UUID id);

    List<T> values();

    void scheduleCacheEviction(UUID id, T object);

    default void add(T object) {
        this.addData(object);
        this.scheduleCacheEviction(object.getId(), object);
    }

}
