package fr.traqueur.modernfactions.api.commands.requirements;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.messages.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NoFactionRequirement extends HaveFactionRequirement {

    public NoFactionRequirement(FactionsPlugin plugin) {
       super(plugin);
    }

    @Override
    public boolean check(CommandSender commandSender) {
        if(!(commandSender instanceof Player)) {
            return false;
        }
        return !super.check(commandSender);
    }

    @Override
    public String errorMessage() {
        return Messages.MUST_NOT_BE_IN_FACTION_MESSAGE.translate(true);
    }
}
