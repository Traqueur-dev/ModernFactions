package fr.traqueur.modernfactions.commands;

import fr.traqueur.commands.api.CommandManager;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.commands.FCommand;
import fr.traqueur.modernfactions.api.modules.Module;
import fr.traqueur.modernfactions.commands.commands.*;
import fr.traqueur.modernfactions.commands.commands.admin.FSetPowerCommand;
import fr.traqueur.modernfactions.commands.commands.lands.FClaimCommand;
import fr.traqueur.modernfactions.commands.commands.lands.FUnclaimCommand;
import fr.traqueur.modernfactions.commands.commands.relations.*;
import fr.traqueur.modernfactions.commands.commands.roles.FDemoteCommand;
import fr.traqueur.modernfactions.commands.commands.roles.FLeaderCommand;
import fr.traqueur.modernfactions.commands.commands.roles.FPromoteCommand;
import fr.traqueur.modernfactions.commands.commands.roles.FRoleCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandsLoader {
    
    private final FactionsPlugin plugin;
    private final CommandManager commandManager;
    private final Map<Module, List<FCommand>> commands;


    public CommandsLoader(FactionsPlugin plugin, CommandManager commandManager) {
        this.plugin = plugin;
        this.commandManager = commandManager;
        this.commands = new HashMap<>();
        this.registerCommands();
    }

    private <T extends Module> void registerCommand(FCommand command, T module) {
        this.commands.computeIfAbsent(module, k -> new ArrayList<>()).add(command);
    }
    
    public void registerCommands() {
        this.registerCommand(new FCreateCommand(plugin), plugin);
        this.registerCommand(new FDisbandCommand(plugin), plugin);
        this.registerCommand(new FNeutralCommand(plugin), plugin);
        this.registerCommand(new FEnemyAllCommand(plugin), plugin);
        this.registerCommand(new FEnemyCommand(plugin), plugin);
        this.registerCommand(new FTruceCommand(plugin), plugin);
        this.registerCommand(new FAllyCommand(plugin), plugin);
        this.registerCommand(new FInviteCommand(plugin), plugin);
        this.registerCommand(new FJoinCommand(plugin), plugin);
        this.registerCommand(new FLeaveCommand(plugin), plugin);
        this.registerCommand(new FPowerCommand(plugin), plugin);
        this.registerCommand(new FClaimCommand(plugin), plugin);
        this.registerCommand(new FUnclaimCommand(plugin), plugin);
        this.registerCommand(new FPromoteCommand(plugin), plugin);
        this.registerCommand(new FDemoteCommand(plugin), plugin);
        this.registerCommand(new FLeaderCommand(plugin), plugin);
        this.registerCommand(new FRoleCommand(plugin), plugin);
        this.registerCommand(new FSetHomeCommand(plugin), plugin);
        this.registerCommand(new FHomeCommand(plugin), plugin);
        this.registerCommand(new FDescCommand(plugin), plugin);
        this.registerCommand(new FChatCommand(plugin), plugin);

        this.registerCommand(new FSetPowerCommand(plugin), plugin);
    }

    public void loadCommands() {
        this.commands.forEach((module, commands) -> {
            if(module.isEnable()) {
                commands.forEach(this.commandManager::registerCommand);
            }
        });
    }
}
