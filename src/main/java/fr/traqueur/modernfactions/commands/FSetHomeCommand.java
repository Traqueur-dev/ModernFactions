package fr.traqueur.modernfactions.commands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.commands.requirements.FactionRequirement;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.command.CommandSender;

public class FSetHomeCommand extends FCommand {

    public FSetHomeCommand(FactionsPlugin plugin) {
        super(plugin, "sethome");

        this.setUsage("/f sethome");
        this.addRequirements(FactionRequirement.HAVE_FACTION);

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);

        if(!this.landsManager.isInsideFactionLand(user)) {
            user.sendMessage(Messages.SETHOME_COMMAND_ERROR_MESSAGE.translate());
            return;
        }

        user.getFaction().setHome(user.getLocation());
        user.sendMessage(Messages.SETHOME_COMMAND_MESSAGE.translate());
    }
}
