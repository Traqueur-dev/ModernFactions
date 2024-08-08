package fr.traqueur.factions.api.storage.cache;

import fr.traqueur.factions.api.storage.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface Cache<T extends Data> {

    Map<Class<?>, Cache<?>> REGISTERY = new HashMap<>();

    static <R extends Data> void registerCache(Class<R> clazz, Cache<R> cache) {
        REGISTERY.put(clazz, cache);
    }

    static <R extends Data> Cache<R> getCache(Class<R> clazz) {
        return (Cache<R>) REGISTERY.get(clazz);
    }

    void addData(T object);

    void remove(UUID id);

    Optional<T> get(UUID id);

    void scheduleCacheEviction(UUID id, T object);

    default void add(T object) {
        this.addData(object);
        this.scheduleCacheEviction(object.getId(), object);
    }

}
