package fr.traqueur.modernfactions.api.messages;

import org.bukkit.entity.Player;

public interface MessageUtils {

    void sendMessage(Player player, String message);

    String convertToLegacyFormat(String message);

}
