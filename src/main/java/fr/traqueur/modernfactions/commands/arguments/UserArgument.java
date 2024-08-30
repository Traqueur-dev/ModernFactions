package fr.traqueur.modernfactions.commands.arguments;

import fr.traqueur.commands.api.arguments.ArgumentConverter;
import fr.traqueur.commands.api.arguments.TabConverter;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class UserArgument implements ArgumentConverter<User>, TabConverter {

    protected final UsersManager usersManager;

    public UserArgument(FactionsPlugin plugin) {
        this.usersManager = plugin.getManager(UsersManager.class);
    }

    @Override
    public User apply(String s) {
        return usersManager.getUserByName(s).orElse(null);
    }

    @Override
    public List<String> onCompletion(CommandSender commandSender) {
        return usersManager.getUsers().stream().map(User::getName).collect(Collectors.toList());
    }
}
