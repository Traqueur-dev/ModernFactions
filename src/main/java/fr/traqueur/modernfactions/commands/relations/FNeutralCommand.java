package fr.traqueur.modernfactions.commands.relations;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.RelationCommand;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.command.CommandSender;

public class FNeutralCommand extends RelationCommand {

    public FNeutralCommand(FactionsPlugin plugin) {
        super(plugin, "neutral");
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
        switch (oldRelation) {
            case ALLY, TRUCE -> {
                relation(emitter, receiver, RelationsType.NEUTRAL, false);
            }
            case NEUTRAL -> {
                user.sendMessage(Messages.ALREADY_RELATION_MESSAGE.translate(Formatter.relation(RelationsType.NEUTRAL), Formatter.faction(receiver)));
            }
            case ENEMY -> {
                emitter.getRelationWish(receiver, RelationsType.NEUTRAL).ifPresentOrElse(acceptRelation(emitter, receiver, RelationsType.NEUTRAL), wishRelation(emitter, receiver, user, RelationsType.NEUTRAL));
            }
        }
    }
}
