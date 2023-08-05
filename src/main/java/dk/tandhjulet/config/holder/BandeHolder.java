package dk.tandhjulet.config.holder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.enums.BandeRank;

@ConfigSerializable
public class BandeHolder {
    private String name;

    public String name() {
        return name;
    }

    public void name(final String name) {
        this.name = name;
    }

    private HashMap<BandeRank, HashSet<UUID>> members = new HashMap<BandeRank, HashSet<UUID>>() {
        {
            put(BandeRank.EJER, new HashSet<>());
            put(BandeRank.ADMIN, new HashSet<>());
            put(BandeRank.MOD, new HashSet<>());
            put(BandeRank.MEDLEM, new HashSet<>());
        }
    };

    public HashMap<BandeRank, HashSet<UUID>> members() {
        return members;
    }

    public void members(final HashMap<BandeRank, HashSet<UUID>> members) {
        this.members = members;
    }

    private HashSet<String> alliances = new HashSet<String>();

    public HashSet<String> alliances() {
        return alliances;
    }

    public void alliances(final HashSet<String> alliances) {
        this.alliances = alliances;
    }

    private HashSet<String> rivals = new HashSet<String>();

    public HashSet<String> rivals() {
        return rivals;
    }

    public void rivals(final HashSet<String> rivals) {
        this.rivals = rivals;
    }

    private Integer level = 1;

    public Integer level() {
        return level;
    }

    public void level(final Integer level) {
        this.level = level;
    }

    private String bandeHus;

    public String bandeHus() {
        return bandeHus;
    }

    public void bandeHus(final String bandeHus) {
        this.bandeHus = bandeHus;
    }

    private List<String> access = new ArrayList<>();

    public List<String> access() {
        return access;
    }

    public void access(final List<String> access) {
        this.access = access;
    }

    private HashSet<UUID> invitations = new HashSet<>();

    public HashSet<UUID> invitations() {
        return invitations;
    }

    public void invitations(final HashSet<UUID> invitations) {
        this.invitations = invitations;
    }

    private HashSet<String> incommingAllyInvitations = new HashSet<>();

    public HashSet<String> incommingAllyInvitations() {
        return incommingAllyInvitations;
    }

    public void incommingAllyInvitations(final HashSet<String> incommingAllyInvitations) {
        this.incommingAllyInvitations = incommingAllyInvitations;
    }

    private Integer maxMembers = BandePlugin.getConfiguration().getMaxMembers();

    public Integer maxMembers() {
        return maxMembers;
    }

    public void maxMembers(final Integer maxMembers) {
        this.maxMembers = maxMembers;
    }

    private Integer maxAlliances = BandePlugin.getConfiguration().getMaxAlliances();

    public Integer maxAlliances() {
        return maxAlliances;
    }

    public void maxAlliances(final Integer maxAlliances) {
        this.maxAlliances = maxAlliances;
    }

    private Integer maxRivals = BandePlugin.getConfiguration().getMaxRivals();

    public Integer maxRivals() {
        return maxRivals;
    }

    public void maxRivals(final Integer maxRivals) {
        this.maxRivals = maxRivals;
    }

    private Integer allySkade = 100;

    public Integer allySkade() {
        return allySkade;
    }

    public void allySkade(final Integer allySkade) {
        this.allySkade = allySkade;
    }

    private Integer bandeSkade = 100;

    public Integer bandeSkade() {
        return bandeSkade;
    }

    public void bandeSkade(final Integer bandeSkade) {
        this.bandeSkade = bandeSkade;
    }

    private BigDecimal balance = new BigDecimal(0);

    public BigDecimal balance() {
        return this.balance;
    }

    public void balance(final BigDecimal balance) {
        this.balance = balance;
    }
}
