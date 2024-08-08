package fr.traqueur.factions.users;

import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.storage.service.Service;
import fr.traqueur.factions.api.users.User;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;

public class UserService extends Service<User> {

    public UserService(FactionsPlugin plugin, String table) {
        super(plugin, table);
    }

    @Override
    public Map<String, Object> serialize(User data) {
        return Map.of("unique_id", data.getId());
    }

    @Override
    public User deserialize(Map<String, Object> map) {
        return map == null ? null : new FUser(plugin, Bukkit.getPlayer(UUID.fromString((String) map.get("unique_id"))));
    }
}
