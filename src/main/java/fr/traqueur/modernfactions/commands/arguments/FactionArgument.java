package fr.traqueur.modernfactions.commands.arguments;

import fr.traqueur.commands.api.arguments.ArgumentConverter;
import fr.traqueur.commands.api.arguments.TabConverter;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FactionArgument implements ArgumentConverter<Faction>, TabConverter {

    private final FactionsManager factionsManager;
    private final UsersManager usersManager;

    public FactionArgument(FactionsPlugin plugin) {
        this.factionsManager = plugin.getManager(FactionsManager.class);
        this.usersManager = plugin.getManager(UsersManager.class);
    }

    @Override
    public Faction apply(String s) {
        return factionsManager.getFaction(s).orElse(null);
    }

    @Override
    public List<String> onCompletion(CommandSender commandSender) {
        Set<Faction> factions = factionsManager.getFactions();
        if(!commandSender.isOp()) {
            factions = factions.stream().filter(faction -> !faction.isSystem()).collect(Collectors.toSet());
        }
        return factions.stream().map(Faction::getName).collect(Collectors.toList());
    }
}
