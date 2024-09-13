package fr.traqueur.modernfactions;

import fr.traqueur.commands.api.CommandManager;
import fr.traqueur.modernfactions.api.FactionsLogger;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.chatmode.ChatMode;
import fr.traqueur.modernfactions.api.commands.CommandLogger;
import fr.traqueur.modernfactions.api.commands.FactionsCommandsHandler;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.factions.roles.Role;
import fr.traqueur.modernfactions.api.lands.LandsManager;
import fr.traqueur.modernfactions.api.messages.LangConfiguration;
import fr.traqueur.modernfactions.api.messages.MessageUtils;
import fr.traqueur.modernfactions.api.platform.paper.PaperMessageUtils;
import fr.traqueur.modernfactions.api.platform.paper.listeners.PaperChatListener;
import fr.traqueur.modernfactions.api.platform.spigot.SpigotMessageUtils;
import fr.traqueur.modernfactions.api.platform.spigot.listeners.SpigotChatListener;
import fr.traqueur.modernfactions.api.relations.RelationsManager;
import fr.traqueur.modernfactions.api.storage.Storage;
import fr.traqueur.modernfactions.api.storage.service.Service;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import fr.traqueur.modernfactions.commands.CommandsLoader;
import fr.traqueur.modernfactions.commands.arguments.*;
import fr.traqueur.modernfactions.configurations.MainConfiguration;
import fr.traqueur.modernfactions.factions.FFactionsManager;
import fr.traqueur.modernfactions.lands.FLandsManager;
import fr.traqueur.modernfactions.listeners.MoveListener;
import fr.traqueur.modernfactions.listeners.ServerListener;
import fr.traqueur.modernfactions.relations.FRelationsManager;
import fr.traqueur.modernfactions.storages.JSONStorage;
import fr.traqueur.modernfactions.storages.MongoDBStorage;
import fr.traqueur.modernfactions.storages.SQLStorage;
import fr.traqueur.modernfactions.storages.StorageType;
import fr.traqueur.modernfactions.users.FUsersManager;
import fr.traqueur.modernfactions.users.UsersListener;

public class ModernFactionsPlugin extends FactionsPlugin {

    private MessageUtils messageUtils;
    private CommandManager commandManager;
    private CommandsLoader commandsLoader;
    private Storage storage;

    @Override
    public void onEnable() {
        Config.registerConfiguration(MainConfiguration.class, new MainConfiguration(this));

        this.messageUtils = this.isPaperVersion() ? new PaperMessageUtils() : new SpigotMessageUtils(this);

        this.commandManager = new CommandManager(this);
        this.commandManager.setMessageHandler(new FactionsCommandsHandler());
        this.commandManager.setLogger(new CommandLogger());

        Config.getConfiguration(MainConfiguration.class).loadConfig();

        this.storage = this.registerStorage();
        this.storage.onEnable();

        this.registerManager(new FFactionsManager(this), FactionsManager.class);
        this.registerManager(new FUsersManager(this), UsersManager.class);
        this.registerManager(new FLandsManager(this), LandsManager.class);
        this.registerManager(new FRelationsManager(this), RelationsManager.class);

        for (Config configuration : Config.REGISTRY.values()) {
            if(configuration instanceof MainConfiguration || configuration instanceof LangConfiguration) {
                continue;
            }
            configuration.loadConfig();
        }

        for (Service<?, ?> service : Service.REGISTRY) {
            service.loadAll();
        }

        this.commandManager.registerConverter(ChatMode.class, "chatmode", new ChatModeArgument());
        this.commandManager.registerConverter(Faction.class, "faction", new FactionArgument(this));
        this.commandManager.registerConverter(User.class, "user", new UserArgument(this));
        this.commandManager.registerConverter(User.class, "faction_member", new FactionMembersArgument(this));
        this.commandManager.registerConverter(Role.class, "role", new RoleArgument());

        this.getServer().getPluginManager().registerEvents(new UsersListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ServerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new MoveListener(this), this);

        if(this.isPaperVersion()) {
            this.getServer().getPluginManager().registerEvents(new PaperChatListener(this), this);
        } else {
            this.getServer().getPluginManager().registerEvents(new SpigotChatListener(this), this);
        }

        this.commandsLoader = new CommandsLoader(this, this.commandManager);
        this.commandsLoader.loadCommands();

        Config.getConfiguration(LangConfiguration.class).loadConfig();

        FactionsLogger.success("ModernFactionsPlugin enabled");
    }

    @Override
    public void onDisable() {
        if(messageUtils instanceof SpigotMessageUtils spigotMessageUtils) {
            spigotMessageUtils.close();
        }
        for (Service<?, ?> service : Service.REGISTRY) {
            service.saveAll();
        }
        this.storage.onDisable();
        FactionsLogger.success("ModernFactionsPlugin disabled");
    }

    private Storage registerStorage() {
        return switch(Config.getConfiguration(MainConfiguration.class).getStorageType()) {
            case MONGODB -> new MongoDBStorage(this, Config.getConfiguration(MainConfiguration.class).isDebug());
            case SQL -> new SQLStorage(this, StorageType.SQL);
            case JSON -> new JSONStorage(this, Config.getConfiguration(MainConfiguration.class).isDebug());
            case SQLITE -> new SQLStorage(this, StorageType.SQLITE);
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

    @Override
    public boolean isEnable() {
        return true;
    }
}
