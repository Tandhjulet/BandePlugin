package dk.tandhjulet.storage;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.config.IConfig;
import dk.tandhjulet.config.BandeConfig;

public class Config implements IConfig {
    private BandeConfig config;

    public Config() {
        File file = new File(BandePlugin.getPlugin().getDataFolder(), "config.yml");
        if (!file.exists())
            BandePlugin.getPlugin().saveResource("config.yml", false);
        config = new BandeConfig(file);

        reloadConfig();
        config.save();
    }

    private List<String> houses;

    public List<String> getHouses() {
        return houses;
    }

    public void setHouses(final List<String> houses) {
        this.houses = houses;
        config.save();
    }

    public void addHouse(String house) {
        houses.add(house);
        config.setProperty("houses", houses);

        config.save();
    }

    public void removeHouse(String house) {
        houses.remove(house);
        config.setProperty("houses", houses);

        config.save();
    }

    public Integer getSpeedCooldown() {
        return config.getInt("cooldowns.speed-buff-cooldown", 900000);
    }

    public Integer getHungerCooldown() {
        return config.getInt("cooldowns.hunger-buff-cooldown", 450000);
    }

    public Integer getStrengthCooldown() {
        return config.getInt("cooldowns.strength-buff-cooldown", 1800000);
    }

    public World getMainWorld() {
        return Bukkit.getWorld(config.getString("WORLD", Bukkit.getWorlds().get(0).getName()));
    }

    public Integer getBandeHusReqLevel() {
        return config.getInt("bande_level_required_for_bandehus", 5);
    }

    public String getPrefferedProvider() {
        return config.getString("preferred_celle_plugin", "auto");
    }

    public List<String> getVagtGroup() {
        return config.getList("vagt_group", String.class);
    }

    public List<String> getOfficerGroup() {
        return config.getList("vagt_group", String.class);
    }

    public String getMemberListFormat() {
        return config.getString("member_list_format", "&d&l • &8[&7{0}&8] &f{1}");
    }

    public String getTopPreUpdateMessage() {
        return config.getString("top_pre_update", "&aBande-top opdater...");
    }

    public String getTopAfterUpdateMessage() {
        return config.getString("top_after_update", "&aBande-top er blevet opdateret!");
    }

    public Integer getStartAlly() {
        return config.getInt("bande.start_ally_amount", 3);
    }

    public Integer getStartRival() {
        return config.getInt("bande.start_rival_amount", 3);
    }

    public Integer getStartMembers() {
        return config.getInt("bande.start_member_amount", 5);
    }

    public Integer getExtraMemberPrice() {
        return config.getInt("bande.extra_member_price", 5000);
    }

    public Integer getMaxAlliances() {
        return config.getInt("bande.max_alliances", 6);
    }

    public Integer getMaxRivals() {
        return config.getInt("bande.max_rivals", 6);
    }

    public Integer getMaxMembers() {
        return config.getInt("bande.max_members", 6);
    }

    public List<String> getDisallowedNames() {
        return config.getList("bande.banned-names", String.class);
    }

    public Integer getMinLength() {
        return config.getInt("bande.bande_name_minimum_length", 3);
    }

    public Integer getMaxLength() {
        return config.getInt("bande.bande_name_maximum_length", 10);
    }

    public String getNoAllyMessage() {
        return config.getString("no_ally_message", "&fIngen allierede...");
    }

    public String getNoRivalMessage() {
        return config.getString("no_rival_message", "&fIngen rivaler...");
    }

    public String getRivalListFormat() {
        return config.getString("rival_list_format", "&d&l • &c");
    }

    public String getAllyListFormat() {
        return config.getString("ally_list_format", "&d&l • &a");
    }

    public Integer getHungerBuffPrice() {
        return config.getInt("bande.shop.hunger_buff_price", 10000);
    }

    public Integer getSpeedBuffPrice() {
        return config.getInt("bande.shop.speed_buff_price", 12500);
    }

    public Integer getStrengthBuffPrice() {
        return config.getInt("bande.shop.strength_buff_price", 12500);
    }

    public Integer getBandeDamagePrice() {
        return config.getInt("bande.rival_damage_price", 500);
    }

    public Integer getAllyDamagePrice() {
        return config.getInt("bande.ally_damage_price", 500);
    }

    public Integer getExtraAllyPrice() {
        return config.getInt("bande.extra_ally_price", 2500);
    }

    public Integer getExtraRivalPrice() {
        return config.getInt("bande.extra_rival_price", 2500);
    }

    public Integer getCreatePrice() {
        return config.getInt("bande.create_price", 2500);
    }

    public String getNoBande() {
        return config.getString("bande.ingen_bande", "Ingen bande");
    }

    @Override
    public void reloadConfig() {
        config.load();

        BandePlugin.setChatEnabled(config.getBoolean("custom_chat_enabled", false));
        houses = config.getList("houses", String.class);
    }
}
