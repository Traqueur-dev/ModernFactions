package fr.traqueur.modernfactions.configurations;

import dev.dejvokep.boostedyaml.YamlDocument;
import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.database.DatabaseType;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.storages.StorageType;
import fr.traqueur.modernfactions.storages.mangodb.MongoDBConfiguration;

public class MainConfiguration implements Config {

    private final FactionsPlugin plugin;

    private StorageType storageType;
    private DatabaseConfiguration databaseConfiguration;
    private MongoDBConfiguration mongoDBConfiguration;
    private boolean debug;

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
        this.debug = config.getBoolean("storage-config.debug");
        if(this.storageType == StorageType.SQL || this.storageType == StorageType.SQLITE) {
            this.databaseConfiguration = new DatabaseConfiguration(
                    config.getString("storage-config.table-prefix"),
                    config.getString("storage-config.username"),
                    config.getString("storage-config.password"),
                    config.getInt("storage-config.port", 3306),
                    config.getString("storage-config.host"),
                    config.getString("storage-config.database"),
                    this.debug,
                    this.storageType == StorageType.SQL ? DatabaseType.MYSQL : DatabaseType.SQLITE
            );
        } else if (this.storageType == StorageType.MONGODB) {
            this.mongoDBConfiguration = new MongoDBConfiguration(
                    config.getString("storage-config.host"),
                    config.getInt("storage-config.port"),
                    config.getString("storage-config.database"),
                    config.getString("storage-config.username"),
                    config.getString("storage-config.password"),
                    config.getString("storage-config.auth-database"),
                    config.getString("storage-config.table-prefix")
            );
        }


    }

    public StorageType getStorageType() {
        return storageType;
    }

    public boolean isDebug() {
        return debug;
    }

    public MongoDBConfiguration getMangoDBConfiguration() {
        if (this.storageType != StorageType.MONGODB) {
            throw new UnsupportedOperationException("The storage type is not MANGODB");
        }
        return mongoDBConfiguration;
    }

    public DatabaseConfiguration getDatabaseConfiguration() {
        if(this.storageType != StorageType.SQL && this.storageType != StorageType.SQLITE) {
            throw new UnsupportedOperationException("The storage type is not SQL");
        }
        return databaseConfiguration;
    }
}
