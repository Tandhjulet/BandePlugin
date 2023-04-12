package dk.tandhjulet.placeholders;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.placeholders.replacers.Replacer;
import dk.tandhjulet.storage.Message;

public class BandePlaceholders {
    public static HashMap<String, Function<Bande, String>> customPlaceholders;

    static {
        customPlaceholders = new HashMap<>();
    }

    public static List<String> setPlaceholders(final Bande bande, final List<String> lines) {
        return lines.stream().map(l -> {
            if (bande == null) {
                return l;
            }
            if (l == null) {
                return "";
            }

            return Replacer.apply(l, bande);
        }).collect(Collectors.toList());
    }

    public static void registerPlaceholder(String identifier, Function<Bande, String> consumer) {
        customPlaceholders.put(identifier, consumer);
    }

    public static void registerStaticPlaceholder(String identifier, String a) {
        customPlaceholders.put(identifier, b -> {
            return a;
        });
    }

    public static String onPlaceholderRequest(final Bande bande, String identifier) {
        if (identifier.equalsIgnoreCase("name")) {
            return bande.getName();
        } else if (identifier.equalsIgnoreCase("level")) {
            return bande.getLevel().toString();
        } else if (identifier.equalsIgnoreCase("bank")) {
            return bande.getBalance().toString();

        } else if (identifier.startsWith("amount")) {
            if (identifier.equalsIgnoreCase("amount_member")) {
                return String.valueOf(Iterables.size(bande.getMemberIterable()));
            } else if (identifier.equalsIgnoreCase("amount_allies")) {
                return String.valueOf(bande.getAllianceAmount());
            } else if (identifier.equalsIgnoreCase("amount_rivals")) {
                return String.valueOf(bande.getRivalAmount());
            }
        } else if (identifier.startsWith("max")) {
            if (identifier.equalsIgnoreCase("max_member")) {
                return String.valueOf(bande.getMaxMembers());
            } else if (identifier.equalsIgnoreCase("max_allies")) {
                return String.valueOf(bande.getMaxAlliances());
            } else if (identifier.equalsIgnoreCase("max_rivals")) {
                return String.valueOf(bande.getMaxRivals());
            }
        } else if (identifier.equalsIgnoreCase("allyskade")) {
            return String.valueOf(100 - bande.getAllySkade());
        } else if (identifier.equalsIgnoreCase("bandeskade")) {
            return String.valueOf(100 - bande.getBandeSkade());

        } else if (identifier.equalsIgnoreCase("bande_allyskadepris")) {
            return BandePlugin.getConfiguration().getAllyDamagePrice().toString();
        } else if (identifier.equalsIgnoreCase("bande_bandeskadepris")) {
            return BandePlugin.getConfiguration().getBandeDamagePrice().toString();

        } else if (identifier.equalsIgnoreCase("bande_extraallypris")) {
            return BandePlugin.getConfiguration().getExtraAllyPrice().toString();
        } else if (identifier.equalsIgnoreCase("bande_extrarivalpris")) {
            return BandePlugin.getConfiguration().getExtraRivalPrice().toString();
        } else if (identifier.equalsIgnoreCase("bande_extramedlempris")) {
            return BandePlugin.getConfiguration().getExtraMemberPrice().toString();

        } else if (identifier.equalsIgnoreCase("hunger_buff_price")) {
            return BandePlugin.getConfiguration().getHungerBuffPrice().toString();
        } else if (identifier.equalsIgnoreCase("strength_buff_price")) {
            return BandePlugin.getConfiguration().getStrengthBuffPrice().toString();
        } else if (identifier.equalsIgnoreCase("speed_buff_price")) {
            return BandePlugin.getConfiguration().getSpeedBuffPrice().toString();

        } else if (identifier.equalsIgnoreCase("fangekills")) {
            return BandePlugin.getTop().getFangeKill(bande.getName()).toString();
        } else if (identifier.equalsIgnoreCase("officerkills")) {
            return BandePlugin.getTop().getOffiKills(bande.getName()).toString();
        } else if (identifier.equalsIgnoreCase("vagtkills")) {
            return BandePlugin.getTop().getVagtKills(bande.getName()).toString();
        } else if (identifier.equalsIgnoreCase("bandehus")) {
            if (bande.hasBandeHus()) {
                return Message.get("bande_huse.symbol_yes")[0];
            }
            return Message.get("bande_huse.symbol_no")[0];
        } else if (identifier.equalsIgnoreCase("bandehuse_sold")) {
            return String.valueOf(BandePlugin.getHouseManager().getRegions().size() - BandePlugin.getHouseManager()
                    .getAvailableRegions(BandePlugin.getConfiguration().getMainWorld()).size());
        } else if (identifier.equalsIgnoreCase("bandehuse_total")) {
            return String.valueOf(BandePlugin.getHouseManager().getRegions().size());
        } else if (customPlaceholders.containsKey(identifier)) {
            return customPlaceholders.get(identifier).apply(bande);
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
