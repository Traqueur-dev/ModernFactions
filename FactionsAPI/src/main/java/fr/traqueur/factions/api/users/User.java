package fr.traqueur.factions.api.users;

import fr.traqueur.factions.api.storage.Data;

public interface User extends Data {

    void sendMessage(String message);

}
