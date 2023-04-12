package dk.tandhjulet.huse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dk.tandhjulet.BandePlugin;

public class HouseHolder implements Serializable {
    private static transient final long serialVersionUID = 8L;

    public List<String> houses;

    public HouseHolder() {
        houses = new ArrayList<>();
    }

    public void add(String regionId) {
        houses.add(regionId);
        save();
    }

    public void remove(String regionId) {
        houses.remove(regionId);
        save();
    }

    public Boolean contains(String regionId) {
        return houses.contains(regionId);
    }

    private void save() {
        BandePlugin.getFileManager().saveHouses(this);
    }

    public ArrayList<String> get() {
        return new ArrayList<>(houses);
    }
}