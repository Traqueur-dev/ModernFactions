package fr.traqueur.modernfactions.api.storage.service;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.storage.Data;
import fr.traqueur.modernfactions.api.storage.Storage;
import fr.traqueur.modernfactions.api.storage.cache.Cache;
import fr.traqueur.modernfactions.api.storage.cache.ConcurrentCache;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Service<T extends Data<DTO>, DTO> {

    public static final Set<Service<?,?>> REGISTERY = new HashSet<>();

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
        REGISTERY.add(this);
    }

    public Optional<T> get(UUID id) {
        Optional<T> optional = this.cache.get(id);
        if (optional.isPresent()) {
            return optional;
        }

        T value = this.deserialize(this.storage.get(table, id, this.dtoClass));
        if(value != null) {
            this.cache.add(value);
        }
        return  Optional.ofNullable(value);
    }

    public List<T> where(String key, String content) {
        return this.where(new String[] {key}, new String[]{content});
    }

    public List<T> where(String[] key, String[] content) {
        List<T> values = this.cache.values().stream().filter(data -> {
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

        this.storage.where(table, this.dtoClass, key, content)
                .stream()
                .map(this::deserialize)
                .forEach(value -> {
                    if(!this.cache.values().contains(value)) {
                        this.cache.add(value);
                        values.add(value);
                    }
        });

        return values;
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

    public List<T> values() {
        this.storage.values(table, this.dtoClass).forEach(dto -> {
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


    public abstract T deserialize(@Nullable DTO dto);
}
