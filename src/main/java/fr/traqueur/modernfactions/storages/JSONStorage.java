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
    private final boolean debug;

    public JSONStorage(FactionsPlugin plugin, boolean debug) {
        this.plugin = plugin;
        this.debug = debug;
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
    public <DTO> void save(String table, UUID id, DTO data) {
        try {
            if (this.isDebug()) {
                FactionsLogger.info("Saving JSON data to: storage/" + table + "/" + id.toString() + ".json");
            }
            Files.write(Path.of(this.getFolder().getPath(), table + "/"+ id.toString() + ".json"), this.gson.toJson(data, data.getClass()).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <DTO> DTO get(String table, UUID id, Class<DTO> clazz) {
        String content;
        if (this.isDebug()) {
            FactionsLogger.info("Get JSON data to: storage/" + table + "/" + id.toString() + ".json");
        }
        try {
            content = new String(Files.readAllBytes(Path.of(this.getFolder().getPath(), table + "/"+ id.toString() + ".json")));
        } catch (IOException e) {
            return null;
        }
        return this.gson.fromJson(content, clazz);
    }

    @Override
    public <DTO> List<DTO> values(String table, Class<DTO> clazz) {
        File tableFolder = new File(this.getFolder(), table);
        List<DTO> values = new ArrayList<>();
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
        if (this.isDebug()) {
            FactionsLogger.info("Get All JSON data to: storage/" + table + "/");
        }
        return values;
    }

    @Override
    public void delete(String table, UUID id) {
        try {
            if (this.isDebug()) {
                FactionsLogger.info("Delete JSON data to: storage/" + table + "/" + id.toString() + ".json");
            }
            Files.delete(Path.of(this.getFolder().getPath(), table + "/"+ id.toString() + ".json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <DTO> List<DTO> where(String tableName, Class<DTO> clazz, String[] key, String[] content) {
        File tableFolder = new File(this.getFolder(), tableName);
        List<DTO> values = new ArrayList<>();
        if (tableFolder.exists()) {
            File[] files = tableFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    try {
                        String fileContent = new String(Files.readAllBytes(file.toPath()));
                        Map<String, Object> map = this.gson.fromJson(fileContent, new TypeToken<Map<String, Object>>(){}.getType());
                        boolean valid = true;
                        for (int i = 0; i < key.length; i++) {
                            if (!map.containsKey(key[i]) || !map.get(key[i]).equals(content[i])) {
                                valid = false;
                                break;
                            }
                        }
                        if (valid) {
                            values.add(this.gson.fromJson(fileContent, clazz));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        if (this.isDebug()) {
            FactionsLogger.info("Get All JSON data to: storage/" + tableName + "/ where key " + key + " is " + content);
        }
        return values;
    }

    @Override
    public boolean isDebug() {
        return this.debug;
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
