package dk.tandhjulet.gui;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import dk.tandhjulet.utils.Utils;

public class InvDataHolder implements Serializable {
    private static transient final long serialVersionUID = 9L;

    private HashMap<String, String> idToName;
    private HashMap<String, Integer> idToSize;

    public InvDataHolder() {
        idToName = new HashMap<String, String>() {
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

        idToSize = new HashMap<String, Integer>() {
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
    }

    public void setName(String id, String name) {
        if (idToName.containsKey(id))
            idToName.put(id, Utils.getColored(name));
    }

    public Integer getSize(String id) {
        return idToSize.get(id);
    }

    public String getName(String id) {
        if (!idToName.containsKey(id))
            return id;
        return idToName.get(id);
    }

    public Set<String> getIds() {
        return idToName.keySet();
    }

    public Collection<String> getNames() {
        return idToName.values();
    }
}
