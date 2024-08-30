package fr.traqueur.modernfactions.commands.commands.admin;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.commands.Permissions;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.configurations.MainConfiguration;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FSetPowerCommand extends FCommand {

    public FSetPowerCommand(FactionsPlugin plugin) {
        super(plugin, "setpower");

        this.addArgs("user:user");
        this.addArgs("power:int", (sender) -> {
            MainConfiguration config = Config.getConfiguration(MainConfiguration.class);
            int minPower = config.getMinUserPower();
            int maxPower = config.getMaxUserPower();

            int[] powers = new int[maxPower - minPower + 1];
            for (int i = 0; i < powers.length; i++) {
                powers[i] = minPower + i;
            }
            return Arrays.stream(powers).mapToObj(String::valueOf).collect(Collectors.toList());
        });
        this.setUsage("/f setpower <user> <power>");

        this.permission(Permissions.SET_POWER);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = arguments.get("user");
        int power = arguments.get("power");
        user.setPower(power);
        getPlugin().getMessageUtils().sendMessage(commandSender, Messages.SET_POWER_COMMAND_MESSAGE
                .translate(Formatter.user(user),
                            Formatter.format("%power%", power)));
    }
}
