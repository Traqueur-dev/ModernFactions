package fr.traqueur.modernfactions.configurations;

import dev.dejvokep.boostedyaml.YamlDocument;
import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.database.DatabaseType;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.utils.FactionsLogger;
import fr.traqueur.modernfactions.lands.NotificationType;
import fr.traqueur.modernfactions.storages.StorageType;
import fr.traqueur.modernfactions.storages.mangodb.MongoDBConfiguration;

public class MainConfiguration implements Config {

    private final FactionsPlugin plugin;

    private StorageType storageType;
    private DatabaseConfiguration databaseConfiguration;
    private MongoDBConfiguration mongoDBConfiguration;
    private boolean debug;
    private NotificationType notificationType;
    private String notificationMessage;
    private String subtitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;
    private int maxUsersPerFaction;

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

        this.notificationType = NotificationType.valueOf(config.getString("enter-chunk-notification.type"));
        this.notificationMessage = config.getString("enter-chunk-notification.message");
        this.subtitle = config.getString("enter-chunk-notification.subtitle");
        this.fadeIn = config.getInt("enter-chunk-notification.fadeIn");
        this.stay = config.getInt("enter-chunk-notification.stay");
        this.fadeOut = config.getInt("enter-chunk-notification.fadeOut");

        this.maxUsersPerFaction = config.getInt("max-users-per-faction");
        if(this.maxUsersPerFaction < 1) {
            this.maxUsersPerFaction = 1;
            FactionsLogger.warning("The max users per faction must be at least 1.");
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

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public int getMaxUsersPerFaction() {
        return maxUsersPerFaction;
    }
}
