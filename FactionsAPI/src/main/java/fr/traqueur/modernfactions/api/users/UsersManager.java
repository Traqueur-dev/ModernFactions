package fr.traqueur.modernfactions.api.users;

import fr.traqueur.modernfactions.api.managers.Manager;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public interface UsersManager extends Manager {

    String TABLE_NAME = "users";

    User loadOrCreateUser(Player player);

    Optional<User> getUser(Player player);

    Optional<User> getUserById(UUID uuid);
}
