package fr.traqueur.modernfactions.commands.relations;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.RelationCommand;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import org.bukkit.command.CommandSender;

public class FEnemyCommand extends RelationCommand {

    public FEnemyCommand(FactionsPlugin plugin) {
        super(plugin, "enemy");

        this.addArgs("receiver:faction");
        this.setGameOnly(true);
    }

    public void executeCommand(CommandSender commandSender, Arguments arguments) {
        Faction receiver = arguments.get("receiver");
        if (emitter.getId().equals(receiver.getId())) {
            user.sendMessage(Messages.SAME_FACTION_MESSAGE.translate());
            return;
        }

        RelationsType type = this.relationsManager.getRelationBetween(emitter, receiver);
        if (type == RelationsType.ENEMY) {
            user.sendMessage(Messages.ALREADY_RELATION_MESSAGE.translate(Formatter.relation(RelationsType.ENEMY), Formatter.faction(receiver)));
        } else {
            relation(emitter, receiver, RelationsType.ENEMY, false);
        }
    }
}
