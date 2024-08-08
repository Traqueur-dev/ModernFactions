package fr.traqueur.factions.api.configurations;

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

public interface Configuration {

    Map<Class<?>, Configuration> REGISTERY = new HashMap<>();

    static <T extends Configuration> void registerConfiguration(Class<T> clazz, T configuration) {
        REGISTERY.put(clazz, configuration);
    }

    static <T extends Configuration> T getConfiguration(Class<T> clazz) {
        return (T) REGISTERY.get(clazz);
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