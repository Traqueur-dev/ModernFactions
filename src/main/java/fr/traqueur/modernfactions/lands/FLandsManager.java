package fr.traqueur.modernfactions.lands;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.lands.LandsManager;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.configurations.LandsConfiguration;
import fr.traqueur.modernfactions.relations.Relations;

public class FLandsManager implements LandsManager {

    private final FactionsPlugin plugin;
    private final LandsConfiguration landsConfiguration;

    public FLandsManager(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.landsConfiguration = Config.registerConfiguration(LandsConfiguration.class, new LandsConfiguration(plugin));
    }

    @Override
    public void sendNotification(User user, Faction faction) {
        String description = Formatter.factionDescription(faction.getDescription());
        String factionName = faction.getName();

        String message = this.landsConfiguration.getNotificationMessage()
                .replace("%faction%", factionName)
                .replace("%faction_description%", description);

        //TODO: get relation between user and faction and replace optional <relation_color> in message
        message = Relations.ALLY.changeColorMessage(message);

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
                //TODO: get relation between user and faction and replace optional <relation_color> in message
                user.sendTitle(message, subtitle, this.landsConfiguration.getFadeIn(), this.landsConfiguration.getStay(), this.landsConfiguration.getFadeOut());
                break;
        }
    }

    @Override
    public FactionsPlugin getPlugin() {
        return this.plugin;
    }
}
