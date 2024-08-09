package fr.traqueur.modernfactions.users;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.dto.UserDTO;
import fr.traqueur.modernfactions.api.storage.service.Service;
import fr.traqueur.modernfactions.api.users.User;

public class UserService extends Service<User, UserDTO> {

    public UserService(FactionsPlugin plugin, String table) {
        super(plugin, UserDTO.class, table);
    }

    @Override
    public User deserialize(UserDTO dto) {
        return dto == null ? null : new FUser(this.plugin, dto.unique_id(), dto.faction());
    }


}
