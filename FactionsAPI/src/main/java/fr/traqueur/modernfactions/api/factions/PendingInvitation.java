package fr.traqueur.modernfactions.api.factions;

import fr.traqueur.modernfactions.api.users.User;

public record PendingInvitation(User invited, long time) {

    public boolean isExpired() {
        return System.currentTimeMillis() - this.time >= 60000L;
    }

}

