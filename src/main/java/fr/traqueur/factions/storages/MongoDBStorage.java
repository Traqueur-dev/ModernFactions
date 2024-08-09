package fr.traqueur.factions.storages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.configurations.Config;
import fr.traqueur.factions.api.storage.Storage;
import fr.traqueur.factions.api.utils.FactionsLogger;
import fr.traqueur.factions.configurations.MainConfiguration;
import fr.traqueur.factions.storages.mangodb.MongoDBConfiguration;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MongoDBStorage implements Storage {

    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;
    private final Gson gson;

    public MongoDBStorage(FactionsPlugin plugin) {
        MongoDBConfiguration mongoConfiguration = Config.getConfiguration(MainConfiguration.class).getMangoDBConfiguration();
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
        this.mongoDatabase = this.mongoClient.getDatabase(mongoConfiguration.database());
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        FactionsLogger.success("Successfully connected to the MongoDB server.");

    }

    @Override
    public void createTable(String table) {
        this.mongoDatabase.createCollection(table);
    }

    @Override
    public void save(String table, UUID id, Map<String, Object> data) {
        MongoCollection<Document> collection = this.mongoDatabase.getCollection(table);
        Document doc = Document.parse(this.gson.toJson(data));
        doc.put("_id", id);
        collection.replaceOne(Filters.eq("_id", id), doc, new ReplaceOptions().upsert(true));
    }

    @Override
    public Map<String, Object> get(String table, UUID id) {
        MongoCollection<Document> collection = this.mongoDatabase.getCollection(table);
        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc != null) {
            return this.gson.fromJson(doc.toJson(), new TypeToken<Map<String, Object>>() {}.getType());
        }
        return null;
    }

    @Override
    public void delete(String table, UUID id) {
        MongoCollection<Document> collection = this.mongoDatabase.getCollection(table);
        collection.deleteOne(Filters.eq("_id", id));
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        this.mongoClient.close();
    }
}
