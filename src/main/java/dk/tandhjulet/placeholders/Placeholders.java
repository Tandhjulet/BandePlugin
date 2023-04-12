package dk.tandhjulet.placeholders;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Placeholders extends PlaceholderExpansion {

    private final BandePlugin plugin;

    public Placeholders(BandePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "player";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    public String onPlaceholderRequest(final Player player, String identifier) {
        final BandePlayer bandePlayer = BandePlugin.getAPI().getPlayer(player);
        final Bande bande = bandePlayer.getBande();

        if (identifier.startsWith("bande_invites")) {
            return String.valueOf(bandePlayer.getInvitations().size());
        } else if (identifier.startsWith("bande_create_price")) {
            return BandePlugin.getConfiguration().getCreatePrice().toString();
        }

        if (bande != null) {
            if (identifier.startsWith("bande_name")) {
                return bande.getName();
            } else if (identifier.startsWith("bande_bank")) {
                return bande.getBalance().toString();
            } else if (identifier.startsWith("bande_level")) {
                return bande.getLevel().toString();
            } else if (identifier.startsWith("bande_rank")) {
                return bandePlayer.getBandeRank().toString();
            }
        } else if (identifier.startsWith("bande_name")) {
            return BandePlugin.getConfiguration().getNoBande();
        }
        return null; // Placeholder is unknown by the Expansion
    }
}
