package dk.tandhjulet.config.holder;

import java.util.HashSet;
import java.util.UUID;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import dk.tandhjulet.config.annotations.DeleteOnEmpty;
import dk.tandhjulet.enums.BandeRank;

@ConfigSerializable
public class BandePlayerHolder {
    private @MonotonicNonNull UUID base;

    public UUID base() {
        return base;
    }

    public void base(final UUID base) {
        this.base = base;
    }

    private @DeleteOnEmpty String bande;

    public String bande() {
        return bande;
    }

    public void bande(final String bande) {
        this.bande = bande;
    }

    private @DeleteOnEmpty BandeRank bandeRank;

    public BandeRank bandeRank() {
        return bandeRank;
    }

    public void bandeRank(final BandeRank bandeRank) {
        this.bandeRank = bandeRank;
    }

    private HashSet<String> invitations;

    public HashSet<String> invitations() {
        return invitations;
    }

    public void invitations(final HashSet<String> invitations) {
        this.invitations = invitations;
    }
}
