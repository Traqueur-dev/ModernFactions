package fr.traqueur.modernfactions.commands.commands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.commands.requirements.FactionRequirement;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.command.CommandSender;

public class FDescCommand extends FCommand {

    public FDescCommand(FactionsPlugin plugin) {
        super(plugin, "description");

        this.setUsage("/f description <description>");
        this.addRequirements(FactionRequirement.HAVE_FACTION, FactionRequirement.LEADER);
        this.addArgs("description:infinite");
        this.aliases("desc");
        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);
        String desc = arguments.get("description");
        this.factionsManager.setDescription(user.getFaction(), desc);
        user.getFaction().broadcast(Messages.DESCRIPTION_CHANGED_MESSAGE
                .translate(Formatter.user(user),
                            Formatter.format("%description%", desc)));
    }
}
