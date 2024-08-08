package fr.traqueur.factions.api.users;

import fr.traqueur.factions.api.managers.Manager;
import org.bukkit.entity.Player;

public interface UsersManager extends Manager {

    User loadOrCreateUser(Player player);
}
