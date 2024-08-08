package fr.traqueur.factions.users;

import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.storage.service.Service;
import fr.traqueur.factions.api.users.User;
import fr.traqueur.factions.api.users.UsersManager;
import org.bukkit.entity.Player;

import java.util.Optional;

public class FUsersManager implements UsersManager {

    public static final String TABLE_NAME = "users";

    private final FactionsPlugin plugin;
    private final Service<User> service;

    public FUsersManager(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.service = new UserService(plugin, TABLE_NAME);
    }

    public User loadOrCreateUser(Player player) {
        Optional<User> optional = this.service.get(player.getUniqueId());
        if(optional.isPresent()) {
            return optional.get();
        }
        User user = new FUser(plugin, player);
        this.service.save(user);
        return user;
    }

    @Override
    public FactionsPlugin getPlugin() {
        return null;
    }
}
