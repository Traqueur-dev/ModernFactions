package fr.traqueur.modernfactions.api.users;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.chatmode.ChatMode;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.managers.Manager;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersManager extends Manager {

    NamespacedKey FACTION_KEY = new NamespacedKey(JavaPlugin.getProvidingPlugin(FactionsPlugin.class), "faction");

    String TABLE_NAME = "users";

    String durationWords(int seconds);

    void startTeleportation(User user, int seconds);

    boolean cancelTeleportation(User user);

    User loadOrCreateUser(Player player);

    Optional<User> getUser(Player player);

    Optional<User> getUserById(UUID uuid);

    List<User> getUsersInFaction(Faction faction);

    Optional<User> getUserByName(String s);

    List<User> getUsers();

    void setChatMode(User user, ChatMode chatMode);

    String getFormat(ChatMode chatMode);
}
