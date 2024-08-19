package fr.traqueur.modernfactions.api.commands.requirements;

import fr.traqueur.commands.api.requirements.Requirement;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class FactionRequirement implements Requirement {

    public static final HaveFactionRequirement HAVE_FACTION;
    public static final NoFactionRequirement HAVE_NO_FACTION;
    public static final LeaderRequirement LEADER;
    public static final NoLeaderRequirement NO_LEADER;

    static {
        FactionsPlugin plugin = JavaPlugin.getPlugin(FactionsPlugin.class);

        HAVE_FACTION = new HaveFactionRequirement(plugin);
        HAVE_NO_FACTION = new NoFactionRequirement(plugin);
        LEADER = new LeaderRequirement(plugin);
        NO_LEADER = new NoLeaderRequirement(plugin);

    }

    protected final FactionsPlugin plugin;
    protected final UsersManager usersManager;

    public FactionRequirement(FactionsPlugin plugin) {
       this.plugin = plugin;
       this.usersManager = plugin.getManager(UsersManager.class);
    }

}
