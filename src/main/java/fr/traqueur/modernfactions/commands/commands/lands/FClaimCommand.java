package fr.traqueur.modernfactions.commands.commands.lands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.commands.requirements.FactionRequirement;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class FClaimCommand extends FCommand {

    public FClaimCommand(FactionsPlugin plugin) {
        super(plugin, "claim");

        this.setUsage("/f claim");

        this.addRequirements(FactionRequirement.HAVE_FACTION);

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);
        Chunk chunk = user.getLocation().getChunk();
        Faction faction = user.getFaction();
        Faction landOwner = this.landsManager.getLandOwner(chunk);

        if(!this.landsManager.canClaimLand(chunk, faction)) {
            user.sendMessage(Messages.LANDS_CLAIM_ERROR_MESSAGE.translate());
            return;
        }

        this.landsManager.claimLand(user.getPlayer().getLocation().getChunk(), user.getFaction());
        user.sendMessage(Messages.LANDS_CLAIM_MESSAGE.translate());

        if(!landOwner.isWilderness()) {
            landOwner.broadcast(Messages.NOTIFY_LANDS_CLAIM_MESSAGE.translate(Formatter.faction(faction)));
        }

    }
}
