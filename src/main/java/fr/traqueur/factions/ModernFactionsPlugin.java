package fr.traqueur.factions;

import fr.traqueur.factions.api.FactionsPlugin;

public class ModernFactionsPlugin extends FactionsPlugin {

    @Override
    public void onEnable() {
        System.out.println("ModernFactionsPlugin enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("ModernFactionsPlugin disabled");
    }
}
