package fr.traqueur.modernfactions.api.storage.cache;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.storage.Data;
import fr.traqueur.modernfactions.api.storage.service.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ConcurrentCache<T extends Data<?>> implements Cache<T> {

    private final FactionsPlugin plugin;
    private final Map<UUID, T> elements;
    private final Service<T, ?> service;

    public ConcurrentCache(FactionsPlugin plugin, Service<T, ?> service) {
        this.plugin = plugin;
        this.service = service;
        this.elements = new ConcurrentHashMap<>();
    }

    @Override
    public void addData(T object) {
        this.elements.put(object.getId(), object);
    }

    @Override
    public void remove(UUID id) {
        this.elements.remove(id);
    }

    @Override
    public Optional<T> get(UUID id) {
        return Optional.ofNullable(this.elements.getOrDefault(id, null));
    }

    @Override
    public List<T> values() {
        return new ArrayList<>(this.elements.values());
    }

    @Override
    public void scheduleCacheEviction(UUID id, T object) {
        if(!plugin.isEnabled()) {
            return;
        }
        this.plugin.getScheduler().runLaterAsync(() -> {
            this.plugin.getStorage().save(this.service.getTable(), id, object.toDTO());
            this.remove(id);
        }, 15, TimeUnit.MINUTES);
    }
}
