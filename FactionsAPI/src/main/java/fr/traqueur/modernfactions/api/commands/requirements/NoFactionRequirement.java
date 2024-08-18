package fr.traqueur.modernfactions.api.commands.requirements;

import fr.traqueur.commands.api.requirements.Requirement;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NoFactionRequirement implements Requirement {

    private final FactionsPlugin plugin;

    public NoFactionRequirement(FactionsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean check(CommandSender commandSender) {
        if(!(commandSender instanceof Player player)) {
            return false;
        }
        User user = plugin.getManager(UsersManager.class).getUser(player).orElseThrow(() -> new IllegalArgumentException("User not found."));
        return !user.hasFaction();
    }

    @Override
    public String errorMessage() {
        return Messages.MUST_NOT_BE_IN_FACTION_MESSAGE.translate(true);
    }
}
