package fr.traqueur.modernfactions.api.factions;

import fr.traqueur.modernfactions.api.factions.exceptions.FactionAlreadyExistsException;
import fr.traqueur.modernfactions.api.managers.Manager;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public interface FactionsManager extends Manager {

        String WILDERNESS_NAME = "Wilderness";
        String SAFEZONE_NAME = "SafeZone";
        String WARZONE_NAME = "WarZone";
        String TABLE_NAME = "factions";

        void loadFactions();

        Optional<Faction> getFaction(String name);

        Optional<Faction> getFactionById(UUID id);

        Optional<Faction> getFactionByPlayer(Player player);

        void createFaction(String faction) throws FactionAlreadyExistsException;

        void removeFaction(Faction faction);

        Faction getWilderness();

        Faction getSafeZone();

        Faction getWarZone();
}
