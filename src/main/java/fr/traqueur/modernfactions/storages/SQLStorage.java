package fr.traqueur.modernfactions.storages;

import fr.maxlego08.sarah.*;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.storage.Storage;
import fr.traqueur.modernfactions.api.users.UsersManager;
import fr.traqueur.modernfactions.api.utils.FactionsLogger;
import fr.traqueur.modernfactions.configurations.MainConfiguration;
import fr.traqueur.modernfactions.migrations.CreateFactionsTableMigration;
import fr.traqueur.modernfactions.migrations.CreateUsersTableMigration;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class SQLStorage implements Storage {

    private final String prefix;
    private final DatabaseConnection connection;
    private final RequestHelper requester;

    public SQLStorage(FactionsPlugin plugin) {
        DatabaseConfiguration databaseConfiguration = Config.getConfiguration(MainConfiguration.class).getDatabaseConfiguration();
        this.prefix = databaseConfiguration.getTablePrefix();
        this.connection = new HikariDatabaseConnection(databaseConfiguration);
        this.requester = new RequestHelper(this.connection, FactionsLogger::info);
        if (!this.connection.isValid()) {
            FactionsLogger.severe("Unable to connect to database !");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        } else {
            FactionsLogger.info("The database connection is valid ! (" + connection.getDatabaseConfiguration().getHost() + ")");
        }

        MigrationManager.registerMigration(new CreateUsersTableMigration(UsersManager.TABLE_NAME));
        MigrationManager.registerMigration(new CreateFactionsTableMigration(FactionsManager.TABLE_NAME));
    }

    @Override
    public void createTable(String table) {}

    @Override
    public <DTO> void save(String tableName, UUID id, DTO data) {
        this.requester.upsert(this.prefix+tableName, table -> {
            for (Field declaredField : data.getClass().getDeclaredFields()) {
                try {
                    declaredField.setAccessible(true);
                    if(declaredField.get(data) instanceof UUID uuid) {
                        table.uuid(declaredField.getName(), uuid);
                        continue;
                    }
                    table.object(declaredField.getName(), declaredField.get(data));
                } catch (IllegalAccessException e) {
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
    public <DTO> List<DTO> where(String tableName, Class<DTO> clazz, String key, String content) {
        return this.requester.select(this.prefix+tableName, clazz, table -> {
            table.where(key, content);
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
