package fr.traqueur.modernfactions.api.configurations;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface Config {

    Map<Class<?>, Config> REGISTRY = new HashMap<>();

    static <T extends Config> T registerConfiguration(Class<T> clazz, T configuration) {
        REGISTRY.put(clazz, configuration);
        return configuration;
    }

    static <T extends Config> T getConfiguration(Class<T> clazz) {
        var config = REGISTRY.get(clazz);
        if(clazz.isInstance(config)) {
            return clazz.cast(config);
        } else {
            throw new IllegalArgumentException("Configuration not found for class " + clazz.getName());
        }
    }

    String getFile();

    default YamlDocument getConfig(JavaPlugin plugin) {
        try {
            return YamlDocument.create(new File(plugin.getDataFolder(), this.getFile()), Objects.requireNonNull(plugin.getResource(this.getFile())), GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    void loadConfig();

}