package fr.traqueur.factions.api.storage.service;

import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.storage.Data;
import fr.traqueur.factions.api.storage.Storage;
import fr.traqueur.factions.api.storage.cache.Cache;
import fr.traqueur.factions.api.storage.cache.ConcurrentCache;
import fr.traqueur.factions.api.utils.FactionsLogger;

import java.util.*;

public abstract class Service<T extends Data> {

    public static final Set<Service<?>> REGISTERY = new HashSet<>();

    protected final FactionsPlugin plugin;
    protected final Cache<T> cache;
    protected final Storage storage;
    protected final String table;

    public Service(FactionsPlugin plugin, String table) {
        this.plugin = plugin;
        this.cache = new ConcurrentCache<>(plugin,this);
        this.storage = plugin.getStorage();
        this.table = table;
        this.storage.createTable(table);
        REGISTERY.add(this);
    }

    public Optional<T> get(UUID id) {
        Optional<T> optional = this.cache.get(id);
        if (optional.isPresent()) {
            FactionsLogger.success("Cached value");
            return optional;
        }

        T value = this.deserialize(this.storage.get(table,id));
        if(value != null) {
            FactionsLogger.success("Cached added");
            this.cache.add(value);
        }
        return  Optional.ofNullable(value);
    }

    public void save(T data) {
        this.cache.add(data);
        this.storage.save(table, data.getId(), this.serialize(data));
    }

    public void saveAll() {
        for (T value : this.cache.values()) {
            this.save(value);
        }
    }

    public void delete(T data) {
        this.cache.remove(data.getId());
        this.storage.delete(table, data.getId());
    }

    public String getTable() {
        return this.table;
    }

    public abstract Map<String, Object> serialize(T data);

    public abstract T deserialize(Map<String, Object> map);


}
