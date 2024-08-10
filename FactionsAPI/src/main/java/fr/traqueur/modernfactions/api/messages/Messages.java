package fr.traqueur.modernfactions.api.messages;

public enum Messages implements Message {

    NO_PERMISSION_MESSAGE,
    ONLY_IN_GAME_MESSAGE,
    MISSING_ARGS_MESSAGE,
    ARG_NOT_RECOGNIZED_MESSAGE,
    NO_REQUIREMENT_MESSAGE,
    NO_LEADER_MESSAGE,
    CREATE_FACTION_MESSAGE,
    BROADCAST_CREATION_MESSAGE,
    FACTION_EXISTS_MESSAGE,
    DISBAND_FACTION_MESSAGE,
    BROADCAST_DISBAND_MESSAGE,
    WILDERNESS_DESCRIPTION,
    SAFEZONE_DESCRIPTION,
    WARZONE_DESCRIPTION,
    ;

    @Override
    public String getKey() {
        return this.name().toLowerCase();
    }
}
