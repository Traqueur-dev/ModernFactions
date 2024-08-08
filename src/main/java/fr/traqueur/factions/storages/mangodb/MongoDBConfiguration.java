package fr.traqueur.factions.storages.mangodb;

public record MongoDBConfiguration(String host, int port, String database, String username, String password, String authDatabase) {
}
