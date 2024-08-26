package fr.traqueur.modernfactions.commands.lands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.commands.requirements.FactionRequirement;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class FUnclaimCommand extends FCommand {

    public FUnclaimCommand(FactionsPlugin plugin) {
        super(plugin, "unclaim");

        this.setUsage("/f unclaim");

        this.addRequirements(FactionRequirement.HAVE_FACTION);

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);
        Chunk chunk = user.getLocation().getChunk();
        Faction faction = user.getFaction();
        Faction landOwner = this.landsManager.getLandOwner(chunk);

        if(!landOwner.getId().equals(faction.getId())) {
            user.sendMessage(Messages.LANDS_UNCLAIM_ERROR_MESSAGE.translate());
            return;
        }

        faction.getHome().ifPresent(home -> {
            if(home.getChunk().equals(chunk)) {
                faction.setHome(null);
            }
        });

        this.landsManager.claimLand(user.getPlayer().getLocation().getChunk(), this.factionsManager.getWilderness());
        user.sendMessage(Messages.LANDS_UNCLAIM_MESSAGE.translate());
    }
}
