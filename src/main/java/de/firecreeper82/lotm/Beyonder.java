package de.firecreeper82.lotm;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Potion;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class Beyonder implements Listener {

    private Pathway pathway;
    protected UUID uuid;

    protected double spirituality;
    protected double maxSpirituality;

    private FastBoard board;

    private boolean beyonder;
    private boolean loosingControl;
    public boolean online;

    public Beyonder(UUID uuid, Pathway pathway) {
        this.pathway = pathway;
        this.uuid = uuid;

        pathway.setBeyonder(this);
        if(getPlayer() == null)
            return;

        pathway.initItems();
        start();

        beyonder = true;
        loosingControl = false;
    }

    @EventHandler
    //Restarts everything when Beyonder rejoins
    public void onJoin(PlayerJoinEvent e) {
        if(!e.getPlayer().getUniqueId().equals(uuid))
            return;
        if(!beyonder)
            return;
        start();
    }

    @EventHandler
    //Stops everything when Beyonder leaves
    public void onLeave(PlayerQuitEvent e) {
        if(!e.getPlayer().getUniqueId().equals(uuid))
            return;
        if(!beyonder)
            return;
        online = false;
    }

    @EventHandler
    //Removes Items on Death
    public void onDeath(PlayerDeathEvent e) {
        if(e.getEntity() != getPlayer())
            return;
        Player p = (Player) e;
        for(Entity entity : p.getNearbyEntities(20, 20, 20)) {
            if(entity instanceof Item && pathway.getItems().returnItemsFromSequence(pathway.getSequence().getCurrentSequence()).contains(((Item) entity).getItemStack())) {
                entity.remove();
            }
        }
    }

    private void updateBoard() {
        board.updateTitle(pathway.getStringColor() + getPlayer().getName());
        board.updateLines("", "§5Pathway", "- " + pathway.getStringColor() + pathway.getName(), "", "§5Sequence", "- " + pathway.getStringColor() + pathway.getSequence().getCurrentSequence() + ": " + Objects.requireNonNull(Pathway.getNamesForPathway(pathway.getNameNormalized())).get(pathway.getSequence().getCurrentSequence()), "", "§5Spirituality", "- " + pathway.getStringColor() + Math.round(spirituality) + "/" + Math.round(maxSpirituality));
    }

    //Gets called everytime the Player rejoins or the Beyonder is newly initialised
    public void start() {
        updateSpirituality();
        board = new FastBoard(getPlayer());
        board.updateTitle(pathway.getStringColor() + getPlayer().getName());
        board.updateLines("", "§5Pathway", "- " + pathway.getStringColor() + pathway.getName(), "", "§5Sequence", "- " + pathway.getStringColor() + pathway.getSequence().getCurrentSequence() + ": " + Objects.requireNonNull(Pathway.getNamesForPathway(pathway.getNameNormalized())).get(pathway.getSequence().getCurrentSequence()), "", "§5Spirituality", "- " + pathway.getStringColor() + Math.round(spirituality) + "/" + Math.round(maxSpirituality));

        online = true;

        //onHold
        new BukkitRunnable() {
            @Override
            public void run() {
                //Cancel and return if player, sequence is null or player is not online
                if(!beyonder || !online || getPlayer() == null || pathway.getSequence() == null) {
                    cancel();
                    return;
                }

                if(loosingControl)
                    return;

                Player p = getPlayer();

                //Call onHold() function in Sequence
                pathway.getSequence().onHold(p.getInventory().getItemInMainHand());
            }
        }.runTaskTimer(Plugin.instance, 0, 3);

        //constant loop
        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                //Cancel and return if player, sequence is null or player is not online
                if(!beyonder || !online || getPlayer() == null || pathway.getSequence() == null) {
                    cancel();
                    return;
                }

                //scoreboard
                counter++;
                updateBoard();

                if(spirituality <= maxSpirituality / 100 && !loosingControl) {
                    looseControl(95, 10);
                }

                //spirituality handling
                if (spirituality < maxSpirituality && counter >= 8) {
                    counter = 0;
                    spirituality += (maxSpirituality / 200);
                    if(spirituality > maxSpirituality)
                        spirituality = maxSpirituality;
                }

                if(loosingControl)
                    return;

                Player p = getPlayer();

                //passive effects
                if(pathway.getSequence().getSequenceEffects().containsKey(pathway.getSequence().getCurrentSequence())) {
                    for(PotionEffect effect : pathway.getSequence().getSequenceEffects().get(pathway.getSequence().getCurrentSequence())) {
                        p.addPotionEffect(effect);
                    }
                }
                else {
                    for(int i = pathway.getSequence().getCurrentSequence(); i < 9; i++) {
                        if(pathway.getSequence().getSequenceEffects().containsKey(i)) {
                            for(PotionEffect effect : pathway.getSequence().getSequenceEffects().get(i)) {
                                p.addPotionEffect(effect);
                            }
                            break;
                        }
                    }
                }

                //passive resistances
                if(pathway.getSequence().getSequenceResistances().containsKey(pathway.getSequence().getCurrentSequence())) {
                    for(PotionEffectType effect : pathway.getSequence().getSequenceResistances().get(pathway.getSequence().getCurrentSequence())) {
                        for(PotionEffect potion : p.getActivePotionEffects()) {
                            if(potion.getType() == effect) {
                                p.removePotionEffect(effect);
                            }
                        }
                    }
                }
                else {
                    for(int i = pathway.getSequence().getCurrentSequence(); i < 9; i++) {
                        if(pathway.getSequence().getSequenceResistances().containsKey(i)) {
                            for(PotionEffectType effect : pathway.getSequence().getSequenceResistances().get(i)) {
                                if(p.getPotionEffect(effect) != null) {
                                    p.removePotionEffect(effect);
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 10);
    }

    public void updateSpirituality() {
        if(pathway.getSequence().getCurrentSequence() > 8)
            spirituality = (int) Math.pow((float) (90 / pathway.getSequence().getCurrentSequence()), 2);
        else if(pathway.getSequence().getCurrentSequence() > 4)
            spirituality = (int) Math.pow((double) (90 / pathway.getSequence().getCurrentSequence()) * 2, 2);
        else if(pathway.getSequence().getCurrentSequence() < 5)
            spirituality = (int) Math.pow((float) (90 / pathway.getSequence().getCurrentSequence()), 3) ;
        maxSpirituality = spirituality;
    }


    //lostControl: chance of surviving
    public void looseControl(int lostControl, int timeOfLoosingControl) {
        Random random = new Random();
        boolean survives = ((random.nextInt(100) + 1) <= lostControl);

        loosingControl = true;
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * timeOfLoosingControl, 3, false, false));
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * timeOfLoosingControl, 3, false, false));
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * timeOfLoosingControl, 3, false, false));

        //Damaging player
        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                if(random.nextInt(25) + 1 == 5 && getPlayer().getHealth() > 2)
                    getPlayer().damage(2);

                counter++;
                if(counter == timeOfLoosingControl * 20) {
                    //When not survives, summons a Warden
                    if(!survives) {
                        Entity rampager = Objects.requireNonNull(getPlayer().getLocation().getWorld()).spawnEntity(getPlayer().getLocation(), EntityType.WARDEN);
                        rampager.setGlowing(true);
                        rampager.setCustomNameVisible(true);
                        rampager.setCustomName(pathway.getStringColor() + getPlayer().getName());
                        getPlayer().setHealth(0);
                        loosingControl = false;
                        removeBeyonder();
                        cancel();
                        return;
                    }
                    loosingControl = false;
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    //Called from the PotionListener
    public void consumePotion(int sequence, Potion potion) {
        if(sequence >= pathway.getSequence().getCurrentSequence())
            return;

        if(!getPathway().getNameNormalized().equals(potion.name)) {
            looseControl(0, 10);
            return;
        }
        if(pathway == null) {
            getPlayer().sendMessage("§cYour advancement has failed! You can call yourself lucky to still be alive...");
            return;
        }
        switch(getPathway().getSequence().getCurrentSequence() - 1 - sequence) {
            case 0 -> pathway.getBeyonder().looseControl(93, 20);
            case 1 -> pathway.getBeyonder().looseControl(50, 20);
            case 2 -> pathway.getBeyonder().looseControl(30, 20);
            case 3, 4 -> pathway.getBeyonder().looseControl(20, 16);
            case 5 -> pathway.getBeyonder().looseControl(1, 16);
            default -> pathway.getBeyonder().looseControl(0, 10);
        }

        pathway.getSequence().setCurrentSequence(sequence);
        updateSpirituality();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public Pathway getPathway() {
        return pathway;
    }

    public void setPathway(Pathway pathway) {
        this.pathway = pathway;
    }

    public UUID getUuid() {
        return uuid;
    }

    public double getSpirituality() {
        return spirituality;
    }

    public void setSpirituality(double spirituality) {
        this.spirituality = spirituality;
    }

    public void removeBeyonder() {
        for(Ability a : pathway.getSequence().getAbilities()) {
            a.removeAbility();
        }
        Plugin.instance.removeBeyonder(getUuid());
        if(board != null)
            board.delete();
        beyonder = false;
        pathway.setSequence(null);
        pathway = null;
    }
}
