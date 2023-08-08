package dk.tandhjulet.config.holder;

import java.util.HashMap;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class TopHolder {
    public HashMap<String, Integer> topOffiKills = new HashMap<>();

    public HashMap<String, Integer> topOffiKills() {
        return topOffiKills;
    }

    public void topOffiKills(HashMap<String, Integer> topOffiKills) {
        this.topOffiKills = topOffiKills;
    }

    public HashMap<String, Integer> topVagtKills = new HashMap<>();

    public HashMap<String, Integer> topVagtKills() {
        return topVagtKills;
    }

    public void topVagtKills(HashMap<String, Integer> topVagtKills) {
        this.topVagtKills = topVagtKills;
    }

    public HashMap<String, Integer> topLevel = new HashMap<>();

    public HashMap<String, Integer> topLevels() {
        return topLevel;
    }

    public void topLevels(HashMap<String, Integer> topLevel) {
        this.topLevel = topLevel;
    }

    public HashMap<String, Integer> topFangeKills = new HashMap<>();

    public HashMap<String, Integer> topFangeKills() {
        return topFangeKills;
    }

    public void topFangeKills(HashMap<String, Integer> topFangeKills) {
        this.topFangeKills = topFangeKills;
    }
}
