package fr.traqueur.factions.api;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.impl.ServerImplementation;
import fr.traqueur.factions.api.managers.Manager;
import fr.traqueur.factions.api.utils.FactionsLogger;
import fr.traqueur.factions.api.utils.MessageUtils;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.NoSuchElementException;

public abstract class FactionsPlugin extends JavaPlugin implements FactionsAPI {

    private ServerImplementation scheduler;

    public abstract MessageUtils getMessageUtils();

    @Override
    public boolean isPaperVersion() {
        try {
            Class.forName("io.papermc.paper.event.player.AsyncChatEvent");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public ServerImplementation getScheduler() {
        if(scheduler == null) {
            scheduler = new FoliaLib(this).getImpl();
        }
        return this.scheduler;
    }

    @Override
    public <T extends Manager> T getManager(Class<T> clazz) {
        RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(clazz);
        if (provider == null) {
            throw new NoSuchElementException("No provider found for " + clazz.getSimpleName() + " class.");
        }
        return provider.getProvider();
    }

    @Override
    public <I extends Manager, T extends I> void registerManager(T instance, Class<I> clazz) {
        this.getServer().getServicesManager().register(clazz, instance, this, ServicePriority.Normal);
        FactionsLogger.info("&eManager registered: " + clazz.getSimpleName());
    }

}
