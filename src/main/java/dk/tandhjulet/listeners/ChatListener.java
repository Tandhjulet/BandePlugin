package dk.tandhjulet.listeners;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.commands.subcommands.Bank;
import dk.tandhjulet.enums.BandeRank;
import dk.tandhjulet.enums.ChatStatus;
import dk.tandhjulet.events.BandeCreateEvent;
import dk.tandhjulet.storage.Config;
import dk.tandhjulet.storage.Message;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

public class ChatListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        BandePlayer player = BandePlugin.getAPI().getPlayer(e.getPlayer());
        Config conf = BandePlugin.getConfiguration();
        if (player.getChat() == ChatStatus.BANDE_OPRET) {
            player.setChat(ChatStatus.NULL);
            e.setCancelled(true);

            if (BandePlugin.getAPI().getBande(e.getMessage()) != null) {
                Message.sendReplaced(e.getPlayer(), "bande_create.bande_exists", null, e.getMessage());
                return;
            }

            if (e.getMessage().equalsIgnoreCase("!cancel")) {
                Message.sendReplaced(e.getPlayer(), "bande_create.creation_cancelled", null);
                return;
            }

            if (player.hasBande()) {
                Message.sendReplaced(e.getPlayer(), "bande_create.in_bande", null, player.getBande().getName());
                return;
            }

            if (e.getMessage().split(" ").length > 1) {
                Message.sendReplaced(e.getPlayer(), "bande_create.invalidName", null);
                return;
            } else if (!StringUtils.isAlpha(e.getMessage())) {
                Message.sendReplaced(e.getPlayer(), "bande_create.containsIllegalChar", null);
                return;
            } else if ((conf.getMinLength().intValue() > e.getMessage().length())
                    || (e.getMessage().length() > conf.getMaxLength().intValue())) {
                Message.sendReplaced(e.getPlayer(), "bande_create.illegalLength", null, conf.getMinLength().toString(),
                        conf.getMaxLength().toString());
                return;
            }
            List<String> disallowedNames = BandePlugin.getConfiguration().getDisallowedNames();
            if (disallowedNames.contains(e.getMessage())) {
                Message.sendReplaced(e.getPlayer(), "bande_create.illegalName", null, e.getMessage());
                return;
            }

            Economy econ = BandePlugin.getEconomy();
            Integer cost = BandePlugin.getConfiguration().getCreatePrice();
            if (!econ.has(e.getPlayer(), cost)) {
                Message.sendReplaced(e.getPlayer(), "bande_create.not_enough_money", null, cost.toString());
                return;
            }
            econ.withdrawPlayer(e.getPlayer(), cost);

            Bande bande = BandePlugin.getAPI().createBande(e.getMessage(), e.getPlayer().getUniqueId());

            player.setBande(bande.getName(), BandeRank.EJER);

            BandePlugin.getAPI().discardCache(player);
            Message.sendReplaced(e.getPlayer(), "bande_create.bande_created", null, e.getMessage());

            Bukkit.getPluginManager().callEvent(new BandeCreateEvent(e.getPlayer(), bande));

            return;

        } else if (player.getChat() == ChatStatus.BANK_DESPOSIT) {

            player.setChat(ChatStatus.NULL);
            e.setCancelled(true);

            if (e.getMessage().equalsIgnoreCase("!cancel")) {
                Message.sendReplaced(e.getPlayer(), "bank.deposit_cancelled", null);
                return;
            }

            Bank.run(player, new String[] { "", e.getMessage() });

            return;
        }
        if (!BandePlugin.getChatEnabled())
            return;

        Chat chat = BandePlugin.getChat();
        String prefix = chat.getGroupPrefix(e.getPlayer().getWorld(), chat.getPrimaryGroup(e.getPlayer()));
        prefix = prefix == null ? "" : prefix;

        String suffix = chat.getGroupSuffix(e.getPlayer().getWorld(), chat.getPrimaryGroup(e.getPlayer()));
        suffix = suffix == null ? "" : suffix;

        if (player.hasBande()) {
            e.setCancelled(true);

            String stars = StringUtils.repeat("*", player.getBandeRank().getPriority() - 1);

            Bande b = player.getBande();
            String bande = b.getName();
            Integer level = b.getLevel();

            for (Player recipient : e.getRecipients()) {
                BandePlayer p = BandePlugin.getAPI().getPlayer(recipient);
                Bande pb = p.getBande();
                if (p.hasBande() && pb.getAlliances().contains(bande)) {
                    Message.sendReplaced(p, "chat.chat_format_ally", null, stars, bande, level.toString(), prefix,
                            e.getPlayer().getName(), suffix,
                            e.getMessage());

                    // recipient.sendMessage(Utils.getColored(format.replaceAll("\\{0\\}", "&a")));
                } else if (p.hasBande() && pb.getRivals().contains(bande)) {
                    Message.sendReplaced(p, "chat.chat_format_rivals", null, stars, bande, level.toString(), prefix,
                            e.getPlayer().getName(), suffix,
                            e.getMessage());

                    // recipient.sendMessage(Utils.getColored(format.replaceAll("\\{0\\}", "&c")));
                } else {
                    Message.sendReplaced(p, "chat.chat_format_normal", null, stars, bande, level.toString(), prefix,
                            e.getPlayer().getName(), suffix,
                            e.getMessage());

                    // recipient.sendMessage(Utils.getColored(format.replaceAll("\\{0\\}", "&7")));
                }
            }
        } else {
            e.setCancelled(true);
            Message.sendReplaced(Bukkit.getOnlinePlayers().toArray(new Player[0]), "chat.chat_format_no_bande", null,
                    prefix, e.getPlayer().getName(), suffix, e.getMessage());
        }
    }
}
