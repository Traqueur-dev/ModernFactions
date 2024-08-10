package fr.traqueur.modernfactions.api.messages;

import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.factions.Faction;
import fr.traqueur.modernfactions.api.users.User;

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

    public static Formatter user(User user) {
        return format("%user%", ressourcefulBeesLikeAPI -> user.getName());
    }

    public String handle(FactionsPlugin api, String message) {
        return message.replaceAll(this.pattern, String.valueOf(this.supplier.apply(api)));
    }
}