package fr.traqueur.modernfactions.users;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.storage.service.Service;
import fr.traqueur.modernfactions.api.users.User;
import java.util.Map;

public class UserService extends Service<User> {

    public UserService(FactionsPlugin plugin, String table) {
        super(plugin, table);
    }

    @Override
    public User deserialize(Map<String, Object> map) {
        return map == null ? null : new FUser(plugin, map);
    }
}
