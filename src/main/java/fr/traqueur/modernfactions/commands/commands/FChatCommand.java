package fr.traqueur.modernfactions.commands.commands;

import fr.traqueur.commands.api.Arguments;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.chatmode.ChatMode;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.command.CommandSender;

public class FChatCommand extends FCommand {

    public FChatCommand(FactionsPlugin plugin) {
        super(plugin, "chat");

        this.aliases("c");
        this.setUsage("/f chat <mode>");
        this.addArgs("chatmode:chatmode");

        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender commandSender, Arguments arguments) {
        User user = this.getUser(commandSender);
        ChatMode chatMode = arguments.get("chatmode");
        this.usersManager.setChatMode(user, chatMode);
        user.sendMessage(Messages.CHAT_MODE_SET_MESSAGE.translate(Formatter.chatMode(chatMode)));
    }
}
