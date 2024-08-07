package fr.traqueur.factions.api;

import com.tcoded.folialib.impl.ServerImplementation;
import fr.traqueur.factions.api.managers.Manager;
import fr.traqueur.factions.api.storage.Configuration;

public interface FactionsAPI {

    ServerImplementation getScheduler();

    <I extends Configuration, T extends I> void registerConfiguration(T instance, Class<I> clazz);

    <T extends Configuration> T getConfiguration(Class<T> clazz);

    <T extends Manager> T getManager(Class<T> clazz);

    <I extends Manager, T extends I> void registerManager(T instance, Class<I> clazz);

}
