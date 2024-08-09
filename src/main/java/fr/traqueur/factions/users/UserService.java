package fr.traqueur.factions.users;

import fr.traqueur.factions.api.FactionsPlugin;
import fr.traqueur.factions.api.storage.service.Service;
import fr.traqueur.factions.api.users.User;

import java.util.Map;

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
        return map == null ? null : new FUser(plugin, map);
    }
}
