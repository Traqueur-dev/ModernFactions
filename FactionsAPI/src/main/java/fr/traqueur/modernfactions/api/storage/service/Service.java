package fr.traqueur.modernfactions.api.storage.service;

import fr.maxlego08.sarah.Column;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.storage.Data;
import fr.traqueur.modernfactions.api.storage.NotLoadable;
import fr.traqueur.modernfactions.api.storage.Storage;
import fr.traqueur.modernfactions.api.storage.cache.Cache;
import fr.traqueur.modernfactions.api.storage.cache.ConcurrentCache;
import fr.traqueur.modernfactions.api.users.User;

import java.lang.reflect.Field;
import java.util.*;

public abstract class Service<T extends Data<DTO>, DTO> {

    public static final Set<Service<?,?>> REGISTERY = new HashSet<>();

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

    public Optional<T> get(UUID id, Class<DTO> clazz) {
        Optional<T> optional = this.cache.get(id);
        if (optional.isPresent()) {
            return optional;
        }

        T value = this.deserialize(this.storage.get(table, id, clazz));
        if(value != null) {
            this.cache.add(value);
        }
        return  Optional.ofNullable(value);
    }

    public void save(T data) {
        this.cache.add(data);
        this.storage.save(table, data.getId(), data.toDTO());
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

    public List<T> values(Class<DTO> clazz) {
        this.storage.values(table, clazz).forEach(dto -> {
            T value = this.deserialize(dto);
            this.cache.add(value);
        });
        return new ArrayList<>(this.cache.values());
    }

    public Cache<T> getCache() {
        return cache;
    }

    public String getTable() {
        return this.table;
    }


    public abstract T deserialize(DTO dto);


}
