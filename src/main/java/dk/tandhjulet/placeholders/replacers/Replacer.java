package dk.tandhjulet.placeholders.replacers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.placeholders.BandePlaceholders;
import dk.tandhjulet.utils.Utils;

public class Replacer {
    private static final Pattern pattern;

    public static String apply(final String text, final Bande bande) {
        final Matcher matcher = Replacer.pattern.matcher(text);
        if (!matcher.find()) {
            return text;
        }

        final StringBuffer builder = new StringBuffer();
        do {
            final String parameters = matcher.group("parameters");
            final String requested = BandePlaceholders.onPlaceholderRequest(bande, parameters);
            matcher.appendReplacement(builder, (requested != null) ? requested : matcher.group(0));
        } while (matcher.find());

        return Utils.getColored(matcher.appendTail(builder).toString());
    }

    static {
        pattern = Pattern.compile("%(?<parameters>.*?)%");
    }
}
