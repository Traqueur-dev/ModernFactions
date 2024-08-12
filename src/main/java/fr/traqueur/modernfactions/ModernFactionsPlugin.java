package fr.traqueur.modernfactions;

import fr.traqueur.commands.api.CommandManager;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FactionsCommandsHandler;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.lands.LandsManager;
import fr.traqueur.modernfactions.api.messages.LangConfiguration;
import fr.traqueur.modernfactions.api.messages.MessageUtils;
import fr.traqueur.modernfactions.api.platform.paper.PaperMessageUtils;
import fr.traqueur.modernfactions.api.platform.paper.listeners.PaperChatListener;
import fr.traqueur.modernfactions.api.platform.spigot.SpigotMessageUtils;
import fr.traqueur.modernfactions.api.platform.spigot.listeners.SpigotChatListener;
import fr.traqueur.modernfactions.api.storage.Storage;
import fr.traqueur.modernfactions.api.storage.service.Service;
import fr.traqueur.modernfactions.api.users.UsersManager;
import fr.traqueur.modernfactions.api.utils.FactionsLogger;
import fr.traqueur.modernfactions.commands.FCreateCommand;
import fr.traqueur.modernfactions.commands.FDisbandCommand;
import fr.traqueur.modernfactions.configurations.MainConfiguration;
import fr.traqueur.modernfactions.factions.FFactionsManager;
import fr.traqueur.modernfactions.lands.FLandsManager;
import fr.traqueur.modernfactions.listeners.MoveListener;
import fr.traqueur.modernfactions.listeners.ServerListener;
import fr.traqueur.modernfactions.storages.JSONStorage;
import fr.traqueur.modernfactions.storages.MongoDBStorage;
import fr.traqueur.modernfactions.storages.SQLStorage;
import fr.traqueur.modernfactions.users.FUsersManager;
import fr.traqueur.modernfactions.users.UsersListener;

public class ModernFactionsPlugin extends FactionsPlugin {

    private MessageUtils messageUtils;
    private CommandManager commandManager;
    private Storage storage;

    @Override
    public void onEnable() {
        Config.registerConfiguration(MainConfiguration.class, new MainConfiguration(this));

        this.messageUtils = this.isPaperVersion() ? new PaperMessageUtils() : new SpigotMessageUtils(this);
        this.commandManager = new CommandManager(this);
        this.commandManager.setMessageHandler(new FactionsCommandsHandler());

        Config.getConfiguration(MainConfiguration.class).loadConfig();

        this.storage = this.registerStorage();
        this.storage.onEnable();

        this.registerManager(new FUsersManager(this), UsersManager.class);
        this.registerManager(new FFactionsManager(this), FactionsManager.class);
        this.registerManager(new FLandsManager(this), LandsManager.class);

        for (Config configuration : Config.REGISTERY.values()) {
            if(configuration instanceof MainConfiguration || configuration instanceof LangConfiguration) {
                continue;
            }
            configuration.loadConfig();
        }

        this.commandManager.registerCommand(new FCreateCommand(this));
        this.commandManager.registerCommand(new FDisbandCommand(this));

        this.getServer().getPluginManager().registerEvents(new UsersListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ServerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new MoveListener(this), this);

        if(this.isPaperVersion()) {
            this.getServer().getPluginManager().registerEvents(new PaperChatListener(this), this);
        } else {
            this.getServer().getPluginManager().registerEvents(new SpigotChatListener(this), this);
        }

        Config.getConfiguration(LangConfiguration.class).loadConfig();

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
            case MONGODB -> new MongoDBStorage(this, Config.getConfiguration(MainConfiguration.class).isDebug());
            case SQL -> new SQLStorage(this);
            case JSON -> new JSONStorage(this, Config.getConfiguration(MainConfiguration.class).isDebug());
        };
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
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
