package fr.traqueur.modernfactions.api.commands;

import fr.traqueur.commands.api.Command;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.users.UsersManager;

public abstract class FCommand extends Command<FactionsPlugin> {

    protected final UsersManager usersManager;
    protected final FactionsManager factionsManager;

    public FCommand(FactionsPlugin plugin, String name) {
        super(plugin, "f." + name);
        this.usersManager = plugin.getManager(UsersManager.class);
        this.factionsManager = plugin.getManager(FactionsManager.class);
    }
}
