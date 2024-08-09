package fr.traqueur.modernfactions.storages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.storage.Storage;
import fr.traqueur.modernfactions.api.utils.FactionsLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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
    public <T> void save(String table, UUID id, T data) {
        try {
            Files.write(Path.of(this.getFolder().getPath(), table + "/"+ id.toString() + ".json"), this.gson.toJson(data, data.getClass()).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T get(String table, UUID id, Class<T> clazz) {
        String content;
        try {
            content = new String(Files.readAllBytes(Path.of(this.getFolder().getPath(), table + "/"+ id.toString() + ".json")));
        } catch (IOException e) {
            return null;
        }
        return this.gson.fromJson(content, clazz);
    }

    @Override
    public <T> List<T> values(String table, Class<T> clazz) {
        File tableFolder = new File(this.getFolder(), table);
        List<T> values = new ArrayList<>();
        if (tableFolder.exists()) {
            File[] files = tableFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    try {
                        String content = new String(Files.readAllBytes(file.toPath()));
                        values.add(this.gson.fromJson(content, clazz));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return values;
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
