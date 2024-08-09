package fr.traqueur.modernfactions.api;

import com.tcoded.folialib.impl.ServerImplementation;
import fr.traqueur.modernfactions.api.managers.Manager;
import fr.traqueur.modernfactions.api.storage.Storage;

public interface FactionsAPI {

    Storage getStorage();

    ServerImplementation getScheduler();

    boolean isPaperVersion();

    <T extends Manager> T getManager(Class<T> clazz);

    <I extends Manager, T extends I> void registerManager(T instance, Class<I> clazz);

}
