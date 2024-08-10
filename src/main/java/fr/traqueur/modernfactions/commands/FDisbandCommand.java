package fr.traqueur.modernfactions.commands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.commands.requirements.LeaderRequirement;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import fr.traqueur.modernfactions.factions.roles.RolesConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FDisbandCommand extends FCommand {

    public FDisbandCommand(FactionsPlugin plugin) {
        super(plugin, "disband");

        this.setUsage("/f disband");
        this.addRequirements(new LeaderRequirement(plugin));
        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        Player player = (Player) commandSender;
        User user = usersManager.getUser(player).orElseThrow(() -> new IllegalArgumentException("User not found."));
        Faction faction = user.getFaction();
        factionsManager.removeFaction(faction);
        user.setFaction(factionsManager.getWilderness().getId());
        user.setRole(Config.getConfiguration(RolesConfiguration.class).getDefaultRole());
        user.sendMessage("<green>Vous avez dissous votre faction.");
        Bukkit.getOnlinePlayers().stream().map(usersManager::getUser).forEach(user1 -> {
            user1.ifPresent(value -> value.sendMessage("<red>" + user.getName() + " a dissout la faction " + faction.getName() + "."));
        });
    }
}
