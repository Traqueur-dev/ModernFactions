package fr.traqueur.modernfactions.api.users;

import fr.traqueur.modernfactions.api.storage.Data;

import java.util.UUID;

public interface User extends Data {

    void setFaction(UUID uuid);

    UUID getFaction();

    void sendMessage(String message);

}
