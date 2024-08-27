package fr.traqueur.modernfactions.api.commands;

import fr.traqueur.commands.api.logging.Logger;
import fr.traqueur.modernfactions.api.FactionsLogger;

public class CommandLogger implements Logger {
    @Override
    public void error(String s) {
        FactionsLogger.severe(s);
    }

    @Override
    public void info(String s) {
        FactionsLogger.info(s);
    }
}
