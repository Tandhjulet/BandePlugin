package dk.tandhjulet.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.storage.Message;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            BandePlayer victim = BandePlugin.getAPI().getPlayer((Player) e.getEntity());
            if (victim.hasBande()) {
                BandePlayer attacker = BandePlugin.getAPI().getPlayer((Player) e.getDamager());
                if (attacker.hasBande() && attacker.getBande() == victim.getBande()) {
                    Integer chance = Integer.valueOf(100 - attacker.getBande().getBandeSkade());

                    e.setCancelled(Math.random() < (chance.doubleValue() / 100));

                    Message.sendReplaced(attacker, "bande.attack_cancelled", null);

                } else if (attacker.hasBande()
                        && attacker.getBande().getAlliances().contains(victim.getBande().getName())) {
                    Integer chance = Integer.valueOf(100 - attacker.getBande().getAllySkade());

                    e.setCancelled(Math.random() < (chance.doubleValue() / 100));

                    Message.sendReplaced(attacker, "ally.ally_attack_cancelled", null);
                }
            }
        }
    }
}
