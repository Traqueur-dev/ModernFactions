package fr.traqueur.factions;

import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.storage.Configuration;
import fr.traqueur.factions.api.utils.FactionsLogger;

import java.util.ArrayList;
import java.util.List;

public class ModernFactionsPlugin extends FactionsPlugin {

    private List<Configuration> configurations;

    @Override
    public void onLoad() {
        this.configurations = new ArrayList<>();
    }

    @Override
    public void onEnable() {
        FactionsLogger.success("ModernFactionsPlugin enabled");
    }

    @Override
    public void onDisable() {
        for (Configuration configuration : this.configurations) {
            configuration.saveData();
        }
        FactionsLogger.success("ModernFactionsPlugin disabled");
    }

    @Override
    public <I, T extends I> void registerManager(T instance, Class<I> clazz) {
        super.registerManager(instance, clazz);
        if (instance instanceof Configuration configuration) {
            this.configurations.add(configuration);
        }
    }
}
