package fr.traqueur.modernfactions.commands.arguments;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class FactionMembersArgument extends UserArgument {

    private final FactionsManager factionsManager;

    public FactionMembersArgument(FactionsPlugin plugin) {
        super(plugin);
        this.factionsManager = plugin.getManager(FactionsManager.class);
    }

    @Override
    public List<String> onCompletion(CommandSender commandSender) {
        if(commandSender instanceof Player player) {
            Faction faction = this.factionsManager.getFactionByPlayer(player).orElseThrow(() -> new IllegalArgumentException("You must be in a faction to use this command."));
            return this.usersManager.getUsersInFaction(faction).stream().map(User::getName).collect(Collectors.toList());
        }

        return super.onCompletion(commandSender);
    }
}
