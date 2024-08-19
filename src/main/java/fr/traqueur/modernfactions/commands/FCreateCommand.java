package fr.traqueur.modernfactions.commands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.commands.requirements.FactionRequirement;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.events.FactionCreateEvent;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.exceptions.FactionAlreadyExistsException;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.configurations.RolesConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class FCreateCommand extends FCommand {

    private final RolesConfiguration rolesConfiguration;

    public FCreateCommand(FactionsPlugin plugin) {
        super(plugin, "create");

        this.setUsage("/f create <name>");
        this.addArgs("name:string");

        this.rolesConfiguration = Config.getConfiguration(RolesConfiguration.class);

        this.addRequirements(FactionRequirement.HAVE_NO_FACTION);

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);
        String name = arguments.get("name");

        FactionCreateEvent event = new FactionCreateEvent(user, name);
        this.getPlugin().getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()) {
            return;
        }

        try {
            Faction faction = factionsManager.createFaction(name, user.getId());
            user.setFaction(faction.getId());
            user.setRole(rolesConfiguration.getMaxPriorityRole());
            user.sendMessage(Messages.CREATE_FACTION_MESSAGE.translate(Formatter.faction(faction)));
            Bukkit.getOnlinePlayers().stream().map(usersManager::getUser).forEach(user1 -> {
                user1.ifPresent(value -> value.sendMessage(Messages.BROADCAST_CREATION_MESSAGE.translate(Formatter.faction(faction), Formatter.user(user))));
            });
        } catch (FactionAlreadyExistsException e) {
            user.sendMessage(Messages.FACTION_EXISTS_MESSAGE.translate(Formatter.faction(e.getFaction())));
        }

    }
}
