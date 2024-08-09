package fr.traqueur.modernfactions;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.platform.paper.PaperMessageUtils;
import fr.traqueur.modernfactions.api.platform.spigot.SpigotMessageUtils;
import fr.traqueur.modernfactions.api.storage.Storage;
import fr.traqueur.modernfactions.api.storage.service.Service;
import fr.traqueur.modernfactions.api.users.UsersManager;
import fr.traqueur.modernfactions.api.utils.FactionsLogger;
import fr.traqueur.modernfactions.api.utils.MessageUtils;
import fr.traqueur.modernfactions.configurations.MainConfiguration;
import fr.traqueur.modernfactions.factions.FFactionsManager;
import fr.traqueur.modernfactions.listeners.ServerListener;
import fr.traqueur.modernfactions.storages.JSONStorage;
import fr.traqueur.modernfactions.storages.MongoDBStorage;
import fr.traqueur.modernfactions.storages.SQLStorage;
import fr.traqueur.modernfactions.users.FUsersManager;
import fr.traqueur.modernfactions.users.UsersListener;

public class ModernFactionsPlugin extends FactionsPlugin {

    private MessageUtils messageUtils;

    private Storage storage;

    @Override
    public void onEnable() {
        Config.registerConfiguration(MainConfiguration.class, new MainConfiguration(this));

        this.messageUtils = this.isPaperVersion() ? new PaperMessageUtils() : new SpigotMessageUtils(this);

        for (Config configuration : Config.REGISTERY.values()) {
            configuration.loadConfig();
        }

        this.storage = this.registerStorage();
        this.storage.onEnable();

        this.registerManager(new FUsersManager(this), UsersManager.class);
        this.registerManager(new FFactionsManager(this), FactionsManager.class);

        this.getServer().getPluginManager().registerEvents(new UsersListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ServerListener(this), this);

        FactionsLogger.success("ModernFactionsPlugin enabled");
    }

    @Override
    public void onDisable() {
        if(messageUtils instanceof SpigotMessageUtils spigotMessageUtils) {
            spigotMessageUtils.close();
        }
        for (Service<?, ?> service : Service.REGISTERY) {
            service.saveAll();
        }
        this.storage.onDisable();
        FactionsLogger.success("ModernFactionsPlugin disabled");
    }

    private Storage registerStorage() {
        return switch(Config.getConfiguration(MainConfiguration.class).getStorageType()) {
            case MONGODB -> new MongoDBStorage(this);
            case SQL -> new SQLStorage(this);
            case JSON -> new JSONStorage(this);
        };
    }

    @Override
    public Storage getStorage() {
        return storage;
    }

    @Override
    public MessageUtils getMessageUtils() {
        return messageUtils;
    }
}
