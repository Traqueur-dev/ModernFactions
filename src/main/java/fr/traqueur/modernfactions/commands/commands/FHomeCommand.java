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

public class FHomeCommand extends FCommand {
    public FHomeCommand(FactionsPlugin plugin) {
        super(plugin, "home");

        this.setUsage("/f home");
        this.addRequirements(FactionRequirement.HAVE_FACTION);

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);
        Faction faction = user.getFaction();

        if(faction.getHome().isEmpty()) {
            user.sendMessage(Messages.HOME_NOT_SET_MESSAGE.translate());
            return;
        }

        int seconds = Config.getConfiguration(MainConfiguration.class).getDelayTeleportHome();
        this.usersManager.startTeleportation(user, seconds);
        user.sendMessage(Messages.START_HOME_TELEPORT_MESSAGE.translate(Formatter.format("%time%", this.usersManager.durationWords(seconds))));
    }
}
