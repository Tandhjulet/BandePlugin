package dk.tandhjulet.gui;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import dev.triumphteam.gui.components.GuiAction;
import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.api.nbt.NBTAPI;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.commands.subcommands.Forlad;
import dk.tandhjulet.enums.ChatStatus;
import dk.tandhjulet.hooks.WorldGuardHook;
import dk.tandhjulet.level.Level;
import dk.tandhjulet.storage.Message;
import dk.tandhjulet.utils.CooldownManager;
import dk.tandhjulet.utils.Logger;
import dk.tandhjulet.utils.Utils;

public class TypeManager {
    private HashMap<String, GuiAction<InventoryClickEvent>> map;
    private CooldownManager speedCooldownManager;
    private CooldownManager strengthCooldownManager;
    private CooldownManager hungerCooldownManager;

    public void reloadCooldowns() {
        this.speedCooldownManager = new CooldownManager(BandePlugin.getConfiguration().getSpeedCooldown());
        this.strengthCooldownManager = new CooldownManager(BandePlugin.getConfiguration().getStrengthCooldown());
        this.hungerCooldownManager = new CooldownManager(BandePlugin.getConfiguration().getHungerCooldown());
    }

    public TypeManager() {
        this.speedCooldownManager = new CooldownManager(BandePlugin.getConfiguration().getSpeedCooldown());
        this.strengthCooldownManager = new CooldownManager(BandePlugin.getConfiguration().getStrengthCooldown());
        this.hungerCooldownManager = new CooldownManager(BandePlugin.getConfiguration().getHungerCooldown());

        map = new HashMap<>();

        map.put("bande-create", (e) -> {
            Message.sendReplaced((Player) e.getWhoClicked(), "bande_create.creation_started", null);

            BandePlayer player = BandePlugin.getAPI().getPlayer((Player) e.getWhoClicked());
            player.setChat(ChatStatus.BANDE_OPRET);

            Utils.scheduleClose(e);
        });

        map.put("bande-invites", (e) -> {
            BandePlugin.getAPI().openGUI((Player) e.getWhoClicked(), "bande_invites");
        });

        map.put("bande-forlad", (e) -> {
            BandePlayer player = BandePlugin.getAPI().getPlayer((Player) e.getWhoClicked());
            Forlad.run(player, null);
        });

        map.put("luk-siden", (e) -> {
            Utils.scheduleClose(e);
        });

        map.put("gaa-tilbage", (e) -> {
            BandePlayer player = BandePlugin.getAPI().getPlayer((Player) e.getWhoClicked());
            if (player.hasBande()) {
                if (player.getPreviousGUI() != null) {
                    BandePlugin.getAPI().openGUI((Player) e.getWhoClicked(), player.getPreviousGUI());
                    if (player.getPreviousGUI() == "bande_medlemmer") {
                        player.setPreviousGUI("bande_info");
                    } else {
                        player.setPreviousGUI(null);
                    }
                } else {
                    BandePlugin.getAPI().openGUI((Player) e.getWhoClicked(), "hjem_med_bande");
                }
            } else {
                BandePlugin.getAPI().openGUI((Player) e.getWhoClicked(), "hjem_ingen_bande");
            }
        });

        map.put("bande-indstillinger", (e) -> {
            BandePlugin.getAPI().openGUI((Player) e.getWhoClicked(), "bande_indstillinger");
        });

        map.put("personlige-indstillinger", (e) -> {
            BandePlugin.getAPI().openGUI((Player) e.getWhoClicked(), "personlige_indstillinger");
        });

        map.put("extra-allierede", (e) -> {
            BandePlayer player = BandePlugin.getAPI().getPlayer((Player) e.getWhoClicked());

            if (player.getBande().getMaxAlliances() >= BandePlugin.getConfiguration().getMaxAlliances()) {
                Message.sendReplaced(player, "ally.no_more", null);
                return;
            }

            Integer price = BandePlugin.getConfiguration().getExtraAllyPrice();
            if (player.getBande().getBalance().compareTo(new BigDecimal(price)) < 1) {
                Message.sendReplaced(player, "general.no_money", null, price.toString());
                return;
            }
            player.getBande().removeBalance(new BigDecimal(price));

            Message.sendReplaced(player, "ally.bought", null);
            player.getBande().addMaxAlliance();
        });

        map.put("ally-skade", (e) -> {
            BandePlayer player = BandePlugin.getAPI().getPlayer((Player) e.getWhoClicked());

            if (player.getBande().getAllySkade() == 0) {
                Message.sendReplaced(player, "ally.ally-skade-maxed", null);
                return;
            }

            Integer price = BandePlugin.getConfiguration().getAllyDamagePrice();
            if (player.getBande().getBalance().compareTo(new BigDecimal(price)) < 1) {
                Message.sendReplaced(player, "general.no_money", null, price.toString());
                return;
            }
            player.getBande().removeBalance(new BigDecimal(price));

            player.getBande().removeAllySkade();
            Message.sendReplaced(player, "ally.ally-skade-bought", null,
                    String.valueOf(100 - player.getBande().getAllySkade()));
        });

        map.put("bande-omraeder-menu", (e) -> {
            BandePlugin.getAPI().openGUI((Player) e.getWhoClicked(), "bande_omraader");
        });

        map.put("extra-bandemedlem", (e) -> {
            BandePlayer player = BandePlugin.getAPI().getPlayer((Player) e.getWhoClicked());

            if (player.getBande().getMaxMembers() == BandePlugin.getConfiguration().getMaxMembers()) {
                Message.sendReplaced(player, "bande.bande-medlem-maxed", null);
                return;
            }

            Integer price = BandePlugin.getConfiguration().getExtraMemberPrice();
            if (player.getBande().getBalance().compareTo(new BigDecimal(price)) < 1) {
                Message.sendReplaced(player, "general.no_money", null, price.toString());
                return;
            }
            player.getBande().removeBalance(new BigDecimal(price));

            Message.sendReplaced(player.getBande().getMemberIterable(), "bande.ekstra-medlem-bought", null);
            player.getBande().addMaxMember();
        });

        map.put("extra-rival", (e) -> {
            BandePlayer player = BandePlugin.getAPI().getPlayer((Player) e.getWhoClicked());

            if (player.getBande().getMaxRivals() >= BandePlugin.getConfiguration().getMaxRivals()) {
                Message.sendReplaced(player, "rival.no_more", null);
                return;
            }

            Integer price = BandePlugin.getConfiguration().getExtraRivalPrice();
            if (player.getBande().getBalance().compareTo(new BigDecimal(price)) < 1) {
                Message.sendReplaced(player, "general.no_money", null, price.toString());
                return;
            }
            player.getBande().removeBalance(new BigDecimal(price));

            Message.sendReplaced(player, "rival.bought", null);
            player.getBande().addMaxRivals();
        });

        map.put("bande-skade", (e) -> {
            BandePlayer player = BandePlugin.getAPI().getPlayer((Player) e.getWhoClicked());

            if (player.getBande().getBandeSkade() == 0) {
                Message.sendReplaced(player, "bande.bande-skade-maxed", null);
                return;
            }

            Integer price = BandePlugin.getConfiguration().getBandeDamagePrice();
            if (player.getBande().getBalance().compareTo(new BigDecimal(price)) < 1) {
                Message.sendReplaced(player, "general.no_money", null, price.toString());
                return;
            }
            player.getBande().removeBalance(new BigDecimal(price));

            player.getBande().removeBandeSkade();
            Message.sendReplaced(player, "bande.bande-skade-bought", null,
                    String.valueOf(100 - player.getBande().getBandeSkade()));
        });

        map.put("bande-hus-menu", (e) -> {
            BandePlugin.getAPI().openGUI((Player) e.getWhoClicked(), "bande_huse");
        });

        map.put("bande-top-menu", (e) -> {
            BandePlugin.getAPI().openGUI((Player) e.getWhoClicked(), "bande_top");
        });

        map.put("bande-buffs-menu", (e) -> {
            BandePlugin.getAPI().openGUI((Player) e.getWhoClicked(), "bande_buffs");
        });

        map.put("bande-buffs-speed", (e) -> {
            Player p = (Player) e.getWhoClicked();
            BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer(p);

            if (speedCooldownManager.isOnCooldown(bandePlayer)) {
                Message.sendReplaced(p, "cooldown.speed-cooldown", null,
                        speedCooldownManager.getFormattedTimeLeft(bandePlayer));
                return;
            }

            Integer price = BandePlugin.getConfiguration().getSpeedBuffPrice();
            if (bandePlayer.getBande().getBalance().compareTo(new BigDecimal(price)) < 1) {
                Message.sendReplaced(p, "general.no_money", null, price.toString());
                return;
            }

            if (bandePlayer.hasBande()) {
                bandePlayer.getBande().removeBalance(new BigDecimal(price));

                speedCooldownManager.setCooldownTimestamp(bandePlayer, System.currentTimeMillis());

                Iterable<UUID> members = bandePlayer.getBande().getMemberIterable();
                members.forEach(uuid -> {
                    Player member = Bukkit.getPlayer(uuid);
                    Message.sendReplaced(member, "bande.buffs-bought", null, p.getName(), "speed");
                    if (member != null && member.isOnline()) {
                        member.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 15 * 20 * 60, 0), true);
                    }
                });
            } else {
                Message.sendReplaced(p, "bande_command.no_bande", null);
                return;
            }
        });

        map.put("bande-buffs-hunger", (e) -> {
            Player p = (Player) e.getWhoClicked();
            Integer price = BandePlugin.getConfiguration().getHungerBuffPrice();
            BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer(p);

            if (hungerCooldownManager.isOnCooldown(bandePlayer)) {
                Message.sendReplaced(p, "cooldown.hunger-cooldown", null,
                        hungerCooldownManager.getFormattedTimeLeft(bandePlayer));
                return;
            }

            if (bandePlayer.getBande().getBalance().compareTo(new BigDecimal(price)) < 1) {
                Message.sendReplaced(p, "general.no_money", null, price.toString());
                return;
            }

            if (bandePlayer.hasBande()) {
                bandePlayer.getBande().removeBalance(new BigDecimal(price));
                hungerCooldownManager.setCooldownTimestamp(bandePlayer, System.currentTimeMillis());

                Iterable<UUID> members = bandePlayer.getBande().getMemberIterable();
                members.forEach(uuid -> {
                    Player member = Bukkit.getPlayer(uuid);
                    Message.sendReplaced(member, "bande.buffs-bought", null, p.getName(), "saturation");
                    if (member != null && member.isOnline()) {
                        member.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 60 * 20 * 60, 10), true);
                    }
                });
            } else {
                Message.sendReplaced(p, "bande_command.no_bande", null);
                return;
            }
        });

        map.put("bande-buffs-strength", (e) -> {
            Player p = (Player) e.getWhoClicked();
            Integer price = BandePlugin.getConfiguration().getStrengthBuffPrice();
            BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer(p);

            if (strengthCooldownManager.isOnCooldown(bandePlayer)) {
                Message.sendReplaced(p, "cooldown.strength-cooldown", null,
                        strengthCooldownManager.getFormattedTimeLeft(bandePlayer));
                return;
            }

            if (bandePlayer.getBande().getBalance().compareTo(new BigDecimal(price)) < 1) {
                Message.sendReplaced(p, "general.no_money", null, price.toString());
                return;
            }

            if (bandePlayer.hasBande()) {
                bandePlayer.getBande().removeBalance(new BigDecimal(price));
                strengthCooldownManager.setCooldownTimestamp(bandePlayer, System.currentTimeMillis());

                Iterable<UUID> members = bandePlayer.getBande().getMemberIterable();
                members.forEach(uuid -> {
                    Player member = Bukkit.getPlayer(uuid);
                    Message.sendReplaced(member, "bande.buffs-bought", null, p.getName(), "strength");
                    if (member != null && member.isOnline()) {
                        member.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60 * 20, 0), true);
                    }
                });
            } else {
                Message.sendReplaced(p, "bande_command.no_bande", null);
                return;
            }
        });

        map.put("bande-level", (e) -> {
            BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer((Player) e.getWhoClicked());
            Bande bande = bandePlayer.getBande();
            if (bande == null) {
                return;
            }

            Level level = BandePlugin.getLevelManager().get(bande.getLevel() + 1);
            if (level == null) {
                Message.sendReplaced(bandePlayer, "level.max_level", null);
                return;
            }

            if (level.get((Player) e.getWhoClicked(), bande)) {
                bande.addLevel();
                Message.sendReplaced(Bukkit.getOnlinePlayers().toArray(new Player[0]), "level.bande_level_up", null,
                        bande.getName(), bande.getLevel().toString());
            } else {
                Message.sendReplaced(bandePlayer, "level.unfulfilled_reqs", null);
            }
        });

        map.put("bande-tilfoej-penge", (e) -> {
            BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer((Player) e.getWhoClicked());
            bandePlayer.setChat(ChatStatus.BANK_DESPOSIT);

            Message.sendReplaced((Player) e.getWhoClicked(), "bank.pre_deposit_message", null);

            Utils.scheduleClose(e);
        });

        map.put("bande-medlemmer-menu", (e) -> {
            BandePlugin.getAPI().openGUI((Player) e.getWhoClicked(), "bande_medlemmer");
        });

        map.put("bande-shop-menu", (e) -> {
            BandePlugin.getAPI().openGUI((Player) e.getWhoClicked(), "bande_shop");
        });

        map.put("bande-info-menu", (e) -> {
            BandePlugin.getAPI().openGUI((Player) e.getWhoClicked(), "bande_info");
        });

        map.put("bande-statistikker", e -> {
        });

        map.put("bande-rival-item", e -> {
            BandePlugin.getAPI().openGUI((Player) e.getWhoClicked(), "bande_forhold_rival");
        });

        map.put("bande-ally-item", e -> {
            BandePlugin.getAPI().openGUI((Player) e.getWhoClicked(), "bande_forhold_ally");
        });

        map.put("bande-top-offikills-item", e -> {
        });

        map.put("bande-top-vagtkills-item", e -> {
        });

        map.put("bande-top-level-item", e -> {
        });

        map.put("bande-top-fangekills-item", e -> {
        });

        map.put("bande-begraensninger-item", e -> {
        });

        map.put("bande-member-promote-item", e -> {
        });

        map.put("bande-member-kick-item", e -> {
        });

        map.put("bande-member-demote-item", e -> {
        });

        map.put("bande-hus-item", (e) -> {
        });

        map.put("bande-omraade-item", e -> {
            String tag = NBTAPI.getTag("omraade-price", e.getCurrentItem());
            if (tag == null || tag == "") {
                return;
            }

            Integer p = Integer.parseInt(tag);
            String location = NBTAPI.getTag("omraade-type", e.getCurrentItem());
            if (p == null || location == null) {
                Logger.warn(
                        "Bande-item configured wrongly. Missing one or multiple required bande-omraade properties (change with /itembuilder).");
                return;
            }

            BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer((Player) e.getWhoClicked());
            if (bandePlayer.getBande().getBalance().compareTo(new BigDecimal(p)) < 1) {
                Message.sendReplaced((Player) e.getWhoClicked(), "general.no_money", null, p.toString());
                return;
            }

            if (bandePlayer.getBande().getAccessList().contains(location)) {
                Message.sendReplaced((Player) e.getWhoClicked(), "bande_omraade.already_bought", null, location);
                return;
            }

            bandePlayer.getBande().removeBalance(new BigDecimal(p));

            bandePlayer.getBande().addAccess(location);
            if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null
                    && Bukkit.getPluginManager().getPlugin("WorldGuard").isEnabled())
                for (UUID uuid : bandePlayer.getBande().getMemberIterable()) {
                    WorldGuardHook.addMember(location, uuid, e.getWhoClicked().getWorld());
                    Message.sendReplaced(uuid, "bande_omraade.new_access", null, location);
                }

            Utils.scheduleClose(e);
        });
    }

    public GuiAction<InventoryClickEvent> get(String type) {
        if (type == null || type == "") {
            return null;
        }

        if (map.get(type) == null) {
            Logger.warn("TypeManager: Requested type '" + type + "' doesn't exist.");
            return null;
        }

        return map.get(type);
    }

    public void addType(String type, GuiAction<InventoryClickEvent> e) {
        map.remove(type);
        map.put(type, e);
    }

    public Set<String> values() {
        return map.keySet();
    }
}
