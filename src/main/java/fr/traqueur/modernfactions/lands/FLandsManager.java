package fr.traqueur.modernfactions.lands;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.dto.LandDTO;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.factions.FactionPersistentDataType;
import fr.traqueur.modernfactions.api.factions.FactionsManager;
import fr.traqueur.modernfactions.api.lands.Land;
import fr.traqueur.modernfactions.api.lands.LandsManager;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.relations.RelationsManager;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import fr.traqueur.modernfactions.api.storage.service.Service;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.configurations.MainConfiguration;
import org.bukkit.Chunk;

public class FLandsManager implements LandsManager {

    private final FactionsPlugin plugin;
    private final Service<Land, LandDTO> service;
    private final MainConfiguration landsConfiguration;

    public FLandsManager(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.service = new LandService(plugin, LandsManager.TABLE_NAME);
        this.landsConfiguration = Config.getConfiguration(MainConfiguration.class);
    }

    @Override
    public boolean canClaimLand(Chunk chunk, Faction faction) {
        RelationsManager relationsManager = this.plugin.getManager(RelationsManager.class);
        Faction landOwner = this.getLandOwner(chunk);

        int powerLandOwner = landOwner.getPowers();
        int landsLandOwner = landOwner.getLands();

        int powerFaction = faction.getPowers();
        int landsFaction = faction.getLands();

        return (!landOwner.isSystem() || landOwner.isWilderness())
                && !faction.getId().equals(landOwner.getId())
                && ((landsLandOwner < powerLandOwner && !landOwner.isWilderness()) || landOwner.isWilderness())
                && landsFaction < powerFaction
                && ((relationsManager.getRelationBetween(faction, landOwner) == RelationsType.ENEMY  && !landOwner.isWilderness()) || landOwner.isWilderness());
    }


    @Override
    public Faction getLandOwner(Chunk chunk) {
        return this.getLand(chunk).getFaction();
    }

    @Override
    public Land getLand(Chunk chunk) {
        var list = service.where(new String[] {"x", "z"}, new String[] {String.valueOf(chunk.getX()), String.valueOf(chunk.getZ())});
        if (list.isEmpty()) {
            Land land = new FLand(chunk.getWorld(), chunk.getX(), chunk.getZ(), this.plugin.getManager(FactionsManager.class).getWilderness());
            service.add(land);
            return land;
        }
        return list.getFirst();
    }

    @Override
    public boolean isInsideFactionLand(User user) {
        return this.getLandOwner(user.getLocation().getChunk()).getId().equals(user.getFaction().getId());
    }

    @Override
    public void claimLand(Chunk chunk, Faction faction) {
        Land land = this.getLand(chunk);
        Faction landOwner = land.getFaction();
        if(!landOwner.isWilderness()) {
            landOwner.removeLand();
        }
        land.setFaction(faction);
        if(!faction.isWilderness()) {
            faction.addLand();
        }
    }

    @Override
    public void sendNotification(User user, Faction faction) {
        RelationsManager relationsManager = plugin.getManager(RelationsManager.class);
        String description = Formatter.factionDescription(faction.getDescription());
        String factionName = faction.getName();

        String message = this.landsConfiguration.getNotificationMessage()
                .replace("%faction%", factionName)
                .replace("%faction_description%", description);

        RelationsType relationsType = relationsManager.getRelationBetween(user.getFaction(), faction);
        message = relationsType.changeColorMessage(message);

        switch (this.landsConfiguration.getNotificationType()) {
            case ACTION_BAR:
                user.sendActionBar(message);
                break;
            case MESSAGE:
                user.sendMessage(message);
                break;
            case TITLE:
                String subtitle = this.landsConfiguration.getSubtitle()
                        .replace("%faction%", factionName)
                        .replace("%faction_description%", description);
                subtitle = relationsType.changeColorMessage(subtitle);
                user.sendTitle(message, subtitle, this.landsConfiguration.getFadeIn(), this.landsConfiguration.getStay(), this.landsConfiguration.getFadeOut());
                break;
        }
    }

    @Override
    public FactionsPlugin getPlugin() {
        return this.plugin;
    }
}
