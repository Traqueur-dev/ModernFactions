package fr.traqueur.modernfactions.api.commands;

public enum Permissions {

    SET_POWER(true),

    ;

    private final boolean admin;

    Permissions() {
        this(false);
    }

    Permissions(boolean admin) {
        this.admin = admin;
    }

    public String getPermission() {
        return ("modernfaction." + (admin ? "admin." : "") + this.name().toLowerCase()).replace("_", ".");
    }

}
