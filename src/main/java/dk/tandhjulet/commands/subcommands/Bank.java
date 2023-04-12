package dk.tandhjulet.commands.subcommands;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.events.BandeDepositEvent;
import dk.tandhjulet.storage.Message;
import net.milkbowl.vault.economy.Economy;

public class Bank {
    public static void run(BandePlayer sender, String[] args) {
        if (args.length >= 2) {
            String amount = args[1];
            if (!StringUtils.isNumeric(amount)) {
                Message.sendReplaced(sender, "bank.not_number", null);
                return;
            }

            Integer parsedAmount = Integer.valueOf(amount);
            Economy econ = BandePlugin.getEconomy();
            if (!econ.has(sender.getBase(), parsedAmount)) {
                Message.sendReplaced(sender, "bank.cannot_afford", null, parsedAmount.toString());
                return;
            }

            econ.withdrawPlayer(sender.getBase(), parsedAmount);

            BigDecimal bd = new BigDecimal(parsedAmount);
            sender.getBande().addToBalance(bd);

            Message.sendReplaced(sender, "bank.success", null, parsedAmount.toString());

            Bukkit.getPluginManager().callEvent(new BandeDepositEvent(sender.getBase(), sender.getBande(), bd));

        } else {
            Message.sendReplaced(sender, "bank.incorrect_syntax", null);
        }
    }
}
