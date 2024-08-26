package fr.traqueur.modernfactions.api.messages;

public enum Messages implements Message {

    NO_PERMISSION_MESSAGE,
    ONLY_IN_GAME_MESSAGE,
    MISSING_ARGS_MESSAGE,
    ARG_NOT_RECOGNIZED_MESSAGE,
    NO_REQUIREMENT_MESSAGE,
    LEADER_MESSAGE,
    CREATE_FACTION_MESSAGE,
    BROADCAST_CREATION_MESSAGE,
    FACTION_EXISTS_MESSAGE,
    DISBAND_FACTION_MESSAGE,
    BROADCAST_DISBAND_MESSAGE,
    WILDERNESS_DESCRIPTION,
    SAFEZONE_DESCRIPTION,
    WARZONE_DESCRIPTION,
    NOT_IN_FACTION_MESSAGE,
    RELATION_ALL_COMMAND_MESSAGE,
    RELATION_COMMAND_MESSAGE,
    RECEIVER_RELATION_COMMAND_MESSAGE,
    RECEIVER_WISH_RELATION_COMMAND_MESSAGE,
    WISH_RELATION_COMMAND_MESSAGE,
    ALREADY_RELATION_MESSAGE,
    SAME_FACTION_MESSAGE,
    ENEMY_RELATION_NAME,
    TRUCE_RELATION_NAME,
    ALLY_RELATION_NAME,
    NEUTRAL_RELATION_NAME,
    MUST_NOT_BE_IN_FACTION_MESSAGE, USER_NOT_FOUND_MESSAGE, USER_ALREADY_INVITED_MESSAGE, INVITATION_SENT_MESSAGE, INVITATION_RECEIVED_MESSAGE, FACTION_NOT_FOUND_MESSAGE, USER_NOT_INVITED_MESSAGE, FACTION_JOINED_MESSAGE, USER_JOIN_MESSAGE, KICK_OR_DISBAND_MESSAGE, FACTION_FULL_MESSAGE, NO_LEADER_MESSAGE, LEAVE_FACTION_MESSAGE, USER_LEAVE_MESSAGE, POWER_USER_MESSAGE, SET_POWER_COMMAND_MESSAGE;


    @Override
    public String getKey() {
        return this.name().toLowerCase();
    }
}
