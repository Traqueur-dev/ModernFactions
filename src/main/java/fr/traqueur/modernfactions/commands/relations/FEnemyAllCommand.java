package fr.traqueur.modernfactions.commands.relations;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.relations.Relation;
import fr.traqueur.modernfactions.api.relations.RelationsManager;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.command.CommandSender;

public class FEnemyAllCommand extends FCommand {

    private final RelationsManager relationsManager;

    public FEnemyAllCommand(FactionsPlugin plugin) {
        super(plugin, "enemyall");

        this.relationsManager = plugin.getManager(RelationsManager.class);
        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);
        Faction emitter = user.getFaction();
        if(emitter.isWilderness()) {
            user.sendMessage(Messages.NOT_IN_FACTION_MESSAGE.translate());
            return;
        }
        for(Faction receiver : this.factionsManager.getFactions()) {
            if(receiver.getId().equals(emitter.getId())) {
                continue;
            }
            Relation relation = this.relationsManager.createRelation(emitter, receiver, RelationsType.ENEMY);
            this.relationsManager.addRelation(relation);
            User leaderReceiver = this.usersManager.getUserById(receiver.getLeader()).orElse(null);
            leaderReceiver.sendMessage(Messages.RECEIVER_RELATION_COMMAND_MESSAGE.translate(Formatter.faction(emitter), Formatter.relation(RelationsType.ENEMY)));
        }

        user.sendMessage(Messages.RELATION_ALL_COMMAND_MESSAGE.translate(Formatter.relation(RelationsType.ENEMY)));
    }
}
