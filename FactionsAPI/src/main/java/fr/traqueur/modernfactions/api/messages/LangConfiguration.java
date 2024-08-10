package fr.traqueur.modernfactions.api.messages;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.utils.FactionsLogger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class LangConfiguration implements Config {

    private final FactionsPlugin plugin;
    private final Map<String, YamlDocument> langs;
    private YamlDocument lang;

    public LangConfiguration(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.langs = new HashMap<>();
    }

    @Override
    public String getFile() {
        return "lang.yml";
    }

    public String translate(Message message) {
        return this.lang.getString(message.getKey());
    }

    @Override
    public void loadConfig() {
        YamlDocument config = this.getConfig(this.plugin);

        Section langs = config.getSection("langs");
        langs.getKeys().forEach(lang -> {
            String langFile = langs.getString(lang.toString());
            try {
                YamlDocument langDocument = YamlDocument.create(new File(plugin.getDataFolder(), this.getFile()), Objects.requireNonNull(plugin.getResource(langFile)), GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build());
                this.langs.put(lang.toString(), langDocument);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        if(!this.langs.containsKey(config.getString("lang"))) {
            throw new IllegalStateException("Lang not found");
        }
        this.lang = this.langs.get(config.getString("lang"));
        FactionsLogger.success("Loaded " + this.langs.size() + " lang.");

        AtomicBoolean missing = new AtomicBoolean(false);
        this.langs.forEach((lang, value) -> {
            for (Message message : Message.MESSAGES) {
                if (!value.contains(message.getKey())) {
                    FactionsLogger.severe("Missing message: " + message.getKey() + " in lang: " + lang);
                    missing.set(true);
                }
            }
        });

        if(missing.get()) {
            this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
        }

    }
}
