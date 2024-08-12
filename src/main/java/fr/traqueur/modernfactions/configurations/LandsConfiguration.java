package fr.traqueur.modernfactions.configurations;

import dev.dejvokep.boostedyaml.YamlDocument;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.lands.NotificationType;

public class LandsConfiguration implements Config {

    private final FactionsPlugin plugin;
    private NotificationType notificationType;
    private String notificationMessage;
    private String subtitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public LandsConfiguration(FactionsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getFile() {
        return "lands.yml";
    }

    @Override
    public void loadConfig() {
        YamlDocument config = this.getConfig(this.plugin);
        this.notificationType = NotificationType.valueOf(config.getString("enter-chunk-notification.type"));
        this.notificationMessage = config.getString("enter-chunk-notification.message");
        this.subtitle = config.getString("enter-chunk-notification.subtitle");
        this.fadeIn = config.getInt("enter-chunk-notification.fadeIn");
        this.stay = config.getInt("enter-chunk-notification.stay");
        this.fadeOut = config.getInt("enter-chunk-notification.fadeOut");
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }
}
