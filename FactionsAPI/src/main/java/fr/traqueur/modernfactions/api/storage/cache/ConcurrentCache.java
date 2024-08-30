package fr.traqueur.modernfactions.api.storage.cache;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.storage.Data;
import fr.traqueur.modernfactions.api.storage.service.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

public class ConcurrentCache<T extends Data<?>> implements Cache<T> {

    private final Map<UUID, T> elements;
    private final Set<UUID> modifiedKeys;
    private final Service<T, ?> service;

    public ConcurrentCache(FactionsPlugin plugin, Service<T, ?> service) {
        this.service = service;
        this.elements = new ConcurrentHashMap<>();
        this.modifiedKeys = new CopyOnWriteArraySet<>();
        if(!plugin.isEnabled()) {
            return;
        }
        plugin.getScheduler().runLaterAsync(() -> {
            for (UUID modifiedKey : this.modifiedKeys) {
                this.service.save(this.elements.get(modifiedKey));
                this.modifiedKeys.remove(modifiedKey);
            }
        }, 15, TimeUnit.MINUTES);
    }

    @Override
    public void add(T object) {
        this.elements.put(object.getId(), object);
        this.modifiedKeys.add(object.getId());
    }

    @Override
    public void remove(UUID id) {
        this.elements.remove(id);
        this.modifiedKeys.remove(id);
    }

    @Override
    public Optional<T> get(UUID id) {
        Optional<T> optional = Optional.ofNullable(this.elements.getOrDefault(id, null));
        if(optional.isPresent()) {
            this.modifiedKeys.add(id);
        }
        return optional;
    }

    @Override
    public List<T> values() {
        return new ArrayList<>(this.elements.values());
    }

    @Override
    public void tag(T data) {
        this.modifiedKeys.add(data.getId());
    }
}
