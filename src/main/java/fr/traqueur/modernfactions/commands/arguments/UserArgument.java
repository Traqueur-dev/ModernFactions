package fr.traqueur.modernfactions.commands.arguments;

import fr.traqueur.commands.api.arguments.ArgumentConverter;
import fr.traqueur.commands.api.arguments.TabConverter;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserArgument implements ArgumentConverter<User>, TabConverter {

    private final UsersManager usersManager;

    public UserArgument(FactionsPlugin plugin) {
        this.usersManager = plugin.getManager(UsersManager.class);
    }

    @Override
    public User apply(String s) {
        return usersManager.getUserByName(s).orElse(null);
    }

    @Override
    public List<String> onCompletion(CommandSender commandSender) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }
}
