package fr.traqueur.factions.storages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.storage.Storage;
import fr.traqueur.factions.api.utils.FactionsLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

public class JSONStorage implements Storage {

    private final FactionsPlugin plugin;
    private final File folder;
    private final Gson gson;


    public JSONStorage(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.folder = new File(plugin.getDataFolder(), "storage");
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
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
    public void createTable(String table) {
        File tableFolder = new File(this.getFolder(), table);
        if (!tableFolder.exists()) {
            if (!tableFolder.mkdirs()) {
                FactionsLogger.severe("Unable to create table folder !");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        }
    }

    @Override
    public void save(String table, UUID id, Map<String, Object> data) {
        try {
            Files.write(Path.of(this.getFolder().getPath(), table + "/"+ id.toString() + ".json"), this.gson.toJson(data).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> get(String table, UUID id) {
        String content;
        try {
            content = new String(Files.readAllBytes(Path.of(this.getFolder().getPath(), table + "/"+ id.toString() + ".json")));
        } catch (IOException e) {
            return null;
        }
        return this.gson.fromJson(content, new TypeToken<Map<String, Object>>() {}.getType());
    }

    @Override
    public void delete(String table, UUID id) {
        try {
            Files.delete(Path.of(this.getFolder().getPath(), table + "/"+ id.toString() + ".json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
