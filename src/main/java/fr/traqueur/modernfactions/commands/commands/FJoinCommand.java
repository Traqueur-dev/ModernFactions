package fr.traqueur.modernfactions.commands.commands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.commands.requirements.FactionRequirement;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.configurations.MainConfiguration;
import org.bukkit.command.CommandSender;

public class FJoinCommand extends FCommand {

    public FJoinCommand(FactionsPlugin plugin) {
        super(plugin, "join");

        this.setUsage("/f join <faction>");
        this.addArgs("faction:faction");

        this.addRequirements(FactionRequirement.HAVE_NO_FACTION);

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);
        Faction faction = arguments.get("faction");

        if(faction == null) {
            user.sendMessage(Messages.FACTION_NOT_FOUND_MESSAGE.translate());
            return;
        }

        if(!faction.hasInvitation(user)) {
            user.sendMessage(Messages.USER_NOT_INVITED_MESSAGE.translate(Formatter.faction(faction)));
            return;
        }

        int usersInFaction = this.usersManager.getUsersInFaction(faction).size();
        int maxUsersInFaction = Config.getConfiguration(MainConfiguration.class).getMaxUsersPerFaction();
        if(usersInFaction >= maxUsersInFaction) {
            user.sendMessage(Messages.FACTION_FULL_MESSAGE.translate());
            return;
        }

        this.factionsManager.joinFaction(user, faction);
        user.sendMessage(Messages.FACTION_JOINED_MESSAGE.translate(Formatter.faction(faction)));
        faction.broadcast(Messages.USER_JOIN_MESSAGE.translate(Formatter.user(user)));
    }
}
