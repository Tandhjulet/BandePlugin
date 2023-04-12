package dk.tandhjulet.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;

public class CooldownManager {
    public float cooldown;

    public CooldownManager(float cooldown) {
        this.cooldown = cooldown;
    }

    private final Map<Bande, BigDecimal> cooldowns = new HashMap<>();

    public void setCooldownTimestamp(BandePlayer player, long time) {
        if (time < 1) {
            cooldowns.remove(player.getBande());
        } else {
            cooldowns.put(player.getBande(), new BigDecimal(time));
        }
    }

    public boolean isOnCooldown(BandePlayer player) {
        long timeLeft = System.currentTimeMillis() - getCooldownTimestamp(player).longValue();
        return !(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= (long) cooldown);
    }

    public BigDecimal getCooldownTimestamp(BandePlayer player) {
        return cooldowns.getOrDefault(player.getBande(), new BigDecimal(0));
    }

    public String getFormattedTimeLeft(BandePlayer player) {
        long timeLeft = (long) cooldown - (System.currentTimeMillis() - getCooldownTimestamp(player).longValue());
        List<String> out = new ArrayList<>();

        long seconds = (timeLeft / 1000) % 60;
        long minutes = (timeLeft / (1000 * 60)) % 60;
        long hours = (timeLeft / (1000 * 60 * 60)) % 24;

        if (hours > 0) {
            out.add(hours == 1 ? hours + " time" : hours + " timer");
        }
        if (minutes > 0) {
            out.add(minutes == 1 ? minutes + " minut" : minutes + " minutter");
        }
        if (seconds > 0) {
            out.add(seconds == 1 ? seconds + " sekundt" : seconds + " sekunder");
        }

        String f = String.join(", ", out);
        if (out.size() == 2) {
            f = out.get(0) + " og " + out.get(1);
        } else if (out.size() == 3) {
            f = out.get(0) + ", " + out.get(1) + " og " + out.get(2);
        }
        return f;
    }
}