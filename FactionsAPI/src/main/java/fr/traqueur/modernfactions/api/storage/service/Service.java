package fr.traqueur.modernfactions.api.storage.service;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.storage.Data;
import fr.traqueur.modernfactions.api.storage.Storage;
import fr.traqueur.modernfactions.api.storage.cache.Cache;
import fr.traqueur.modernfactions.api.storage.cache.ConcurrentCache;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public abstract class Service<T extends Data<DTO>, DTO> {

    public static final List<Service<?,?>> REGISTRY = new CopyOnWriteArrayList<>();

    private final Class<DTO> dtoClass;
    protected final FactionsPlugin plugin;
    protected final Cache<T> cache;
    protected final Storage storage;
    protected final String table;

    public Service(FactionsPlugin plugin, Class<DTO> dtoClass, String table) {
        this.plugin = plugin;
        this.dtoClass = dtoClass;
        if(!this.dtoClass.isRecord()) {
            throw new IllegalArgumentException("DTO class must be a record !");
        }
        this.cache = new ConcurrentCache<>(plugin,this);
        this.storage = plugin.getStorage();
        this.table = table;
        this.storage.createTable(table);
        REGISTRY.add(this);
    }

    public Optional<T> get(UUID id) {
        return this.cache.get(id);
    }

    public List<T> where(String key, String content) {
        return this.where(new String[] {key}, new String[]{content});
    }

    public List<T> where(String[] key, String[] content) {
        return this.cache.values().stream().filter(data -> {
            try {
                DTO dto = data.toDTO();
                for (int i = 0; i < key.length; i++) {
                    Field field = dto.getClass().getDeclaredField(key[i]);
                    field.setAccessible(true);
                    if (!field.get(dto).toString().equals(content[i])) {
                        return false;
                    }
                }
                return true;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public void add(T data) {
        this.cache.add(data);
    }

    public void save(T data) {
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

    public List<T> values() {
        return new ArrayList<>(this.cache.values());
    }

    public void loadAll() {
        this.storage.values(table, this.dtoClass).forEach(dto -> {
            T value = this.deserialize(dto);
            this.cache.add(value);
        });
    }

    public String getTable() {
        return this.table;
    }


    public abstract T deserialize(@Nullable DTO dto);
}
