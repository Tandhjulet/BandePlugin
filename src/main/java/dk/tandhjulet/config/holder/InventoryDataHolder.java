package dk.tandhjulet.config.holder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import dk.tandhjulet.utils.Utils;

@ConfigSerializable
public class InventoryDataHolder {
    private Map<String, String> idToName = new HashMap<String, String>() {
        {
            put("hjem_ingen_bande", Utils.getColored("&d&lBANDE &8• &f&lSTART"));
            put("hjem_med_bande", Utils.getColored("&d&lBANDE &8• &f&lSTART"));
            put("bande_invites", Utils.getColored("&d&lBANDE &8• &f&lINVITES"));
            put("bande_top", Utils.getColored("&d&lBANDE &8• &f&lLEADERBOARDS"));
            put("bande_shop", Utils.getColored("&d&lBANDE &8• &f&lSHOP"));
            put("bande_buffs", Utils.getColored("&d&lBANDE &8• &f&lBUFFS"));
            put("bande_info", Utils.getColored("&d&lBANDE &8• &f&lINFORMATION"));
            put("bande_forhold_rival", Utils.getColored("&d&lBANDE &8• &f&lFORHOLD"));
            put("bande_forhold_ally", Utils.getColored("&d&lBANDE &8• &f&lFORHOLD"));
            put("bande_medlemmer", Utils.getColored("&d&lBANDE &8• &f&lMEDLEMMER"));
            put("bande_indstillinger", Utils.getColored("&d&lBANDE &8• &f&lINDSTILLINGER"));
            put("personlige_indstillinger", Utils.getColored("&d&lBANDE &8• &f&lINDSTILLINGER &7(Personlige)"));
            put("bande_manage_member", Utils.getColored("&d&lBANDE &8• &f&lADMINISTRER MEDLEM"));
            put("bande_search", Utils.getColored("&d&lBANDE &8• &f&lSØGNING"));
            put("bande_omraader", Utils.getColored("&d&lBANDE &8• &f&lBANDE OMRÅDER"));
            put("bande_huse", Utils.getColored("&d&lBANDE &8• &f&lHUSE"));
        }
    };

    private Map<String, Integer> idToSize = new HashMap<String, Integer>() {
        {
            put("bande_huse", 5);
            put("bande_omraader", 5);
            put("hjem_ingen_bande", 5);
            put("hjem_med_bande", 5);
            put("bande_invites", 5);
            put("bande_top", 5);
            put("bande_shop", 6);
            put("bande_buffs", 3);
            put("bande_info", 6);
            put("bande_forhold_rival", 5);
            put("bande_forhold_ally", 5);
            put("bande_medlemmer", 5);
            put("bande_indstillinger", 5);
            put("personlige_indstillinger", 5);
            put("bande_manage_member", 3);
            put("bande_search", 6);
        }
    };

    public void setName(String id, String name) {
        if (idToName.containsKey(id))
            idToName.put(id, Utils.getColored(name));
    }

    public void setSizes(HashMap<String, Integer> map) {
        this.idToSize = map;
    }

    public void setNames(HashMap<String, String> map) {
        this.idToName = map;
    }

    public Integer getSize(String id) {
        return idToSize.getOrDefault(id, 6);
    }

    public String getName(String id) {
        return idToName.getOrDefault(id, id);
    }

    public Set<String> getIds() {
        return idToName.keySet();
    }

    public Collection<String> getNames() {
        return idToName.values();
    }

    public void setSize(String id, Integer size) {
        if (idToSize.containsKey(id))
            idToSize.put(id, size);
    }
}
