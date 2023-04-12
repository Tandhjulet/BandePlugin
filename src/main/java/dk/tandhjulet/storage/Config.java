package dk.tandhjulet.storage;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import dk.tandhjulet.BandePlugin;

public class Config {
    private FileConfiguration config;

    public Config() {
        this.config = BandePlugin.getPlugin().getConfig();
        BandePlugin.setChatEnabled(config.getBoolean("custom_chat_enabled"));
    }

    public Integer getSpeedCooldown() {
        return config.getInt("cooldowns.speed-buff-cooldown");
    }

    public Integer getHungerCooldown() {
        return config.getInt("cooldowns.hunger-buff-cooldown");
    }

    public Integer getStrengthCooldown() {
        return config.getInt("cooldowns.strength-buff-cooldown");
    }

    public World getMainWorld() {
        return Bukkit.getWorld(config.getString("WORLD"));
    }

    public Integer getBandeHusReqLevel() {
        return config.getInt("bande_level_required_for_bandehus");
    }

    public String getPrefferedProvider() {
        return config.getString("preferred_celle_plugin");
    }

    public List<String> getVagtGroup() {
        return config.getStringList("vagt_group");
    }

    public List<String> getOfficerGroup() {
        return config.getStringList("officer_group");
    }

    public String getMemberListFormat() {
        return config.getString("member_list_format");
    }

    public String getTopPreUpdateMessage() {
        return config.getString("top_pre_update");
    }

    public String getTopAfterUpdateMessage() {
        return config.getString("top_after_update");
    }

    public Integer getStartAlly() {
        return config.getInt("bande.start_ally_amount");
    }

    public Integer getStartRival() {
        return config.getInt("bande.start_rival_amount");
    }

    public Integer getStartMembers() {
        return config.getInt("bande.start_member_amount");
    }

    public Integer getExtraMemberPrice() {
        return config.getInt("bande.extra_member_price");
    }

    public Integer getMaxAlliances() {
        return config.getInt("bande.max_alliances");
    }

    public Integer getMaxRivals() {
        return config.getInt("bande.max_rivals");
    }

    public Integer getMaxMembers() {
        return config.getInt("bande.max_members");
    }

    public List<String> getDisallowedNames() {
        return config.getStringList("bande.banned-names");
    }

    public Integer getMinLength() {
        return config.getInt("bande.bande_name_minimum_length");
    }

    public Integer getMaxLength() {
        return config.getInt("bande.bande_name_maximum_length");
    }

    public String getNoAllyMessage() {
        return config.getString("no_ally_message");
    }

    public String getNoRivalMessage() {
        return config.getString("no_rival_message");
    }

    public String getRivalListFormat() {
        return config.getString("rival_list_format");
    }

    public String getAllyListFormat() {
        return config.getString("ally_list_format");
    }

    public Integer getHungerBuffPrice() {
        return config.getInt("bande.shop.hunger_buff_price");
    }

    public Integer getSpeedBuffPrice() {
        return config.getInt("bande.shop.speed_buff_price");
    }

    public Integer getStrengthBuffPrice() {
        return config.getInt("bande.shop.strength_buff_price");
    }

    public Integer getBandeDamagePrice() {
        return config.getInt("bande.rival_damage_price");
    }

    public Integer getAllyDamagePrice() {
        return config.getInt("bande.ally_damage_price");
    }

    public Integer getExtraAllyPrice() {
        return config.getInt("bande.extra_ally_price");
    }

    public Integer getExtraRivalPrice() {
        return config.getInt("bande.extra_rival_price");
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public Integer getCreatePrice() {
        return config.getInt("bande.create_price");
    }

    public String getNoBande() {
        return config.getString("bande.ingen_bande");
    }

    public void reload() {
        BandePlugin.getPlugin().reloadConfig();
        this.config = BandePlugin.getPlugin().getConfig();

        BandePlugin.setChatEnabled(config.getBoolean("custom_chat_enabled"));
    }
}
