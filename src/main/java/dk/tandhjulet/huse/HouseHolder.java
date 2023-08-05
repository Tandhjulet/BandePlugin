package dk.tandhjulet.huse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dk.tandhjulet.migrator.Migrate;

@Deprecated
public class HouseHolder implements Serializable {
    @Deprecated
    private static transient final long serialVersionUID = 8L;

    @Migrate
    public List<String> houses;

    public HouseHolder() {
        houses = new ArrayList<>();
    }

    public void add(String regionId) {
        houses.add(regionId);
    }

    public void remove(String regionId) {
        houses.remove(regionId);
    }

    public Boolean contains(String regionId) {
        return houses.contains(regionId);
    }

    public ArrayList<String> get() {
        return new ArrayList<>(houses);
    }
}