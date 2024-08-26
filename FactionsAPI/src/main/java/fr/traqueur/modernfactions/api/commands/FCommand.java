package fr.traqueur.modernfactions.api.commands;

import fr.traqueur.commands.api.Command;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.lands.LandsManager;
import fr.traqueur.modernfactions.api.relations.RelationsManager;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

public abstract class FCommand extends Command<FactionsPlugin> {

    protected final UsersManager usersManager;
    protected final FactionsManager factionsManager;
    protected final LandsManager landsManager;
    protected final RelationsManager relationsManager;

    public FCommand(FactionsPlugin plugin, String name) {
        super(plugin, "f." + name);
        this.usersManager = plugin.getManager(UsersManager.class);
        this.factionsManager = plugin.getManager(FactionsManager.class);
        this.relationsManager = plugin.getManager(RelationsManager.class);
        this.landsManager = plugin.getManager(LandsManager.class);
    }

    public User getUser(CommandSender sender) {
        if(!(sender instanceof Player player)) {
            throw new IllegalArgumentException("Command sender must be a player.");
        }
        return this.usersManager.getUser(player).orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    public void aliases(String... aliases) {
        this.addAlias(Stream.of(aliases).map(alias -> "f." + alias).toArray(String[]::new));
    }

    public void permission(Permissions permissions) {
        this.setPermission(permissions.getPermission());
    }
}
