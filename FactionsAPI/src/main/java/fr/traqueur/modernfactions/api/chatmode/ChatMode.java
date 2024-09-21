package fr.traqueur.modernfactions.api.chatmode;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.relations.RelationsManager;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum ChatMode {

    PUBLIC(RelationsType.NEUTRAL,"p", "pub"),
    FACTION(RelationsType.OWN,"f", "fac") ,
    ALLY(RelationsType.ALLY,"a"),
    TRUCE(RelationsType.TRUCE,"t"),;

    private RelationsType type;
    private final String[] aliases;

    ChatMode(RelationsType type, String... aliases) {
        this.aliases = aliases;
        this.type = type;
    }

    public String[] getAliases() {
        return aliases;
    }

    public RelationsType getType() {
        return type;
    }

    public String format(FactionsPlugin plugin, User source, User target, String message) throws UserScopeMessageException {
        Faction faction = source.getFaction();
        RelationsManager relationsManager = plugin.getManager(RelationsManager.class);
        String prefix = source.getRole().prefix();
        var type = relationsManager.getRelationBetween(faction, target.getFaction());
        if (this != ChatMode.PUBLIC && type != this.getType() && type != RelationsType.OWN) {
            throw new UserScopeMessageException();
        }
        return plugin.getManager(UsersManager.class).getFormat(this)
                .replace("%player%", source.getPlayer().getDisplayName())
                .replace("%role%", type.getColor() + prefix)
                .replace("%faction%", faction.getName())
                .replace("%color%", type.getColor())
                .replace("%message%", message);
    }

    public static ChatMode fromString(String upperCase) {
        try {
            return ChatMode.valueOf(upperCase);
        } catch (Exception e) {
            for (ChatMode mode : ChatMode.values()) {
                for (String alias : mode.aliases) {
                    if (alias.equalsIgnoreCase(upperCase)) {
                        return mode;
                    }
                }
            }
            return PUBLIC;
        }
    }
}
