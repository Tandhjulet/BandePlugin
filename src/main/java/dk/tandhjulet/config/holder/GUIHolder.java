package dk.tandhjulet.config.holder;

import java.util.HashMap;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import dk.tandhjulet.gui.GUIItem;

@ConfigSerializable
public class GUIHolder {

    private HashMap<Integer, GUIItem> contents = new HashMap<>();

    public HashMap<Integer, GUIItem> contents() {
        return contents;
    }

    public void contents(final HashMap<Integer, GUIItem> contents) {
        this.contents = contents;
    }

    private String id;

    public String id() {
        return id;
    }

    public void id(final String id) {
        this.id = id;
    }

    private Integer rows = 6;

    public Integer rows() {
        return rows;
    }

    public void rows(final Integer rows) {
        this.rows = rows;
    }
}
