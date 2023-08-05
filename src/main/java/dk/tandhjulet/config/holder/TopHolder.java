package dk.tandhjulet.config.holder;

import java.util.HashMap;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class TopHolder {
    public HashMap<String, Integer> offiKills = new HashMap<>();

    public HashMap<String, Integer> offiKills() {
        return offiKills;
    }

    public void offiKills(HashMap<String, Integer> offiKills) {
        this.offiKills = offiKills;
    }

    public HashMap<String, Integer> vagtKills = new HashMap<>();

    public HashMap<String, Integer> vagtKills() {
        return vagtKills;
    }

    public void vagtKills(HashMap<String, Integer> vagtKills) {
        this.vagtKills = vagtKills;
    }

    public HashMap<String, Integer> bandeLevels = new HashMap<>();

    public HashMap<String, Integer> bandeLevels() {
        return bandeLevels;
    }

    public void bandeLevels(HashMap<String, Integer> bandeLevels) {
        this.bandeLevels = bandeLevels;
    }

    public HashMap<String, Integer> fangeKills = new HashMap<>();

    public HashMap<String, Integer> fangeKills() {
        return fangeKills;
    }

    public void fangeKills(HashMap<String, Integer> fangeKills) {
        this.fangeKills = fangeKills;
    }
}
