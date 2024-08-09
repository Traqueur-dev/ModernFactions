package fr.traqueur.modernfactions.users;

import fr.maxlego08.sarah.Column;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.storage.NotLoadable;
import fr.traqueur.modernfactions.api.users.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class FUser implements User {

    @NotLoadable
    private final FactionsPlugin plugin;
    @Column(value = "unique_id")
    private final UUID uuid;
    @Column(value = "faction")
    private UUID factionId;

    public FUser(FactionsPlugin plugin, UUID uuid, UUID faction) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.factionId = faction;
    }

    public FUser(FactionsPlugin plugin, Map<String, Object> objectMap) {
        this(plugin, UUID.fromString((String) objectMap.get("unique_id")), UUID.fromString((String) objectMap.get("faction")));
    }

    @Override
    public UUID getId() {
        return this.uuid;
    }

    @Override
    public void setFaction(UUID uuid) {
        this.factionId = uuid;
    }

    @Override
    public UUID getFaction() {
        return this.factionId;
    }

    @Override
    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(this.uuid);
        plugin.getMessageUtils().sendMessage(player, message);
    }
}
