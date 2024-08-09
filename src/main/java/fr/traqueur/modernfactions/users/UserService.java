package fr.traqueur.modernfactions.users;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.storage.service.Service;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.dto.UserDTO;

public class UserService extends Service<User, UserDTO> {

    public UserService(FactionsPlugin plugin, String table) {
        super(plugin, table);
    }

    @Override
    public User deserialize(UserDTO dto) {
        return new FUser(this.plugin, dto.unique_id(), dto.faction());
    }


}
