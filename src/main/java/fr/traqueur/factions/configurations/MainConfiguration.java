package fr.traqueur.factions.configurations;

import dev.dejvokep.boostedyaml.YamlDocument;
import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.database.DatabaseType;
import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.configurations.Configuration;
import fr.traqueur.factions.storages.StorageType;
import fr.traqueur.factions.storages.mangodb.MongoDBConfiguration;

public class MainConfiguration implements Configuration {

    private final FactionsPlugin plugin;

    private StorageType storageType;
    private DatabaseConfiguration databaseConfiguration;
    private MongoDBConfiguration mongoDBConfiguration;


    public MainConfiguration(FactionsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getFile() {
        return "config.yml";
    }

    @Override
    public void loadConfig() {
        YamlDocument config = this.getConfig(this.plugin);
        this.storageType = StorageType.valueOf(config.getString("storage-type").toUpperCase());
        if(this.storageType == StorageType.SQL) {
            this.databaseConfiguration = new DatabaseConfiguration(
                    config.getString("storage-config.table-prefix"),
                    config.getString("storage-config.username"),
                    config.getString("storage-config.password"),
                    config.getInt("storage-config.port", 3306),
                    config.getString("storage-config.host"),
                    config.getString("storage-config.database"),
                    config.getBoolean("storage-config.debug"),
                    DatabaseType.MYSQL
            );
        } else if (this.storageType == StorageType.MANGODB) {
            this.mongoDBConfiguration = new MongoDBConfiguration(
                    config.getString("storage-config.host"),
                    config.getInt("storage-config.port"),
                    config.getString("storage-config.database"),
                    config.getString("storage-config.username"),
                    config.getString("storage-config.password"),
                    config.getString("storage-config.auth-database")
            );
        }


    }

    public StorageType getStorageType() {
        return storageType;
    }

    public MongoDBConfiguration getMangoDBConfiguration() {
        if (this.storageType != StorageType.MANGODB) {
            throw new UnsupportedOperationException("The storage type is not MANGODB");
        }
        return mongoDBConfiguration;
    }

    public DatabaseConfiguration getDatabaseConfiguration() {
        if(this.storageType != StorageType.SQL) {
            throw new UnsupportedOperationException("The storage type is not SQL");
        }
        return databaseConfiguration;
    }
}
