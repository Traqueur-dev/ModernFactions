package fr.traqueur.modernfactions.users;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.dto.UserDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.factions.roles.Role;
import fr.traqueur.modernfactions.api.storage.service.Service;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import fr.traqueur.modernfactions.configurations.RolesConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FUsersManager implements UsersManager {

    private final FactionsPlugin plugin;
    private final Service<User, UserDTO> service;

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
        Role defaultRole = Config.getConfiguration(RolesConfiguration.class).getDefaultRole();
        User user = new FUser(plugin, player.getUniqueId(),player.getName(), factionsManager.getWilderness().getId(), defaultRole.name());
        this.service.save(user);
        return user;
    }

    @Override
    public Optional<User> getUser(Player player) {
        return this.service.get(player.getUniqueId());
    }

    @Override
    public Optional<User> getUserById(UUID uuid) {
        return this.service.get(uuid);
    }

    @Override
    public List<User> getUsersInFaction(Faction faction) {
        return this.service.where("faction", faction.getId().toString());
    }

    @Override
    public FactionsPlugin getPlugin() {
        return this.plugin;
    }
}
