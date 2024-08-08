package fr.traqueur.factions.storages;

import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.DatabaseConnection;
import fr.maxlego08.sarah.HikariDatabaseConnection;
import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.storage.Storage;
import fr.traqueur.factions.api.utils.FactionsLogger;
import fr.traqueur.factions.configurations.MainConfiguration;

public class SQLStorage implements Storage {

    private final DatabaseConnection connection;

    public SQLStorage(FactionsPlugin plugin) {
        DatabaseConfiguration databaseConfiguration = plugin.getConfiguration(MainConfiguration.class).getDatabaseConfiguration();
        this.connection = new HikariDatabaseConnection(databaseConfiguration);

        if (!this.connection.isValid()) {
            FactionsLogger.severe("Unable to connect to database !");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        } else {
            FactionsLogger.info("The database connection is valid ! (" + connection.getDatabaseConfiguration().getHost() + ")");
        }
    }

    @Override
    public void onEnable() {
        this.connection.connect();
    }

    @Override
    public void onDisable() {
        this.connection.disconnect();
    }
}
