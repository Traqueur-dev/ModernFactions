package fr.traqueur.modernfactions.configurations;

import dev.dejvokep.boostedyaml.YamlDocument;
import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.database.DatabaseType;
import fr.traqueur.modernfactions.api.FactionsLogger;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.messages.NotificationType;
import fr.traqueur.modernfactions.storages.StorageType;
import fr.traqueur.modernfactions.storages.mangodb.MongoDBConfiguration;

import java.io.IOException;

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
    private int maxUserPower;
    private int minUserPower;
    private int defaultUserPower;
    private int delayTeleportHome;
    private String publicChatFormat;
    private String truceChatFormat;
    private String allyChatFormat;
    private String factionChatFormat;
    private boolean load;

    public MainConfiguration(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.load = false;
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

        boolean saved = false;

        this.maxUsersPerFaction = config.getInt("max-users-per-faction");
        if(this.maxUsersPerFaction < 1) {
            this.maxUsersPerFaction = 1;
            config.set("max-users-per-faction", this.maxUsersPerFaction);
            saved = true;
            FactionsLogger.warning("The max users per faction must be at least 1.");
        }

        int maxUserPower = config.getInt("max-user-power");
        int minUserPower = config.getInt("min-user-power");
        if(maxUserPower < 1) {
            maxUserPower = 1;
            config.set("max-user-power", maxUserPower);
            saved = true;
            FactionsLogger.warning("The max user power must be at least 1.");
        }

        if(minUserPower > maxUserPower) {
            this.minUserPower = maxUserPower;
            this.maxUserPower = minUserPower;
            config.set("min-user-power", this.minUserPower);
            config.set("max-user-power", this.maxUserPower);
            saved = true;
            FactionsLogger.warning("The min user power must be less than the max user power.");
        } else if(minUserPower == maxUserPower) {
            this.minUserPower = minUserPower;
            this.maxUserPower = maxUserPower + 1;
            config.set("max-user-power", this.maxUserPower);
            saved = true;
            FactionsLogger.warning("The min user power must not be equals than the max user power.");
        } else {
            this.minUserPower = minUserPower;
            this.maxUserPower = maxUserPower;
        }

        this.defaultUserPower = config.getInt("default-user-power");
        if (this.defaultUserPower < this.minUserPower || this.defaultUserPower > this.maxUserPower) {
            this.defaultUserPower = this.maxUserPower;
            config.set("default-user-power", this.defaultUserPower);
            saved = true;
            FactionsLogger.warning("The default user power must be between the min and max user power.");
        }

        if(saved) {
            try {
                config.save();
            } catch (IOException e) {
                throw new RuntimeException("An error occurred while saving the configuration file.", e);
            }
            FactionsLogger.info("Some values in the configuration file were invalid and have been corrected.");
        }

        this.delayTeleportHome = config.getInt("delay-teleport-home");
        if (this.delayTeleportHome < 0) {
            this.delayTeleportHome = 1;
            config.set("delay-teleport-home", this.delayTeleportHome);
            FactionsLogger.warning("The delay teleport home must be positive.");
            try {
                config.save();
            } catch (IOException e) {
                throw new RuntimeException("An error occurred while saving the configuration file.", e);
            }
        }

        this.publicChatFormat = config.getString("chat.public-format");
        this.truceChatFormat = config.getString("chat.truce-format");
        this.allyChatFormat = config.getString("chat.ally-format");
        this.factionChatFormat = config.getString("chat.faction-format");

        this.load = true;
    }

    @Override
    public boolean isLoad() {
        return this.load;
    }

    public String getPublicChatFormat() {
        return this.publicChatFormat;
    }

    public String getTruceChatFormat() {
        return this.truceChatFormat;
    }

    public String getAllyChatFormat() {
        return this.allyChatFormat;
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

    public int getMaxUserPower() {
        return maxUserPower;
    }

    public int getMinUserPower() {
        return minUserPower;
    }

    public int getDefaultUserPower() {
        return defaultUserPower;
    }

    public int getDelayTeleportHome() {
        return delayTeleportHome;
    }

    public String getFactionChatFormat() {
        return this.factionChatFormat;
    }
}
