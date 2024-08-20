package fr.traqueur.modernfactions.commands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.commands.requirements.FactionRequirement;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.command.CommandSender;

public class FLeaveCommand extends FCommand {

    public FLeaveCommand(FactionsPlugin plugin) {
        super(plugin, "leave");

        this.setUsage("/f leave");

        this.addRequirements(FactionRequirement.HAVE_FACTION, FactionRequirement.NO_LEADER);

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);
        Faction faction = user.getFaction();
        faction.removeMember(user);
        user.sendMessage(Messages.LEAVE_FACTION_MESSAGE.translate(Formatter.faction(faction)));
        faction.broadcast(Messages.USER_LEAVE_MESSAGE.translate(Formatter.user(user)));
    }
}
