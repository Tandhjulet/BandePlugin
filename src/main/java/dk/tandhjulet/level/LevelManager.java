package dk.tandhjulet.level;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import dk.tandhjulet.BandePlugin;

public class LevelManager {
    private static HashMap<Integer, Level> levels;

    static {
        levels = new HashMap<>();
    }

    public LevelManager() {
        levels = new HashMap<>();

        AtomicInteger index = new AtomicInteger(1);

        final File file = new File(BandePlugin.getPlugin().getDataFolder(), "levels");
        if (!file.exists()) {
            file.mkdirs();
        }

        // create levels embedded in jar, if they dont exist
        for (String name : new String[] { "level-2.yml" }) {
            File f = new File(BandePlugin.getPlugin().getDataFolder(), "levels" + File.separator + name);
            if (!f.exists()) {
                BandePlugin.getPlugin().saveResource("levels" + File.separator + name, false);
            }
        }

        // load every level in /levels folder.

        while (new File(file, "level-" + index.incrementAndGet() + ".yml").exists()) {
            File f = new File(file, "level-" + index.get() + ".yml");

            Level l = new Level(f);
            Integer i = Integer.valueOf(index.get());

            levels.put(i, l);
        }

    }

    public static void reload() {
        BandePlugin.setLevelManager(new LevelManager());
    }

    public Level get(Integer level) {
        return levels.get(level);
    }
}
