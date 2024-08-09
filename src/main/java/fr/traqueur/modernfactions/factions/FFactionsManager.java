package fr.traqueur.modernfactions.factions;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.dto.FactionDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.factions.exceptions.FactionAlreadyExistsException;
import fr.traqueur.modernfactions.api.storage.service.Service;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import fr.traqueur.modernfactions.commands.FCreateCommand;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FFactionsManager implements FactionsManager {

    private final FactionsPlugin plugin;
    private final Service<Faction, FactionDTO> service;

    public FFactionsManager(FactionsPlugin plugin) {
       this.plugin = plugin;
       this.service = new FactionService(plugin, FactionsManager.TABLE_NAME);
    }

    @Override
    public void loadFactions() {
        if(this.service.values().isEmpty()) {
            this.service.save(new FFaction(plugin, FactionsManager.WILDERNESS_NAME, "The wilderness is a place where you can build your base.", FactionsPlugin.CONSOLE_UUID));
            this.service.save(new FFaction(plugin, FactionsManager.SAFEZONE_NAME, "The safezone is a place where you can't take damage.", FactionsPlugin.CONSOLE_UUID));
            this.service.save(new FFaction(plugin, FactionsManager.WARZONE_NAME, "The warzone is a place where you can take damage.", FactionsPlugin.CONSOLE_UUID));
        }
    }

    @Override
    public Optional<Faction> getFaction(String name) {
        List<Faction> factions = this.service.where("name", name);
        return factions.isEmpty() ? Optional.empty() : Optional.of(factions.getFirst());
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
            return this.getFactionById(user.get().getFaction().getId());
        } else {
            throw new IllegalStateException("User not found.");
        }
    }

    @Override
    public Faction createFaction(String faction, UUID leader) throws FactionAlreadyExistsException {
        if(this.getFaction(faction).isPresent()) {
            throw new FactionAlreadyExistsException();
        }
        Faction created = new FFaction(plugin, faction, "The faction " + faction + " is a new faction.", leader);
        this.service.save(created);
        return created;
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
