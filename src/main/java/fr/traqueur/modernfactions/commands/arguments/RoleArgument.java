package fr.traqueur.modernfactions.commands.arguments;

import fr.traqueur.commands.api.arguments.ArgumentConverter;
import fr.traqueur.commands.api.arguments.TabConverter;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.factions.roles.Role;
import fr.traqueur.modernfactions.configurations.RolesConfiguration;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class RoleArgument implements ArgumentConverter<Role>, TabConverter {

    @Override
    public Role apply(String s) {
        try {
            return Config.getConfiguration(RolesConfiguration.class).getRoleByName(s);
        } catch (IllegalStateException e) {
            return null;
        }
    }

    @Override
    public List<String> onCompletion(CommandSender commandSender) {
        return Config.getConfiguration(RolesConfiguration.class)
                .getRoles()
                .stream()
                .map(Role::name)
                .collect(Collectors.toList());
    }
}
