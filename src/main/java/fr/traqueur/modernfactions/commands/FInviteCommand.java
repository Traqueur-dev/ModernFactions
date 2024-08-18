package fr.traqueur.modernfactions.commands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.commands.requirements.HaveFactionRequirement;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.command.CommandSender;

public class FInviteCommand extends FCommand {

    public FInviteCommand(FactionsPlugin plugin) {
        super(plugin, "invite");

        this.setUsage("/f invite <player>");
        this.addArgs("user:user");

        this.addRequirements(new HaveFactionRequirement(plugin));

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);
        Faction faction = user.getFaction();
        User invited = arguments.get("user");

        if(invited == null) {
            user.sendMessage(Messages.USER_NOT_FOUND_MESSAGE.translate());
            return;
        }

        if(faction.hasInvitation(invited)) {
            user.sendMessage(Messages.USER_ALREADY_INVITED_MESSAGE.translate(Formatter.user(invited)));
            return;
        }

        //TODO: Add faction limit

        faction.inviteUser(invited);
        user.sendMessage(Messages.INVITATION_SENT_MESSAGE.translate(Formatter.user(invited)));
        invited.sendMessage(Messages.INVITATION_RECEIVED_MESSAGE.translate(Formatter.faction(faction)));
    }
}
