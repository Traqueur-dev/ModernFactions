package fr.traqueur.factions.storages;

import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.storage.Storage;
import fr.traqueur.factions.api.utils.FactionsLogger;

import java.io.File;

public class JSONStorage implements Storage {

    private final FactionsPlugin plugin;
    private final File folder;

    public JSONStorage(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.folder = new File(plugin.getDataFolder(), "storage");
        this.createFolder();
    }

    public File getFolder() {
        return this.folder;
    }

    private void createFolder() {
        File folder = this.getFolder();
        if (!folder.exists()) {
            if(!folder.mkdirs()) {
                FactionsLogger.severe("Unable to create storage folder !");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        }
        FactionsLogger.success("The storage folder has been created or it's already existing !");
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
