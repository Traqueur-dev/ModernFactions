package fr.traqueur.modernfactions.commands.relations;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.RelationCommand;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import org.bukkit.command.CommandSender;

public class FEnemyAllCommand extends RelationCommand {

    public FEnemyAllCommand(FactionsPlugin plugin) {
        super(plugin, "enemyall");
        this.setGameOnly(true);
    }

    public void executeCommand(CommandSender commandSender, Arguments arguments) {
        for(Faction receiver : this.factionsManager.getFactions()) {
            if(receiver.getId().equals(emitter.getId())) {
                continue;
            }
            relation(emitter, receiver, RelationsType.ENEMY,true);
        }
        emitter.broadcast(Messages.RELATION_ALL_COMMAND_MESSAGE.translate(Formatter.relation(RelationsType.ENEMY)));
    }
}
