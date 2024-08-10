package fr.traqueur.modernfactions.api.users;

import fr.traqueur.modernfactions.api.dto.UserDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.roles.Role;
import fr.traqueur.modernfactions.api.storage.Data;

import java.util.UUID;

public interface User extends Data<UserDTO> {

    void setFaction(UUID uuid);

    void setRole(Role role);

    Faction getFaction();

    Role getRole();

    void sendMessage(String message);

    String getName();
}
