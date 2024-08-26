package fr.traqueur.modernfactions.commands.roles;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.commands.requirements.FactionRequirement;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.factions.roles.Role;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.configurations.RolesConfiguration;
import org.bukkit.command.CommandSender;

public class FDemoteCommand extends FCommand {

    public FDemoteCommand(FactionsPlugin plugin) {
        super(plugin, "demote");

        this.setUsage("/f demote <player>");
        this.addArgs("user:user");
        this.addRequirements(FactionRequirement.HAVE_FACTION);

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        RolesConfiguration config = Config.getConfiguration(RolesConfiguration.class);
        User user = arguments.get("user");
        User sender = this.getUser(commandSender);
        Role role = config.getPreviousPriorityRole(user.getRole());

        if (!user.getFaction().getId().equals(sender.getFaction().getId())) {
            sender.sendMessage(Messages.NOT_IN_THE_FACTION_MESSAGE.translate());
            return;
        }

        user.setRole(role);
        sender.sendMessage(Messages.ROLE_COMMAND_MESSAGE.translate(Formatter.user(user), Formatter.format("%role%", role.name())));
        user.sendMessage(Messages.RECEIVER_ROLE_COMMAND_MESSAGE.translate(Formatter.format("%role%", role.name())));
    }
}