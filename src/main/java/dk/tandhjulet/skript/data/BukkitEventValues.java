package dk.tandhjulet.skript.data;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import dk.tandhjulet.bande.Bande;
import dk.tandhjulet.bande.BandePlayer;
import dk.tandhjulet.events.BandeBankUpdateEvent;
import dk.tandhjulet.events.BandeCreateEvent;
import dk.tandhjulet.events.BandeDemoteEvent;
import dk.tandhjulet.events.BandeDepositEvent;
import dk.tandhjulet.events.BandeForladEvent;
import dk.tandhjulet.events.BandeGUICloseEvent;
import dk.tandhjulet.events.BandeGUIOpenEvent;
import dk.tandhjulet.events.BandeJoinEvent;
import dk.tandhjulet.events.BandeKickEvent;
import dk.tandhjulet.events.BandeNewAccessEvent;
import dk.tandhjulet.events.BandePreDisbandEvent;
import dk.tandhjulet.events.BandePromoteEvent;
import dk.tandhjulet.events.BandeRivalEvent;

public class BukkitEventValues {
    static {
        EventValues.registerEventValue(BandeCreateEvent.class, Bande.class, new Getter<Bande, BandeCreateEvent>() {

            @Override
            @Nullable
            public Bande get(BandeCreateEvent arg0) {
                return arg0.getBande();
            }

        }, 0);

        EventValues.registerEventValue(BandeCreateEvent.class, Player.class, new Getter<Player, BandeCreateEvent>() {

            @Override
            @Nullable
            public Player get(BandeCreateEvent arg0) {
                return arg0.getPlayer();
            }

        }, 0);

        EventValues.registerEventValue(BandeDemoteEvent.class, BandePlayer.class,
                new Getter<BandePlayer, BandeDemoteEvent>() {

                    @Override
                    @Nullable
                    public BandePlayer get(BandeDemoteEvent arg0) {
                        return arg0.getDemoted();
                    }

                }, 0);

        EventValues.registerEventValue(BandeDemoteEvent.class, Player.class, new Getter<Player, BandeDemoteEvent>() {

            @Override
            @Nullable
            public Player get(BandeDemoteEvent arg0) {
                return arg0.getDemoted().getBase();
            }

        }, 0);

        EventValues.registerEventValue(BandeDemoteEvent.class, Bande.class, new Getter<Bande, BandeDemoteEvent>() {

            @Override
            @Nullable
            public Bande get(BandeDemoteEvent arg0) {
                return arg0.getBande();
            }

        }, 0);

        EventValues.registerEventValue(BandeDepositEvent.class, Bande.class, new Getter<Bande, BandeDepositEvent>() {

            @Override
            @Nullable
            public Bande get(BandeDepositEvent arg0) {
                return arg0.getBande();
            }

        }, 0);

        EventValues.registerEventValue(BandeDepositEvent.class, Double.class, new Getter<Double, BandeDepositEvent>() {

            @Override
            @Nullable
            public Double get(BandeDepositEvent arg0) {
                return arg0.getAmount().doubleValue();
            }

        }, 0);

        EventValues.registerEventValue(BandeForladEvent.class, Player.class, new Getter<Player, BandeForladEvent>() {

            @Override
            @Nullable
            public Player get(BandeForladEvent arg0) {
                return arg0.getPlayer();
            }

        }, 0);

        EventValues.registerEventValue(BandeForladEvent.class, Bande.class, new Getter<Bande, BandeForladEvent>() {

            @Override
            @Nullable
            public Bande get(BandeForladEvent arg0) {
                return arg0.getBande();
            }

        }, 0);

        EventValues.registerEventValue(BandeJoinEvent.class, Bande.class, new Getter<Bande, BandeJoinEvent>() {

            @Override
            @Nullable
            public Bande get(BandeJoinEvent arg0) {
                return arg0.getBande();
            }

        }, 0);

        EventValues.registerEventValue(BandeJoinEvent.class, Player.class, new Getter<Player, BandeJoinEvent>() {

            @Override
            @Nullable
            public Player get(BandeJoinEvent arg0) {
                return arg0.getPlayer();
            }

        }, 0);

        EventValues.registerEventValue(BandeKickEvent.class, Bande.class, new Getter<Bande, BandeKickEvent>() {

            @Override
            @Nullable
            public Bande get(BandeKickEvent arg0) {
                return arg0.getBande();
            }

        }, 0);

        EventValues.registerEventValue(BandeKickEvent.class, BandePlayer.class,
                new Getter<BandePlayer, BandeKickEvent>() {

                    @Override
                    @Nullable
                    public BandePlayer get(BandeKickEvent arg0) {
                        return arg0.getKicked();
                    }

                }, 0);

        EventValues.registerEventValue(BandeNewAccessEvent.class, Bande.class,
                new Getter<Bande, BandeNewAccessEvent>() {

                    @Override
                    @Nullable
                    public Bande get(BandeNewAccessEvent arg0) {
                        return arg0.getBande();
                    }

                }, 0);

        EventValues.registerEventValue(BandeNewAccessEvent.class, String.class,
                new Getter<String, BandeNewAccessEvent>() {

                    @Override
                    @Nullable
                    public String get(BandeNewAccessEvent arg0) {
                        return arg0.getRegionId();
                    }

                }, 0);

        EventValues.registerEventValue(BandePreDisbandEvent.class, Bande.class,
                new Getter<Bande, BandePreDisbandEvent>() {

                    @Override
                    @Nullable
                    public Bande get(BandePreDisbandEvent arg0) {
                        return arg0.getBande();
                    }

                }, 0);

        EventValues.registerEventValue(BandePreDisbandEvent.class, Player.class,
                new Getter<Player, BandePreDisbandEvent>() {

                    @Override
                    @Nullable
                    public Player get(BandePreDisbandEvent arg0) {
                        return arg0.getPlayer();
                    }

                }, 0);

        EventValues.registerEventValue(BandePromoteEvent.class, BandePlayer.class,
                new Getter<BandePlayer, BandePromoteEvent>() {

                    @Override
                    @Nullable
                    public BandePlayer get(BandePromoteEvent arg0) {
                        return arg0.getPromoted();
                    }

                }, 0);

        EventValues.registerEventValue(BandePromoteEvent.class, Bande.class,
                new Getter<Bande, BandePromoteEvent>() {

                    @Override
                    @Nullable
                    public Bande get(BandePromoteEvent arg0) {
                        return arg0.getBande();
                    }

                }, 0);

        EventValues.registerEventValue(BandeRivalEvent.class, Bande.class,
                new Getter<Bande, BandeRivalEvent>() {

                    @Override
                    @Nullable
                    public Bande get(BandeRivalEvent arg0) {
                        return arg0.getAggressor();
                    }

                }, 0);

        EventValues.registerEventValue(BandeRivalEvent.class, String.class,
                new Getter<String, BandeRivalEvent>() {

                    @Override
                    @Nullable
                    public String get(BandeRivalEvent arg0) {
                        return arg0.getRival().getName();
                    }

                }, 0);

        EventValues.registerEventValue(BandeBankUpdateEvent.class, Number.class,
                new Getter<Number, BandeBankUpdateEvent>() {

                    @Override
                    @Nullable
                    public Number get(BandeBankUpdateEvent arg0) {
                        return arg0.getAmount().doubleValue();
                    }

                }, 0);

        EventValues.registerEventValue(BandeBankUpdateEvent.class, Bande.class,
                new Getter<Bande, BandeBankUpdateEvent>() {

                    @Override
                    @Nullable
                    public Bande get(BandeBankUpdateEvent arg0) {
                        return arg0.getBande();
                    }

                }, 0);

        EventValues.registerEventValue(BandeGUIOpenEvent.class, Player.class,
                new Getter<Player, BandeGUIOpenEvent>() {

                    @Override
                    @Nullable
                    public Player get(BandeGUIOpenEvent arg0) {
                        return (Player) arg0.getEvent().getPlayer();
                    }

                }, 0);

        EventValues.registerEventValue(BandeGUIOpenEvent.class, Inventory.class,
                new Getter<Inventory, BandeGUIOpenEvent>() {

                    @Override
                    @Nullable
                    public Inventory get(BandeGUIOpenEvent arg0) {
                        return (Inventory) arg0.getEvent().getInventory();
                    }

                }, 0);

        EventValues.registerEventValue(BandeGUICloseEvent.class, Player.class,
                new Getter<Player, BandeGUICloseEvent>() {

                    @Override
                    @Nullable
                    public Player get(BandeGUICloseEvent arg0) {
                        return (Player) arg0.getEvent().getPlayer();
                    }

                }, 0);

        EventValues.registerEventValue(BandeGUICloseEvent.class, Inventory.class,
                new Getter<Inventory, BandeGUICloseEvent>() {

                    @Override
                    @Nullable
                    public Inventory get(BandeGUICloseEvent arg0) {
                        return (Inventory) arg0.getEvent().getInventory();
                    }

                }, 0);
    }

}
