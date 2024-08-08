package fr.traqueur.factions;

import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.managers.Manager;
import fr.traqueur.factions.api.platform.paper.PaperMessageUtils;
import fr.traqueur.factions.api.platform.spigot.SpigotMessageUtils;
import fr.traqueur.factions.api.configurations.Configuration;
import fr.traqueur.factions.api.storage.Storage;
import fr.traqueur.factions.api.utils.FactionsLogger;
import fr.traqueur.factions.api.utils.MessageUtils;
import fr.traqueur.factions.configurations.MainConfiguration;
import fr.traqueur.factions.storages.JSONStorage;
import fr.traqueur.factions.storages.MongoDBStorage;
import fr.traqueur.factions.storages.SQLStorage;

public class ModernFactionsPlugin extends FactionsPlugin {

    private MessageUtils messageUtils;

    private Storage storage;

    @Override
    public void onEnable() {

        Configuration.registerConfiguration(MainConfiguration.class, new MainConfiguration(this));

        this.messageUtils = this.isPaperVersion() ? new PaperMessageUtils() : new SpigotMessageUtils(this);

        for (Configuration configuration : Configuration.REGISTERY.values()) {
            configuration.loadConfig();
        }

        this.storage = this.registerStorage();

        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);

        FactionsLogger.success("ModernFactionsPlugin enabled");
    }

    @Override
    public void onDisable() {
        if(messageUtils instanceof SpigotMessageUtils spigotMessageUtils) {
            spigotMessageUtils.close();
        }
        FactionsLogger.success("ModernFactionsPlugin disabled");
    }

    private Storage registerStorage() {
        return switch (Configuration.getConfiguration(MainConfiguration.class).getStorageType()) {
            case SQL -> new SQLStorage(this);
            case MANGODB -> new MongoDBStorage(this);
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
