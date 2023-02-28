package de.firecreeper82.lotm;

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

    public Pathway pathway;
    public UUID uuid;

    public double spirituality;
    public double maxSpirituality;

    public FastBoard board;

    public boolean beyonder;
    public boolean loosingControl;
    public boolean online;

    public Beyonder(UUID uuid, Pathway pathway) {
        this.pathway = pathway;
        this.uuid = uuid;

        pathway.setBeyonder(this);
        if(getPlayer() == null)
            return;
        start();

        beyonder = true;
        loosingControl = false;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if(!e.getPlayer().getUniqueId().equals(uuid))
            return;
        start();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if(!e.getPlayer().getUniqueId().equals(uuid))
            return;
        online = false;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if(e.getEntity() != getPlayer())
            return;
        for(ItemStack item : e.getDrops()) {
            if(pathway.getItems().returnItemsFromSequence(pathway.getSequence().getCurrentSequence()).contains(item)) {
                for(Entity entity : e.getEntity().getWorld().getNearbyEntities(e.getEntity().getLocation(), 50, 50, 50)) {
                    if(entity instanceof Item) {
                        entity.remove();
                    }
                }
            }
        }
    }

    private void updateBoard() {
        board.updateTitle(pathway.getStringColor() + getPlayer().getName());
        board.updateLines("", "§5Pathway", "- " + pathway.getStringColor() + pathway.getName(), "", "§5Sequence", "- " + pathway.getStringColor() + pathway.getSequence().getCurrentSequence() + ": " + Objects.requireNonNull(Pathway.getNamesForPathway(pathway.getNameNormalized())).get(pathway.getSequence().getCurrentSequence()), "", "§5Spirituality", "- " + pathway.getStringColor() + Math.round(spirituality) + "/" + Math.round(maxSpirituality));
    }

    public void start() {
        updateSpirituality();
        board = new FastBoard(getPlayer());
        board.updateTitle(pathway.getStringColor() + getPlayer().getName());
        board.updateLines("", "§5Pathway", "- " + pathway.getStringColor() + pathway.getName(), "", "§5Sequence", "- " + pathway.getStringColor() + pathway.getSequence().getCurrentSequence() + ": " + Objects.requireNonNull(Pathway.getNamesForPathway(pathway.getNameNormalized())).get(pathway.getSequence().getCurrentSequence()), "", "§5Spirituality", "- " + pathway.getStringColor() + Math.round(spirituality) + "/" + Math.round(maxSpirituality));

        pathway.initItems();
        online = true;


        //constant loop
        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                if(!beyonder || !online || getPlayer() == null) {
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

                pathway.getSequence().onHold(p.getInventory().getItemInMainHand());

                //passive effects
                if(pathway.getSequence().getSequenceEffects().containsKey(pathway.getSequence().getCurrentSequence())) {
                    for(PotionEffect effect : pathway.getSequence().getSequenceEffects().get(pathway.getSequence().getCurrentSequence())) {
                        p.addPotionEffect(effect);
                    }
                }
                else {
                    for(int i = pathway.getSequence().currentSequence; i < 9; i++) {
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
                    for(int i = pathway.getSequence().currentSequence; i < 9; i++) {
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
        }.runTaskTimer(Plugin.instance, 0, 5);
    }

    public void updateSpirituality() {
        if(pathway.getSequence().currentSequence > 8)
            spirituality = (int) Math.pow((float) (90 / pathway.getSequence().currentSequence), 2);
        else if(pathway.getSequence().currentSequence > 4)
            spirituality = (int) Math.pow((double) (90 / pathway.getSequence().currentSequence) * 2, 2);
        else if(pathway.getSequence().currentSequence < 5)
            spirituality = (int) Math.pow((float) (90 / pathway.getSequence().currentSequence), 3) ;
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

        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                if(random.nextInt(25) + 1 == 5 && getPlayer().getHealth() > 2)
                    getPlayer().damage(2);

                counter++;
                if(counter == timeOfLoosingControl * 20) {
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
        Plugin.instance.removeBeyonder(getUuid());
        board.delete();
        beyonder = false;
        pathway = null;
    }
}
