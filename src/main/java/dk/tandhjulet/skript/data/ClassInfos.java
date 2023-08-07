package dk.tandhjulet.skript.data;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.EnumSerializer;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.EnumUtils;
import ch.njol.yggdrasil.Fields;
import dk.tandhjulet.BandePlugin;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.enums.BandeRank;

public class ClassInfos {

    public static final Pattern UUID_PATTERN = Pattern.compile("(?i)[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}");

    static {
        Classes.registerClass(new ClassInfo<>(Bande.class, "bande")
                .user("bande?")
                .name("bande")
                .description("Represents a Bande made from the Bande-Plugin")
                .defaultExpression(new EventValueExpression<>(Bande.class))
                .parser(new Parser<Bande>() {
                    @Override
                    public boolean canParse(ParseContext context) {
                        return true;
                    }

                    @Override
                    @Nullable
                    public Bande parse(String input, ParseContext context) {
                        return BandePlugin.getAPI().getBande(input);
                    }

                    @Override
                    public String toString(Bande arg0, int arg1) {
                        return toVariableNameString(arg0);
                    }

                    @Override
                    public String toVariableNameString(Bande arg0) {
                        return arg0.getName();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return "([a-zA-Z]+)";
                    }

                })
                .serializer(new Serializer<Bande>() {

                    @Override
                    protected boolean canBeInstantiated() {
                        return false;
                    }

                    @Override
                    public Bande deserialize(Fields fields) throws StreamCorruptedException {
                        return BandePlugin.getAPI().getBande(fields.getAndRemovePrimitive("bandeId", String.class));
                    }

                    @Override
                    public void deserialize(Bande arg0, Fields arg1)
                            throws StreamCorruptedException, NotSerializableException {
                        assert false;
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return true;
                    }

                    @Override
                    public Fields serialize(Bande arg0) throws NotSerializableException {
                        Fields fields = new Fields();
                        fields.putPrimitive("bandeId", arg0.getName());
                        return fields;
                    }
                }));

        Classes.registerClass(new ClassInfo<>(BandePlayer.class, "bandeplayer")
                .user("bandeplayers?")
                .name("Bandeplayer")
                .description("Represents a Bande Player made from the Bande-Plugin")
                .defaultExpression(new EventValueExpression<>(BandePlayer.class))
                .after("player", "offlineplayer")
                .parser(new Parser<BandePlayer>() {
                    @Override
                    public boolean canParse(ParseContext context) {
                        return true;
                    }

                    @Override
                    @Nullable
                    public BandePlayer parse(String input, ParseContext context) {
                        if (input.isEmpty()) {
                            return null;
                        }

                        if (UUID_PATTERN.matcher(input).matches()) {
                            return BandePlugin.getAPI().getPlayer(UUID.fromString(input));
                        }
                        List<Player> ps = Bukkit.matchPlayer(input);
                        if (ps.size() == 1) {
                            return BandePlugin.getAPI().getPlayer(ps.get(0));
                        }

                        return null;
                    }

                    @Override
                    public String toString(BandePlayer arg0, int arg1) {
                        return toVariableNameString(arg0);
                    }

                    @Override
                    public String toVariableNameString(BandePlayer arg0) {
                        return arg0.getBase().getName();
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return "([a-zA-Z]+)";
                    }

                })
                .serializer(new Serializer<BandePlayer>() {

                    @Override
                    protected boolean canBeInstantiated() {
                        return false;
                    }

                    @Override
                    public BandePlayer deserialize(Fields fields) throws StreamCorruptedException {
                        return BandePlugin.getAPI().getPlayer(fields.getAndRemovePrimitive("playerUUID", UUID.class));
                    }

                    @Override
                    public void deserialize(BandePlayer arg0, Fields arg1)
                            throws StreamCorruptedException, NotSerializableException {
                        assert false;
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return true;
                    }

                    @Override
                    public Fields serialize(BandePlayer arg0) throws NotSerializableException {
                        Fields fields = new Fields();
                        fields.putPrimitive("playerUUID", arg0.getBase().getUniqueId());
                        return fields;
                    }
                }));

        EnumUtils<BandeRank> ranks = new EnumUtils<>(BandeRank.class, "banderank");
        Classes.registerClass(new ClassInfo<>(BandeRank.class, "banderank")
                .user("banderank?")
                .name("BandeRank")
                .usage(ranks.getAllNames())
                .defaultExpression(new EventValueExpression<>(BandeRank.class))
                .parser(new Parser<BandeRank>() {

                    @Override
                    @Nullable
                    public BandeRank parse(String input, ParseContext context) {
                        return ranks.parse(input);
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return true;
                    }

                    @Override
                    public String toString(BandeRank arg0, int arg1) {
                        return ranks.toString(arg0, 0);
                    }

                    @Override
                    public String toVariableNameString(BandeRank arg0) {
                        return arg0.name().toLowerCase(Locale.ENGLISH);
                    }

                    @Override
                    public String getVariableNamePattern() {
                        return "banderank\\..*";
                    }

                }).serializer(new EnumSerializer<>(BandeRank.class)));
    }

}
