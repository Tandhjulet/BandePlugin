package dk.tandhjulet.level;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.level.operators.Operators;
import dk.tandhjulet.placeholders.BandePlaceholders;
import dk.tandhjulet.utils.Logger;
import dk.tandhjulet.utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class LevelRequirement {
    String met;
    String notMet;
    String cond;

    public LevelRequirement(ConfigurationSection section) {
        met = Utils.getColored(section.getString("ConditionMet"));
        notMet = Utils.getColored(section.getString("ConditionNotMet"));

        cond = Utils.getColored(section.getString("PlaceHolderCondition"));
    }

    public String apply(OfflinePlayer player, Bande bande) {
        List<String> lines = new LinkedList<String>() {
            {
                add(cond);
            }
        };

        List<String> tempLines;

        tempLines = BandePlaceholders.setPlaceholders(bande, lines);
        tempLines = PlaceholderAPI.setPlaceholders(player, tempLines);

        if (tempLines.equals(lines)) {
            Logger.warn("Couldn't parse level requirement " + cond + ". Placeholder (presumably) doesn't exist.");
            return notMet;
        }

        lines = tempLines;

        Expression expr = new ExpressionBuilder(lines.get(0))
                .operator(Operators.get())
                .build();

        if (expr.evaluate() == 0d) { // false
            return notMet;
        }

        return met;
    }

    public boolean get(OfflinePlayer player, Bande bande) {
        List<String> lines = new LinkedList<String>() {
            {
                add(cond);
            }
        };

        List<String> tempLines;

        tempLines = BandePlaceholders.setPlaceholders(bande, lines);
        tempLines = PlaceholderAPI.setPlaceholders(player, tempLines);

        if (tempLines == lines) {
            Logger.warn("Couldn't parse level requirement " + cond + ". Placeholder (presumably) doesn't exist.");
            return false;
        }

        lines = tempLines;

        Expression expr = new ExpressionBuilder(lines.get(0))
                .operator(Operators.get())
                .build();

        return expr.evaluate() != 0d;
    }
}
