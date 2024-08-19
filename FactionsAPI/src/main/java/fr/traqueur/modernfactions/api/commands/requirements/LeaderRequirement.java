package fr.traqueur.modernfactions.api.commands.requirements;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaderRequirement extends FactionRequirement {


    public LeaderRequirement(FactionsPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean check(CommandSender commandSender) {
        if(!(commandSender instanceof Player player)) {
            return false;
        }
        User user = usersManager.getUser(player).orElseThrow(() -> new IllegalArgumentException("User not found."));
        return user.isLeader();
    }

    @Override
    public String errorMessage() {
        return Messages.LEADER_MESSAGE.translate(true);
    }
}
