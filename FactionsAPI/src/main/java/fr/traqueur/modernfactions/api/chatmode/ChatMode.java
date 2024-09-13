package fr.traqueur.modernfactions.api.chatmode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ChatMode {

    PUBLIC("<yellow>","p", "pub"),
    FACTION("<green>","f", "fac"),
    ALLY("<dark_purple>","a"),
    TRUCE("<light_purple>","t"),;

    private final String color;
    private final String[] aliases;

    ChatMode(String color, String... aliases) {
        this.color = color;
        this.aliases = aliases;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getColor() {
        return color;
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
