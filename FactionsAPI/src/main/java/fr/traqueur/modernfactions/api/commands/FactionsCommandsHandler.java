package fr.traqueur.modernfactions.api.commands;

import fr.traqueur.commands.api.messages.MessageHandler;
import fr.traqueur.modernfactions.api.messages.Messages;

public class FactionsCommandsHandler implements MessageHandler {
    @Override
    public String getNoPermissionMessage() {
        return Messages.NO_PERMISSION_MESSAGE.translate(true);
    }

    @Override
    public String getOnlyInGameMessage() {
        return Messages.ONLY_IN_GAME_MESSAGE.translate(true);
    }

    @Override
    public String getMissingArgsMessage() {
        return Messages.MISSING_ARGS_MESSAGE.translate(true);
    }

    @Override
    public String getArgNotRecognized() {
        return Messages.ARG_NOT_RECOGNIZED_MESSAGE.translate(true);
    }

    @Override
    public String getRequirementMessage() {
        return Messages.NO_REQUIREMENT_MESSAGE.translate(true);
    }
}
