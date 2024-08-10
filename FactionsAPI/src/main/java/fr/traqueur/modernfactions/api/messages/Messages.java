package fr.traqueur.modernfactions.api.messages;

public enum Messages implements Message {

    NO_PERMISSION_MESSAGE,
    ONLY_IN_GAME_MESSAGE,
    MISSING_ARGS_MESSAGE,
    ARG_NOT_RECOGNIZED_MESSAGE,
    NO_REQUIREMENT_MESSAGE,
    ;

    @Override
    public String getKey() {
        return this.name().toLowerCase();
    }
}
