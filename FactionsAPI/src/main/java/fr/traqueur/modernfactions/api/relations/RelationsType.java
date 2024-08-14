package fr.traqueur.modernfactions.api.relations;

import fr.traqueur.modernfactions.api.messages.Message;
import fr.traqueur.modernfactions.api.messages.Messages;

public enum RelationsType {

    OWN("<green>", () -> ""),
    ALLY("<dark_purple>", Messages.ALLY_RELATION_NAME),
    TRUCE("<light_purple>", Messages.TRUCE_RELATION_NAME),
    NEUTRAL("<gray>", Messages.NEUTRAL_RELATION_NAME),
    ENEMY("<red>", Messages.ENEMY_RELATION_NAME),
    ;

    private final String color;
    private final Message mesage;

    RelationsType(String color, Message message) {
        this.color = color;
        this.mesage = message;
    }

    public String message() {
        return this.mesage.translate();
    }

    public String getColor() {
        return color;
    }

    public String changeColorMessage(String message) {
        return message.replace("<relation_color>", this.getColor());
    }
}
