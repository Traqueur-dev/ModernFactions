package fr.traqueur.modernfactions.commands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.exceptions.FactionAlreadyExistsException;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.configurations.RolesConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class FCreateCommand extends FCommand {

    public FCreateCommand(FactionsPlugin plugin) {
        super(plugin, "create");

        this.setUsage("/f create <name>");
        this.addArgs("name:string");

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        Optional<User> userOpt = usersManager.getUser((Player) commandSender);
        if(userOpt.isEmpty()) {
            commandSender.sendMessage("§cErreur: Impossible de charger votre profil.");
            return;
        }
        User user = userOpt.get();
        String name = arguments.get("name");
        if (!user.getFaction().isWilderness()) {
            user.sendMessage("<red>Vous êtes déjà dans une faction.");
            return;
        }

        try {
            Faction faction = factionsManager.createFaction(name, user.getId());
            user.setFaction(faction.getId());
            user.setRole(Config.getConfiguration(RolesConfiguration.class).getMaxPriorityRole());
            user.sendMessage("<green>Vous avez créé la faction " + faction.getName() + ".");
            Bukkit.getOnlinePlayers().stream().map(usersManager::getUser).forEach(user1 -> {
                user1.ifPresent(value -> value.sendMessage("<green>" + user.getName() + " a créé la faction " + faction.getName() + "."));
            });
        } catch (FactionAlreadyExistsException e) {
            user.sendMessage("<red>Une faction avec ce nom existe déjà.");
        }

    }
}
