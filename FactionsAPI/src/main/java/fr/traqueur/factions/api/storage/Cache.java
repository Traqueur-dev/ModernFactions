package fr.traqueur.factions.api.storage;

import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.users.User;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface Cache<T extends Data> {

    Map<Class<?>, Cache<?>> REGISTERY = new HashMap<>();

    static <R extends Data> void registerCache(Class<R> clazz, Cache<R> cache) {
        REGISTERY.put(clazz, cache);
    }

    static <R extends Data> Cache<R> getCache(Class<R> clazz) {
        return (Cache<R>) REGISTERY.get(clazz);
    }

    void addData(T object);

    void remove(int id);

    T get(int id);

    default void add(T object) {
        this.addData(object);
        this.scheduleCacheEviction(object.getId(), object);
    }

    default void scheduleCacheEviction(int id, T object) {
        JavaPlugin.getPlugin(FactionsPlugin.class).getScheduler().runLaterAsync(() -> {
           this.remove(id);
        }, 15, TimeUnit.MINUTES);
    }

}
