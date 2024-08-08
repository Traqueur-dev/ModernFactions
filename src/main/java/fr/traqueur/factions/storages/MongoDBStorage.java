package fr.traqueur.factions.storages;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.configurations.Configuration;
import fr.traqueur.factions.api.storage.Storage;
import fr.traqueur.factions.api.utils.FactionsLogger;
import fr.traqueur.factions.configurations.MainConfiguration;
import fr.traqueur.factions.storages.mangodb.MongoDBConfiguration;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.concurrent.TimeUnit;

public class MongoDBStorage implements Storage {

    private final MongoClient mongoClient;

    public MongoDBStorage(FactionsPlugin plugin) {
        MongoDBConfiguration mongoConfiguration = Configuration.getConfiguration(MainConfiguration.class).getMangoDBConfiguration();
        String urlConfig = "mongodb://" + mongoConfiguration.username() + ":" + mongoConfiguration.password() +
                "@" + mongoConfiguration.host() + ":" + mongoConfiguration.port() +
                "/" + mongoConfiguration.database() + "?authSource=" + mongoConfiguration.authDatabase();
        ConnectionString connectionString = new ConnectionString(urlConfig);

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        CodecRegistry pojoCodec = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodec
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder -> builder.maxSize(5))
                .applyToSocketSettings(builder -> builder.connectTimeout(5, TimeUnit.SECONDS))
                .serverApi(serverApi)
                .codecRegistry(codecRegistry)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .retryWrites(false)
                .build();

        try {
            this.mongoClient = MongoClients.create(settings);
        } catch (Exception exception) {
            FactionsLogger.severe("Unable to connect to the MongoDB server.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            throw new RuntimeException(exception);
        }
        FactionsLogger.success("Successfully connected to the MongoDB server.");

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        this.mongoClient.close();
    }
}
