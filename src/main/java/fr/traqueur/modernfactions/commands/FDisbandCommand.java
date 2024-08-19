package fr.traqueur.modernfactions.commands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.commands.requirements.FactionRequirement;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.events.FactionDisbandEvent;
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
        this.addRequirements(FactionRequirement.LEADER, FactionRequirement.HAVE_FACTION);

        this.rolesConfiguration = Config.getConfiguration(RolesConfiguration.class);

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);
        Faction faction = user.getFaction();

        FactionDisbandEvent event = new FactionDisbandEvent(user, faction);
        this.getPlugin().getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()) {
            return;
        }

        usersManager.getUsersInFaction(faction).forEach(userInFaction -> {
            factionsManager.getWilderness().addMember(userInFaction);
        });

        faction.broadcast(Messages.DISBAND_FACTION_MESSAGE.translate(Formatter.user(user), Formatter.faction(faction)));
        Bukkit.getOnlinePlayers().stream().map(usersManager::getUser).forEach(user1 -> {
            user1.ifPresent(value -> value.sendMessage(Messages.BROADCAST_DISBAND_MESSAGE.translate(Formatter.faction(faction), Formatter.user(user))));
        });
        factionsManager.removeFaction(faction);
    }
}
