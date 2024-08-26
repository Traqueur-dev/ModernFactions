package fr.traqueur.modernfactions.api.factions;

import fr.traqueur.modernfactions.api.messages.Formatter;
import fr.traqueur.modernfactions.api.messages.Messages;
import fr.traqueur.modernfactions.api.users.User;
import fr.traqueur.modernfactions.api.users.UsersManager;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class HomeRunnable extends BukkitRunnable {

    private final UsersManager usersManager;
    private final User user;
    private final Location location;
    private int seconds;

    public HomeRunnable(UsersManager usersManager, User user, Location location, int seconds) {
        this.usersManager = usersManager;
        this.user = user;
        this.location = location;
        this.seconds = seconds;
    }

    @Override
    public void run() {
        this.seconds--;

        if (this.seconds % 10 == 0 || this.seconds <= 5 && this.seconds > 0) {
            this.user.sendMessage(Messages.HOME_COOLDOWN_MESSAGE.translate(Formatter.format("%time%", this.usersManager.durationWords(this.seconds))));
        }

        if(this.user.getPlayer() != null && this.seconds <= 0) {
            this.usersManager.getPlugin().getScheduler().teleportAsync(this.user.getPlayer(), this.location);
            this.user.sendMessage(Messages.HOME_TELEPORT_MESSAGE.translate());
            this.usersManager.cancelTeleportation(this.user);
        }
    }

}
