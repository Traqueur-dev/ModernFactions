package fr.traqueur.modernfactions.api.factions;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class FactionPersistentDataType implements PersistentDataType<String, Faction> {

    public static final FactionPersistentDataType INSTANCE = new FactionPersistentDataType();
    private static final FactionsManager FACTIONS_MANAGER = JavaPlugin.getPlugin(FactionsPlugin.class).getManager(FactionsManager.class);


    @NotNull
    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @NotNull
    @Override
    public Class<Faction> getComplexType() {
        return Faction.class;
    }

    @NotNull
    @Override
    public String toPrimitive(@NotNull Faction faction, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return faction.getId().toString();
    }

    @NotNull
    @Override
    public Faction fromPrimitive(@NotNull String uuid, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return FACTIONS_MANAGER.getFactionById(UUID.fromString(uuid)).orElseThrow(() -> new IllegalArgumentException("Faction with id " + uuid + " not found"));
    }
}
