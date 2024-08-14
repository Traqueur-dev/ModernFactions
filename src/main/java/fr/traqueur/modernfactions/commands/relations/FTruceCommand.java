package fr.traqueur.modernfactions.commands.relations;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.RelationCommand;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import org.bukkit.command.CommandSender;

public class FTruceCommand extends RelationCommand {

    public FTruceCommand(FactionsPlugin plugin) {
        super(plugin, "truce");
        this.addArgs("receiver:faction");
        this.setGameOnly(true);
    }

    public void executeCommand(CommandSender commandSender, Arguments arguments) {
        Faction receiver = arguments.get("receiver");
        if (emitter.getId().equals(receiver.getId())) {
            user.sendMessage(Messages.SAME_FACTION_MESSAGE.translate());
            return;
        }

        RelationsType oldRelation = this.relationsManager.getRelationBetween(emitter, receiver);
        if (oldRelation == RelationsType.TRUCE) {
            user.sendMessage(Messages.ALREADY_RELATION_MESSAGE.translate(Formatter.relation(oldRelation)));
            return;
        }

        if(oldRelation == RelationsType.ALLY) {
            relation(emitter, receiver, RelationsType.TRUCE, false);
            return;
        }

        emitter.getRelationWish(receiver, RelationsType.TRUCE).ifPresentOrElse(acceptRelation(emitter, receiver, RelationsType.TRUCE), wishRelation(emitter, receiver, user, RelationsType.TRUCE));
    }
}
