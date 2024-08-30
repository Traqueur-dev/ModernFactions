package fr.traqueur.modernfactions.commands.commands.roles;

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

public class FRoleCommand extends FCommand {

    public FRoleCommand(FactionsPlugin plugin) {
        super(plugin, "role");

        this.setUsage("/f role <player> <role>");
        this.addArgs("user:faction_member", "role:role");
        this.addRequirements(FactionRequirement.HAVE_FACTION);

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        RolesConfiguration config = Config.getConfiguration(RolesConfiguration.class);

        User user = arguments.get("user");
        Role role = arguments.get("role");
        User sender = this.getUser(commandSender);

        if (!user.getFaction().getId().equals(sender.getFaction().getId())) {
            sender.sendMessage(Messages.NOT_IN_THE_FACTION_MESSAGE.translate());
            return;
        }

        if (config.getMaxPriorityRole().equals(role)) {
            sender.sendMessage(Messages.CANT_SET_ROLE_MESSAGE.translate());
            return;
        }

        user.setRole(role);
        sender.sendMessage(Messages.ROLE_COMMAND_MESSAGE.translate(Formatter.user(user), Formatter.format("%role%", role.name())));
        user.sendMessage(Messages.RECEIVER_ROLE_COMMAND_MESSAGE.translate(Formatter.format("%role%", role.name())));
    }
}
