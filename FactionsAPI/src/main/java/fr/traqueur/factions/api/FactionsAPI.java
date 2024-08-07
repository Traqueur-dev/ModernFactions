package fr.traqueur.factions.api;

import com.tcoded.folialib.impl.ServerImplementation;

public interface FactionsAPI {

    ServerImplementation getScheduler();

    <T> T getManager(Class<T> clazz);

    <I, T extends I> void registerManager(T instance, Class<I> clazz);

}
