package fr.traqueur.modernfactions.commands.arguments;

import fr.traqueur.commands.api.arguments.ArgumentConverter;
import fr.traqueur.commands.api.arguments.TabConverter;
import fr.traqueur.modernfactions.api.chatmode.ChatMode;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChatModeArgument implements ArgumentConverter<ChatMode>, TabConverter {
    @Override
    public ChatMode apply(String s) {
        return ChatMode.fromString(s.toUpperCase());
    }

    @Override
    public List<String> onCompletion(CommandSender commandSender) {
        List<String> names = Arrays.stream(ChatMode.values()).map(Enum::name).collect(Collectors.toList());
        for (ChatMode mode : ChatMode.values()) {
            names.addAll(Arrays.asList(mode.getAliases()));
        }
        return names;
    }
}
