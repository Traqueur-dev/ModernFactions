package fr.traqueur.factions.api;

import com.tcoded.folialib.impl.ServerImplementation;
import fr.traqueur.factions.api.managers.Manager;
import fr.traqueur.factions.api.configurations.Configuration;
import fr.traqueur.factions.api.storage.Storage;

public interface FactionsAPI {

    Storage getStorage();

    ServerImplementation getScheduler();

    boolean isPaperVersion();

    <I extends Configuration, T extends I> void registerConfiguration(T instance, Class<I> clazz);

    <T extends Configuration> T getConfiguration(Class<T> clazz);

    <T extends Manager> T getManager(Class<T> clazz);

    <I extends Manager, T extends I> void registerManager(T instance, Class<I> clazz);

}
