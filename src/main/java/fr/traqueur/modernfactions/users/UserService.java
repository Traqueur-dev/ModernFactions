package fr.traqueur.modernfactions.users;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.dto.UserDTO;
import fr.traqueur.modernfactions.api.storage.service.Service;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.configurations.MainConfiguration;

public class UserService extends Service<User, UserDTO> {

    public UserService(FactionsPlugin plugin, String table) {
        super(plugin, UserDTO.class, table);
    }

    @Override
    public User deserialize(UserDTO dto) {
        int defaultPower = Config.getConfiguration(MainConfiguration.class).getDefaultUserPower();
        return dto == null ? null : new FUser(this.plugin, dto.unique_id(), dto.name(), dto.faction(), dto.role(), dto.power() == null ? defaultPower : dto.power());
    }


}
