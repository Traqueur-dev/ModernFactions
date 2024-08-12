package fr.traqueur.modernfactions.api.lands;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.managers.Manager;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public interface LandsManager extends Manager {

    NamespacedKey LAND_OWNER_KEY = new NamespacedKey(JavaPlugin.getProvidingPlugin(FactionsPlugin.class), "factions");

    void sendNotification(User user, Faction faction);

}
