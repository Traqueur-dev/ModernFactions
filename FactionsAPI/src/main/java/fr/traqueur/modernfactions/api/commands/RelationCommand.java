package fr.traqueur.modernfactions.api.commands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.requirements.FactionRequirement;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.relations.Relation;
import fr.traqueur.modernfactions.api.relations.RelationWish;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.command.CommandSender;

import java.util.function.Consumer;

public abstract class RelationCommand extends FCommand {

    protected User user;
    protected Faction emitter;

    public RelationCommand(FactionsPlugin plugin, String name) {
        super(plugin, name);

        this.addRequirements(FactionRequirement.HAVE_FACTION);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);
        Faction emitter = user.getFaction();
        this.user = user;
        this.emitter = emitter;
        this.executeCommand(commandSender, arguments);
    }

    public abstract void executeCommand(CommandSender commandSender, Arguments arguments);

    protected void relation(Faction emitter, Faction receiver, RelationsType type, boolean all) {
        this.relationsManager.getRelation(emitter, receiver).ifPresent(relation -> {
            this.relationsManager.removeRelation(relation);
        });

        Relation relation = this.relationsManager.createRelation(emitter, receiver, type);
        this.relationsManager.addRelation(relation);

        if(!all) {
            emitter.broadcast(Messages.RELATION_COMMAND_MESSAGE.translate(Formatter.faction(receiver), Formatter.relation(type)));
        }
        receiver.broadcast(Messages.RECEIVER_RELATION_COMMAND_MESSAGE.translate(Formatter.faction(emitter), Formatter.relation(type)));
    }

    protected Consumer<RelationWish> acceptRelation(Faction emitter, Faction receiver, RelationsType type) {
        return relationWish -> {
            this.relationsManager.getRelation(emitter, receiver).ifPresent(relation -> {
                this.relationsManager.removeRelation(relation);
            });
            Relation relation = relationWish.conclude(emitter);
            receiver.removeRelationWish(emitter, type);
            emitter.removeRelationWish(receiver, type);
            this.relationsManager.addRelation(relation);
            emitter.broadcast(Messages.RELATION_COMMAND_MESSAGE.translate(Formatter.faction(receiver), Formatter.relation(type)));
            receiver.broadcast(Messages.RECEIVER_RELATION_COMMAND_MESSAGE.translate(Formatter.faction(emitter), Formatter.relation(type)));
        };
    }

    protected Runnable wishRelation(Faction  emitter, Faction receiver, User user, RelationsType type) {
        return () -> {
            receiver.addRelationWish(emitter, type);
            user.sendMessage(Messages.WISH_RELATION_COMMAND_MESSAGE.translate(Formatter.relation(type), Formatter.faction(receiver)));
            receiver.broadcast(Messages.RECEIVER_WISH_RELATION_COMMAND_MESSAGE.translate(Formatter.relation(type), Formatter.faction(emitter)));
        };
    }
}
