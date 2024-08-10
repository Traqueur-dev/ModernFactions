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

    String getKey();

    static String translate(Message message) {
        return JavaPlugin.getPlugin(FactionsPlugin.class).getMessageUtils().convertToLegacyFormat(message.translate());
    }

    static String translate(String messageStr) {
        Message message = MESSAGES.stream().filter(messageInner -> messageInner.getKey().equalsIgnoreCase(messageStr)).findFirst().orElseThrow(() -> new IllegalStateException("Message not found"));
        return translate(message);
    }

    default String translate() {
        return Config.getConfiguration(LangConfiguration.class).translate(this);
    }

}
