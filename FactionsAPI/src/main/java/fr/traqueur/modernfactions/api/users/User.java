package fr.traqueur.modernfactions.api.users;

import fr.traqueur.modernfactions.api.dto.UserDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.roles.Role;
import fr.traqueur.modernfactions.api.storage.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface User extends Data<UserDTO> {

    void setFaction(UUID uuid);

    void setRole(Role role);

    Faction getFaction();

    Role getRole();

    boolean isLeader();

    void sendMessage(String message);

    String getName();

    Location getLocation();

    void sendActionBar(String message);

    void sendTitle(String notificationMessage, String subtitle, int fadeIn, int stay, int fadeOut);

    boolean isOnline();

    Player getPlayer();

    boolean hasFaction();
}
