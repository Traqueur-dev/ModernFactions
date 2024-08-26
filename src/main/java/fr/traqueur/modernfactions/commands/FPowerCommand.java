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

        this.aliases("p");

        this.setUsage("/f power");
        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);
        int maxPower = Config.getConfiguration(MainConfiguration.class).getMaxUserPower();
        int minPower = Config.getConfiguration(MainConfiguration.class).getMinUserPower();
        user.sendMessage(Messages.POWER_USER_MESSAGE.translate(
                Formatter.format("%power%", (plugin) -> this.getCodeColorFromPower(user.getPower(), maxPower, minPower) + user.getPower() + "<gray>"),
                Formatter.format("%max_power%", (plugin) -> "<green>" + maxPower)));
    }

    private String getCodeColorFromPower(int power, int maxPower, int minPower) {
        int middle = (maxPower - minPower) / 2 + minPower;
        if (power >= middle) {
            return "<green>";
        } else if (power > minPower) {
            return "<yellow>";
        } else {
            return "<red>";
        }
    }
}
