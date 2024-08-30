package fr.traqueur.modernfactions.configurations;

import dev.dejvokep.boostedyaml.YamlDocument;
import fr.traqueur.modernfactions.api.FactionsLogger;
import fr.traqueur.modernfactions.api.FactionsPlugin;
import fr.traqueur.modernfactions.api.configurations.Config;
import fr.traqueur.modernfactions.api.factions.roles.Role;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RolesConfiguration implements Config {

    private final FactionsPlugin plugin;
    private final List<Role> roles;
    private Role defaultRole;

    public RolesConfiguration(FactionsPlugin plugin) {
        this.plugin = plugin;
        this.roles = new ArrayList<>();

    }

    @Override
    public String getFile() {
        return "roles.yml";
    }

    @Override
    public void loadConfig() {
        YamlDocument config = this.getConfig(this.plugin);

        config.getList("roles").stream().map(roleObj -> {
            Map<String, Object> role = (Map<String, Object>) roleObj;
            String name = (String) role.get("name");
            String prefix = (String) role.get("prefix");
            int priority = (int) role.get("priority");
            return new Role(name, priority, prefix);
        }).forEach(this.roles::add);

        this.defaultRole = this.getRoleByName(config.getString("default-role"));
        FactionsLogger.info("&eLoaded " + this.roles.size() + " faction role.");
    }

    public List<Role> getRoles() {
        return roles;
    }

    public Role getDefaultRole() {
        return defaultRole;
    }

    public Role getRoleByName(String name) {
        return this.roles.stream().filter(role -> role.name().equalsIgnoreCase(name)).findFirst().orElseThrow(() -> new IllegalStateException("Role not found"));
    }

    public Role getMaxPriorityRole() {
        return this.roles.stream().max(Comparator.comparingInt(Role::power)).orElseThrow(() -> new IllegalStateException("Role not found"));
    }

    public Role getMinProrityRole() {
        return this.roles.stream().min(Comparator.comparingInt(Role::power)).orElseThrow(() -> new IllegalStateException("Role not found"));
    }

    public Role getNextPriorityRole(Role role) {
        return this.roles.stream().filter(r -> r.power() > role.power()).min(Comparator.comparingInt(Role::power)).orElse(this.getMaxPriorityRole());
    }

    public Role getPreviousPriorityRole(Role role) {
        return this.roles.stream().filter(r -> r.power() < role.power()).max(Comparator.comparingInt(Role::power)).orElse(this.getMinProrityRole());
    }
}
