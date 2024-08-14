package fr.traqueur.modernfactions.commands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.commands.requirements.LeaderRequirement;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.configurations.RolesConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class FDisbandCommand extends FCommand {

    private final RolesConfiguration rolesConfiguration;

    public FDisbandCommand(FactionsPlugin plugin) {
        super(plugin, "disband");

        this.setUsage("/f disband");
        this.addRequirements(new LeaderRequirement(plugin));

        this.rolesConfiguration = Config.getConfiguration(RolesConfiguration.class);

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);
        Faction faction = user.getFaction();
        user.setFaction(factionsManager.getWilderness().getId());
        user.setRole(rolesConfiguration.getDefaultRole());
        faction.broadcast(Messages.DISBAND_FACTION_MESSAGE.translate(Formatter.user(user), Formatter.faction(faction)));
        Bukkit.getOnlinePlayers().stream().map(usersManager::getUser).forEach(user1 -> {
            user1.ifPresent(value -> value.sendMessage(Messages.BROADCAST_DISBAND_MESSAGE.translate(Formatter.faction(faction), Formatter.user(user))));
        });
        factionsManager.removeFaction(faction);
    }
}
