package fr.traqueur.factions;

import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.managers.Manager;
import fr.traqueur.factions.api.platform.paper.PaperMessageUtils;
import fr.traqueur.factions.api.platform.spigot.SpigotMessageUtils;
import fr.traqueur.factions.api.storage.Configuration;
import fr.traqueur.factions.api.utils.FactionsLogger;
import fr.traqueur.factions.api.utils.MessageUtils;
import fr.traqueur.factions.configurations.GlobalConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ModernFactionsPlugin extends FactionsPlugin {

    private MessageUtils messageUtils;

    private Map<Class<? extends Configuration>, Configuration> configurations;

    @Override
    public void onLoad() {
        this.configurations = new HashMap<>();
    }

    @Override
    public void onEnable() {

        this.registerConfiguration(new GlobalConfiguration(this), GlobalConfiguration.class);

        this.messageUtils = this.isPaperVersion() ? new PaperMessageUtils() : new SpigotMessageUtils(this);

        for (Configuration configuration : this.configurations.values()) {
            configuration.loadData();
        }

        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);

        FactionsLogger.success("ModernFactionsPlugin enabled");
    }

    @Override
    public void onDisable() {
        for (Configuration configuration : this.configurations.values()) {
            configuration.saveData();
        }

        if(messageUtils instanceof SpigotMessageUtils spigotMessageUtils) {
            spigotMessageUtils.close();
        }

        FactionsLogger.success("ModernFactionsPlugin disabled");
    }

    @Override
    public MessageUtils getMessageUtils() {
        return messageUtils;
    }

    @Override
    public <I extends Configuration, T extends I> void registerConfiguration(T instance, Class<I> clazz) {
        this.configurations.put(clazz, instance);
    }

    @Override
    public <T extends Configuration> T getConfiguration(Class<T> clazz) {
        return (T) this.configurations.get(clazz);
    }

    @Override
    public <I extends Manager, T extends I> void registerManager(T instance, Class<I> clazz) {
        super.registerManager(instance, clazz);
        if (instance instanceof Configuration configuration) {
            this.configurations.put(configuration.getClass(), configuration);
        }
    }
}
