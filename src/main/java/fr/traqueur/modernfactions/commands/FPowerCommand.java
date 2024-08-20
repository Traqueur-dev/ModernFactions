package fr.traqueur.modernfactions.commands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.configurations.MainConfiguration;
import org.bukkit.command.CommandSender;

public class FPowerCommand extends FCommand {

    public FPowerCommand(FactionsPlugin plugin) {
        super(plugin, "power");

        this.setUsage("/f power");
        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);
        int maxPower = Config.getConfiguration(MainConfiguration.class).getMaxUserPower();
        user.sendMessage(Messages.POWER_USER_MESSAGE.translate(
                Formatter.format("%power%", (plugin) -> String.valueOf(user.getPower())),
                Formatter.format("%max_power%", (plugin) -> String.valueOf(maxPower))));
    }
}
