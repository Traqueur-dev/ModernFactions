package fr.traqueur.modernfactions.commands.commands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.commands.requirements.FactionRequirement;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.configurations.MainConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FInviteCommand extends FCommand {

    public FInviteCommand(FactionsPlugin plugin) {
        super(plugin, "invite");

        this.setUsage("/f invite <player>");
        this.addArgs("user:user", (commandSender -> {
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            if(commandSender instanceof Player player) {
                players = players.stream().filter(p -> !p.getUniqueId().equals(player.getUniqueId())).toList();
            }
            return players.stream().map(Player::getName).collect(Collectors.toList());
        }));

        this.addRequirements(FactionRequirement.HAVE_FACTION);

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);
        Faction faction = user.getFaction();
        User invited = arguments.get("user");

        if(invited == null) {
            user.sendMessage(Messages.USER_NOT_FOUND_MESSAGE.translate());
            return;
        }

        if(faction.hasInvitation(invited)) {
            user.sendMessage(Messages.USER_ALREADY_INVITED_MESSAGE.translate(Formatter.user(invited)));
            return;
        }

        int usersInFaction = this.usersManager.getUsersInFaction(faction).size();
        int maxUsersInFaction = Config.getConfiguration(MainConfiguration.class).getMaxUsersPerFaction();
        if(usersInFaction >= maxUsersInFaction) {
            user.sendMessage(Messages.FACTION_FULL_MESSAGE.translate());
            return;
        }

        faction.inviteUser(invited);
        user.sendMessage(Messages.INVITATION_SENT_MESSAGE.translate(Formatter.user(invited)));
        invited.sendMessage(Messages.INVITATION_RECEIVED_MESSAGE.translate(Formatter.faction(faction)));
    }
}
