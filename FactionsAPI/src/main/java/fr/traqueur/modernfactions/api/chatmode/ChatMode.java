package fr.traqueur.modernfactions.api.chatmode;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.relations.RelationsManager;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;

import java.util.List;

public enum ChatMode {

    PUBLIC(List.of(RelationsType.NEUTRAL),"p", "pub"),
    FACTION(List.of(RelationsType.OWN),"f", "fac") ,
    ALLY(List.of(RelationsType.ALLY),"a"),
    TRUCE(List.of(RelationsType.TRUCE, RelationsType.ALLY),"t"),;

    private final List<RelationsType> type;
    private final String[] aliases;

    ChatMode(List<RelationsType> type, String... aliases) {
        this.aliases = aliases;
        this.type = type;
    }

    public String[] getAliases() {
        return aliases;
    }

    public List<RelationsType> getType() {
        return type;
    }

    public String format(FactionsPlugin plugin, User source, User target, String message) throws UserScopeMessageException {
        Faction faction = source.getFaction();
        RelationsManager relationsManager = plugin.getManager(RelationsManager.class);
        String prefix = source.getRole().prefix();
        var type = relationsManager.getRelationBetween(faction, target.getFaction());
        if (this != ChatMode.PUBLIC && !this.getType().contains(type) && type != RelationsType.OWN) {
            throw new UserScopeMessageException();
        }
        return plugin.getManager(UsersManager.class).getFormat(this)
                .replace("%player%", source.getPlayer().getDisplayName())
                .replace("%relation_color%", type.getColor())
                .replace("%role%", prefix)
                .replace("%faction%", faction.getName())
                .replace("%color_format%", this.getType().getFirst().getColor())
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
