package fr.traqueur.modernfactions.users;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.storage.service.Service;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.entity.Player;

import java.util.Optional;

public class FUsersManager implements UsersManager {

    private final FactionsPlugin plugin;
    private final Service<User> service;

    public FUsersManager(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.service = new UserService(plugin, TABLE_NAME);
    }

    public User loadOrCreateUser(Player player) {
        FactionsManager factionsManager = this.plugin.getManager(FactionsManager.class);
        Optional<User> optional = this.service.get(player.getUniqueId());
        if(optional.isPresent()) {
            return optional.get();
        }
        User user = new FUser(plugin, player.getUniqueId(),factionsManager.getWilderness().getId());
        this.service.save(user);
        return user;
    }

    @Override
    public Optional<User> getUser(Player player) {
        return this.service.get(player.getUniqueId());
    }

    @Override
    public FactionsPlugin getPlugin() {
        return this.plugin;
    }
}
