package fr.traqueur.modernfactions.api.commands.requirements;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.messages.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NoLeaderRequirement extends LeaderRequirement {

    public NoLeaderRequirement(FactionsPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean check(CommandSender sender) {
        if(!(sender instanceof Player)) {
            return false;
        }
        return !super.check(sender) ;
    }

    @Override
    public String errorMessage() {
       return Messages.NO_LEADER_MESSAGE.translate(true);
    }
}
