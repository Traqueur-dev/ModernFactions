package fr.traqueur.modernfactions.api.messages;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Message {

    List<Message> MESSAGES = new ArrayList<>(Arrays.asList(Messages.values()));

    static void registerMessage(Message message) {
        MESSAGES.add(message);
    }

    static void registerMessage(String s) {
        registerMessage(() -> s);
    }

    static String translate(Message message, Formatter... formatters) {
        return translate(false, message, formatters);
    }

    static String translate(String messageStr, Formatter... formatters) {
        Message message = MESSAGES.stream().filter(messageInner -> messageInner.getKey().equalsIgnoreCase(messageStr)).findFirst().orElseThrow(() -> new IllegalStateException("Message not found"));
        return translate(false, message, formatters);
    }

    static String translate(boolean legacy, Message message, Formatter... formatters) {
        return message.translate(legacy, formatters);
    }

    static String translate(boolean legacy, String messageStr, Formatter... formatters) {
        Message message = MESSAGES.stream().filter(messageInner -> messageInner.getKey().equalsIgnoreCase(messageStr)).findFirst().orElseThrow(() -> new IllegalStateException("Message not found"));
        return translate(legacy, message, formatters);
    }

    String getKey();

    default String translate(Formatter... formatters) {
        return translate(false, formatters);
    }

    default String translate(boolean legacy, Formatter... formatters) {
        String message = Config.getConfiguration(LangConfiguration.class).translate(this);
        for (Formatter formatter : formatters) {
            message = formatter.handle(JavaPlugin.getPlugin(FactionsPlugin.class), message);
        }
        if(!legacy) return message;
        return JavaPlugin.getPlugin(FactionsPlugin.class).getMessageUtils().convertToLegacyFormat(message);
    }

}
