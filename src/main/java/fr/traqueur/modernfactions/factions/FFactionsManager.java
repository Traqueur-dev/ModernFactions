package fr.traqueur.modernfactions.factions;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.factions.exceptions.FactionAlreadyExistsException;
import fr.traqueur.modernfactions.api.storage.service.Service;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class FFactionsManager implements FactionsManager {

    private final FactionsPlugin plugin;
    private final Service<Faction> service;

    public FFactionsManager(FactionsPlugin plugin) {
       this.plugin = plugin;
       this.service = new FactionService(plugin, FactionsManager.TABLE_NAME);
    }

    @Override
    public void loadFactions() {
        if(this.service.values().isEmpty()) {
            this.service.save(new FFaction(FactionsManager.WILDERNESS_NAME));
            this.service.save(new FFaction(FactionsManager.SAFEZONE_NAME));
            this.service.save(new FFaction(FactionsManager.WARZONE_NAME));
        }
    }

    @Override
    public Optional<Faction> getFaction(String name) {
        Optional<Faction> opt = this.service.getCache().values().stream().filter(faction -> faction.getName().equalsIgnoreCase(name)).findFirst();
        if(opt.isPresent()) {
            return opt;
        }
        return this.service.values().stream().filter(faction -> faction.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public Optional<Faction> getFactionById(UUID id) {
        return this.service.get(id);
    }

    @Override
    public Optional<Faction> getFactionByPlayer(Player player) {
        UsersManager usersManager = this.plugin.getManager(UsersManager.class);
        Optional<User> user = usersManager.getUser(player);
        if(user.isPresent()) {
            return this.getFactionById(user.get().getFaction());
        } else {
            throw new IllegalStateException("User not found.");
        }
    }

    @Override
    public void createFaction(String faction) throws FactionAlreadyExistsException {
        if(this.getFaction(faction).isPresent()) {
            throw new FactionAlreadyExistsException();
        }
        this.service.save(new FFaction(faction));
    }

    @Override
    public void removeFaction(Faction faction) {
        this.service.delete(faction);
    }

    @Override
    public Faction getWilderness() {
        return this.getFaction(FactionsManager.WILDERNESS_NAME).orElseThrow(() -> new IllegalStateException("Wilderness not found."));
    }

    @Override
    public Faction getSafeZone() {
        return this.getFaction(FactionsManager.SAFEZONE_NAME).orElseThrow(() -> new IllegalStateException("Wilderness not found."));
    }

    @Override
    public Faction getWarZone() {
        return this.getFaction(FactionsManager.WARZONE_NAME).orElseThrow(() -> new IllegalStateException("Wilderness not found."));
    }

    @Override
    public FactionsPlugin getPlugin() {
        return this.plugin;
    }
}
