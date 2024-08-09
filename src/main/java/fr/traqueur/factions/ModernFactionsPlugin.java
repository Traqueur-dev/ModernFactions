package fr.traqueur.factions;

import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.platform.paper.PaperMessageUtils;
import fr.traqueur.factions.api.platform.spigot.SpigotMessageUtils;
import fr.traqueur.factions.api.configurations.Config;
import fr.traqueur.factions.api.storage.Storage;
import fr.traqueur.factions.api.storage.service.Service;
import fr.traqueur.factions.api.users.UsersManager;
import fr.traqueur.factions.api.utils.FactionsLogger;
import fr.traqueur.factions.api.utils.MessageUtils;
import fr.traqueur.factions.configurations.MainConfiguration;
import fr.traqueur.factions.storages.JSONStorage;
import fr.traqueur.factions.storages.MongoDBStorage;
import fr.traqueur.factions.storages.SQLStorage;
import fr.traqueur.factions.users.FUsersManager;
import fr.traqueur.factions.users.JoinListener;

public class ModernFactionsPlugin extends FactionsPlugin {

    private MessageUtils messageUtils;

    private Storage storage;

    @Override
    public void onLoad() {
        Config.registerConfiguration(MainConfiguration.class, new MainConfiguration(this));

        this.messageUtils = this.isPaperVersion() ? new PaperMessageUtils() : new SpigotMessageUtils(this);

        for (Config configuration : Config.REGISTERY.values()) {
            configuration.loadConfig();
        }

        this.storage = this.registerStorage();
        this.storage.onEnable();
    }

    @Override
    public void onEnable() {
        this.registerManager(new FUsersManager(this), UsersManager.class);
        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        FactionsLogger.success("ModernFactionsPlugin enabled");
    }

    @Override
    public void onDisable() {
        if(messageUtils instanceof SpigotMessageUtils spigotMessageUtils) {
            spigotMessageUtils.close();
        }
        for (Service<?> service : Service.REGISTERY) {
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
