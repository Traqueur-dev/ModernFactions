package fr.traqueur.modernfactions.users;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.dto.UserDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.factions.HomeRunnable;
import fr.traqueur.modernfactions.api.factions.roles.Role;
import fr.traqueur.modernfactions.api.storage.service.Service;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import fr.traqueur.modernfactions.configurations.MainConfiguration;
import fr.traqueur.modernfactions.configurations.RolesConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class FUsersManager implements UsersManager {

    private final FactionsPlugin plugin;
    private final Service<User, UserDTO> service;
    private final Map<UUID, WrappedTask> homeRunnables;

    public FUsersManager(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.service = new UserService(plugin, TABLE_NAME);
        this.homeRunnables = new HashMap<>();
    }

    @Override
    public String durationWords(int seconds) {
        long minutes = seconds / 60;
        long secondes = seconds % 60;

        StringBuilder resultat = new StringBuilder();

        if (minutes > 0) {
            resultat.append(minutes).append("m");
        }
        if (secondes > 0) {
            resultat.append(secondes);
        }
        resultat.append("s");

        return resultat.toString();
    }

    @Override
    public void startTeleportation(User user, int seconds) {
        HomeRunnable runnable = new HomeRunnable(this, user, user.getFaction().getHome().get(), seconds);
        WrappedTask task = this.plugin.getScheduler().runTimer(runnable, 1, 1, TimeUnit.SECONDS);
        this.homeRunnables.put(user.getId(), task);
    }

    @Override
    public boolean cancelTeleportation(User user) {
        WrappedTask runnable = this.homeRunnables.remove(user.getId());
        if(runnable != null) {
            this.plugin.getScheduler().cancelTask(runnable);
            return true;
        }
        return false;
    }

    @Override
    public User loadOrCreateUser(Player player) {
        FactionsManager factionsManager = this.plugin.getManager(FactionsManager.class);
        Optional<User> optional = this.service.get(player.getUniqueId());
        if(optional.isPresent()) {
            return optional.get();
        }
        Role defaultRole = Config.getConfiguration(RolesConfiguration.class).getDefaultRole();
        int defaultPower = Config.getConfiguration(MainConfiguration.class).getDefaultUserPower();
        User user = new FUser(plugin, player.getUniqueId(),player.getName(), factionsManager.getWilderness().getId(), defaultRole.name(), defaultPower);
        this.service.add(user);
        return user;
    }

    @Override
    public Optional<User> getUser(Player player) {
        return this.service.get(player.getUniqueId());
    }

    @Override
    public Optional<User> getUserById(UUID uuid) {
        return this.service.get(uuid);
    }

    @Override
    public List<User> getUsersInFaction(Faction faction) {
        return this.service.where("faction", faction.getId().toString());
    }

    @Override
    public Optional<User> getUserByName(String s) {
        return this.service.where("name", s).stream().findFirst();
    }

    @Override
    public List<User> getUsers() {
        return this.service.values();
    }

    @Override
    public FactionsPlugin getPlugin() {
        return this.plugin;
    }
}
