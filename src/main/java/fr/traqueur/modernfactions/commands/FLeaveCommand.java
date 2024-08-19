package fr.traqueur.modernfactions.commands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.commands.requirements.FactionRequirement;
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

    }
}
