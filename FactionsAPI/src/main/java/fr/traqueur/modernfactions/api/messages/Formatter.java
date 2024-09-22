package fr.traqueur.modernfactions.api.messages;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.chatmode.ChatMode;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.relations.RelationsType;
import fr.traqueur.modernfactions.api.users.User;

import java.util.List;
import java.util.function.Function;

public class Formatter {

    private final String pattern;
    private final Function<FactionsPlugin, String> supplier;

    private Formatter(String pattern, Object supplier) {
        this.pattern = pattern;
        this.supplier = (api) -> supplier.toString();
    }

    private Formatter(String pattern, Function<FactionsPlugin, String> supplier) {
        this.pattern = pattern;
        this.supplier = supplier;
    }

    public static Formatter format(String pattern, Object supplier) {
        return new Formatter(pattern, supplier);
    }

    public static Formatter format(String pattern, Function<FactionsPlugin, String> supplier) {
        return new Formatter(pattern, supplier);
    }

    public static Formatter faction(Faction faction) {
        return format("%faction%", ressourcefulBeesLikeAPI -> faction.getName());
    }

    public static Formatter relation(RelationsType relation) {
        //TODO add translation for relations
        return format("%relation%", ressourcefulBeesLikeAPI -> relation.message());
    }

    public static Formatter user(User user) {
        return format("%user%", ressourcefulBeesLikeAPI -> user.getName());
    }

    public static String factionDescription(String message) {
        for (Formatter formatter : List.of(wildernessDescription(), safezoneDescription(), warzoneDescription())) {
            message = formatter.handle(null, message);
        }
        return message;
    }

    public static Formatter wildernessDescription() {
        return format("%wilderness_description%", ressourcefulBeesLikeAPI -> Messages.WILDERNESS_DESCRIPTION.translate());
    }

    public static Formatter safezoneDescription() {
        return format("%safezone_description%", ressourcefulBeesLikeAPI -> Messages.SAFEZONE_DESCRIPTION.translate());
    }

    public static Formatter warzoneDescription() {
        return format("%warzone_description%", ressourcefulBeesLikeAPI -> Messages.WARZONE_DESCRIPTION.translate());
    }

    public static Formatter chatMode(ChatMode chatMode) {
        return format("%chatmode%", ressourcefulBeesLikeAPI ->
                chatMode.getType().getFirst().getColor() + chatMode.name());
    }

    public String handle(FactionsPlugin api, String message) {
        return message.replaceAll(this.pattern, String.valueOf(this.supplier.apply(api)));
    }
}