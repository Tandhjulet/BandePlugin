package dk.tandhjulet.level;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.utils.Logger;
import net.kyori.adventure.text.Component;

public class Level {
    public HashMap<Integer, LevelRequirement> reqs;
    public Integer price;
    public boolean isEnabled;

    public Level(File file) {
        isEnabled = true;
        reqs = new HashMap<>();
        price = 0;

        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        AtomicInteger index = new AtomicInteger(1);

        try {
            for (String key : config.getConfigurationSection("krav").getKeys(false)) {

                if ((StringUtils.isNumeric(key) && Integer.parseInt(key) == index.get())
                        || key.equalsIgnoreCase("price")) {
                    ConfigurationSection section = config.getConfigurationSection("krav." + key);
                    if (key.equalsIgnoreCase("price")) {
                        this.price = section.getInt("price");
                    }
                    reqs.put(index.get(), new LevelRequirement(section));

                    index.incrementAndGet();
                }
            }
        } catch (Exception e) {
            Logger.severe("Could not parse level " + file.getAbsolutePath());
            isEnabled = false;
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public List<Component> apply(OfflinePlayer p, Bande b) {
        List<Component> out = new LinkedList<>();

        reqs.forEach((i, req) -> {
            out.add(Component.text(req.apply(p, b)));
        });

        return out;
    }

    public boolean get(OfflinePlayer p, Bande b) {
        List<Boolean> out = new LinkedList<>();

        reqs.forEach((i, req) -> {
            out.add(req.get(p, b));
        });

        return !out.contains(false);
    }

    public Integer getPrice() {
        return price;
    }
}
