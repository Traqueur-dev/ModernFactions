package fr.traqueur.modernfactions.api.factions;

import fr.traqueur.modernfactions.api.dto.FactionDTO;
import fr.traqueur.modernfactions.api.factions.exceptions.FactionAlreadyExistsException;
import fr.traqueur.modernfactions.api.managers.Manager;
import fr.traqueur.modernfactions.api.storage.service.Service;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface FactionsManager extends Manager {

        String WILDERNESS_NAME = "Wilderness";
        String SAFEZONE_NAME = "SafeZone";
        String WARZONE_NAME = "WarZone";
        String TABLE_NAME = "factions";

        String serializeLocation(Location location);

        Location deserializeLocation(String location);

        void loadFactions();

        Optional<Faction> getFaction(String name);

        Optional<Faction> getFactionById(UUID id);

        Optional<Faction> getFactionByPlayer(Player player);

        Faction createFaction(String faction, UUID leader) throws FactionAlreadyExistsException;

        void removeFaction(Faction faction);

        Set<Faction> getFactions();

        Faction getWilderness();

        Faction getSafeZone();

        Faction getWarZone();

        void joinFaction(User user, Faction faction);
}
