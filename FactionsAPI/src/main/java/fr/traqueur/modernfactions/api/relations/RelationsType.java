package fr.traqueur.modernfactions.api.relations;

public enum RelationsType {

    OWN("<green>"),
    ALLY("<dark_purple>"),
    TRUCE("<light_purple>"),
    NEUTRAL("<gray>"),
    ENEMY("<red>"),
    ;

    private final String color;

    RelationsType(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String changeColorMessage(String message) {
        return message.replace("<relation_color>", this.getColor());
    }
}
