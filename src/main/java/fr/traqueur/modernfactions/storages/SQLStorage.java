package fr.traqueur.modernfactions.storages;

import fr.maxlego08.sarah.*;
import fr.traqueur.modernfactions.api.FactionsLogger;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.lands.LandsManager;
import fr.traqueur.modernfactions.api.relations.RelationsManager;
import fr.traqueur.modernfactions.api.storage.Storage;
import fr.traqueur.modernfactions.api.users.UsersManager;
import fr.traqueur.modernfactions.configurations.MainConfiguration;
import fr.traqueur.modernfactions.migrations.CreateFactionsTableMigration;
import fr.traqueur.modernfactions.migrations.CreateLandsTableMigration;
import fr.traqueur.modernfactions.migrations.CreateRelationsTableMigration;
import fr.traqueur.modernfactions.migrations.CreateUsersTableMigration;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.List;
import java.util.UUID;

public class SQLStorage implements Storage {

    private final String prefix;
    private final DatabaseConnection connection;
    private final RequestHelper requester;

    public SQLStorage(FactionsPlugin plugin, StorageType type) {
        DatabaseConfiguration databaseConfiguration = Config.getConfiguration(MainConfiguration.class).getDatabaseConfiguration();
        this.prefix = databaseConfiguration.getTablePrefix();
        if(type == StorageType.SQL) {
            this.connection = new HikariDatabaseConnection(databaseConfiguration);
        } else if (type == StorageType.SQLITE) {
            this.connection = new SqliteConnection(databaseConfiguration, new File(plugin.getDataFolder(), "storage"));
        } else {
            throw new IllegalArgumentException("Invalid storage type !");
        }

        this.requester = new RequestHelper(this.connection, FactionsLogger::info);
        if (!this.connection.isValid()) {
            FactionsLogger.severe("Unable to connect to database !");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        } else {
            FactionsLogger.info("The database connection is valid ! (" + connection.getDatabaseConfiguration().getHost() + ")");
        }

        MigrationManager.registerMigration(new CreateUsersTableMigration(UsersManager.TABLE_NAME));
        MigrationManager.registerMigration(new CreateFactionsTableMigration(FactionsManager.TABLE_NAME));
        MigrationManager.registerMigration(new CreateRelationsTableMigration(RelationsManager.TABLE_NAME));
        MigrationManager.registerMigration(new CreateLandsTableMigration(LandsManager.TABLE_NAME));
    }

    @Override
    public void createTable(String table) {}

    @Override
    public <DTO> void save(String tableName, UUID id, DTO data) {
        this.requester.upsert(this.prefix+tableName, table -> {
            int i = 0;
            for (RecordComponent recordComponent : data.getClass().getRecordComponents()) {
                try {
                    Field field = data.getClass().getDeclaredField(recordComponent.getName());
                    field.setAccessible(true);
                    Object obj = field.get(data);

                    if(recordComponent.isAnnotationPresent(Column.class) && recordComponent.getAnnotation(Column.class).primary()) {
                        if(obj instanceof UUID uuid) {
                            table.uuid(recordComponent.getName(), uuid).primary();
                            continue;
                        }
                        table.object(recordComponent.getName(), obj).primary();
                    } else {
                        if(obj instanceof UUID uuid) {
                            table.uuid(recordComponent.getName(), uuid);
                            continue;
                        }
                        table.object(recordComponent.getName(), obj);
                    }
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public <DTO> DTO get(String tableName, UUID id, Class<DTO> clazz) {
        List<DTO> result = this.requester.select(this.prefix+tableName, clazz, table -> {
            table.where("unique_id", id);
        });
        return result.isEmpty() ? null : result.getFirst();
    }

    @Override
    public <DTO> List<DTO> values(String table, Class<DTO> clazz) {
       return this.requester.selectAll(this.prefix+table, clazz);
    }

    @Override
    public void delete(String tableName, UUID id) {
        this.requester.delete(this.prefix+tableName, table -> {
            table.where("unique_id", id);
        });
    }

    @Override
    public <DTO> List<DTO> where(String tableName, Class<DTO> clazz, String[] key, String[] content) {
        return this.requester.select(this.prefix+tableName, clazz, table -> {
            for (int i = 0; i < key.length; i++) {
                table.where(key[i], content[i]);
            }
        });
    }

    @Override
    public boolean isDebug() {
        return Config.getConfiguration(MainConfiguration.class).isDebug();
    }

    @Override
    public void onEnable() {
        this.connection.connect();
        MigrationManager.execute(this.connection, FactionsLogger::info);
    }

    @Override
    public void onDisable() {
        this.connection.disconnect();
    }
}
