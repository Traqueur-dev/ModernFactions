package fr.traqueur.factions.storages;

import com.google.gson.reflect.TypeToken;
import fr.maxlego08.sarah.*;
import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.configurations.Config;
import fr.traqueur.factions.api.storage.Storage;
import fr.traqueur.factions.api.utils.FactionsLogger;
import fr.traqueur.factions.configurations.MainConfiguration;
import fr.traqueur.factions.storages.sql.CreateUserTableMigration;
import fr.traqueur.factions.users.FUsersManager;

import java.util.List;
import java.util.Map;
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

        MigrationManager.registerMigration(new CreateUserTableMigration(FUsersManager.TABLE_NAME));
    }

    @Override
    public void createTable(String table) {

    }

    @Override
    public void save(String tableName, UUID id, Map<String, Object> data) {
        this.requester.upsert(this.prefix+tableName, table -> {
            data.forEach((key, value) -> {
                table.object(key, value.toString());
            });
        });
    }

    @Override
    public Map<String, Object> get(String tableName, UUID id) {
        TypeToken<Map<String, Object>> typeToken = new TypeToken<>() {};
        Class<Map<String, Object>> clazz = (Class<Map<String, Object>>) typeToken.getRawType();
        List<Map<String,Object>> result = this.requester.select(this.prefix+tableName, clazz, table -> {
            table.where("unique_id", id);
        });
        return result.isEmpty() ? null : result.getFirst();
    }

    @Override
    public void delete(String tableName, UUID id) {
        this.requester.delete(this.prefix+tableName, table -> {
            table.where("unique_id", id);
        });
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
