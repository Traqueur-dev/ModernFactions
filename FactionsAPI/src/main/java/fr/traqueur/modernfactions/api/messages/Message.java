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

    static String translate(Message message) {
        return message.translate();
    }

    static String translate(String messageStr) {
        Message message = MESSAGES.stream().filter(messageInner -> messageInner.getKey().equalsIgnoreCase(messageStr)).findFirst().orElseThrow(() -> new IllegalStateException("Message not found"));
        return translate(message);
    }

    String getKey();

    default String translate() {
        return JavaPlugin.getPlugin(FactionsPlugin.class).getMessageUtils().convertToLegacyFormat(Config.getConfiguration(LangConfiguration.class).translate(this));
    }

}
