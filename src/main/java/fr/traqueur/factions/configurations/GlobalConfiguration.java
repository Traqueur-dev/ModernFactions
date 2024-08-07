package fr.traqueur.factions.configurations;

import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.storage.Configuration;

import java.io.IOException;

public class GlobalConfiguration implements Configuration {

    private final FactionsPlugin plugin;

    public GlobalConfiguration(FactionsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getFile() {
        return "config.yml";
    }

    @Override
    public void loadData() {

    }

    @Override
    public void saveData() {
        try {
            this.getConfig(this.plugin).save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
