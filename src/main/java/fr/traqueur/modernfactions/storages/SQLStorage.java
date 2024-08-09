package fr.traqueur.modernfactions.storages;

import fr.maxlego08.sarah.*;
import fr.maxlego08.sarah.database.Schema;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.storage.Storage;
import fr.traqueur.modernfactions.api.users.UsersManager;
import fr.traqueur.modernfactions.api.utils.FactionsLogger;
import fr.traqueur.modernfactions.configurations.MainConfiguration;
import fr.traqueur.modernfactions.storages.migrations.CreateFactionsTableMigration;
import fr.traqueur.modernfactions.storages.migrations.CreateUsersTableMigration;
import fr.traqueur.modernfactions.users.FUsersManager;

import java.sql.SQLException;
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

        MigrationManager.registerMigration(new CreateUsersTableMigration(UsersManager.TABLE_NAME));
        MigrationManager.registerMigration(new CreateFactionsTableMigration(FactionsManager.TABLE_NAME));
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
        List<Map<String,Object>> result = this.requester.select(this.prefix+tableName, table -> {
            table.where("unique_id", id);
        });
        return result.isEmpty() ? null : result.getFirst();
    }

    @Override
    public List<Map<String, Object>> values(String table) {
        Schema schema = SchemaBuilder.select(this.prefix + table);
        try {
            return schema.executeSelect(connection, FactionsLogger::info);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
