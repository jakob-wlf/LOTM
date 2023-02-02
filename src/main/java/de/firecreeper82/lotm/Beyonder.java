package de.firecreeper82.lotm;

import de.firecreeper82.pathways.Pathway;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.A;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Beyonder {

    public Pathway pathway;
    public UUID uuid;

    public int spirituality;
    public int maxSpirituality;

    public FastBoard board;

    public Beyonder(UUID uuid, Pathway pathway) {
        this.pathway = pathway;
        this.uuid = uuid;

        pathway.setBeyonder(this);

        //set up spirituality and scoreboard after 4 ticks delay (So the other things have time to initialize)
        new BukkitRunnable() {
            @Override
            public void run() {
                updateSpirituality();
                board = new FastBoard(getPlayer());
                board.updateTitle(pathway.getStringColor() + getPlayer().getName());
                board.updateLines("", "§5Pathway", "- " + pathway.getStringColor() + pathway.getName(), "", "§5Sequence", "- " + pathway.getStringColor() + pathway.getSequence().getCurrentSequence(), "", "§5Spirituality", "- " + pathway.getStringColor() + spirituality + "/" + maxSpirituality);
            }
        }.runTaskLater(Plugin.instance, 4);


        //constant loop
        AtomicInteger counter = new AtomicInteger();
        new BukkitRunnable() {
            @Override
            public void run() {
                if(getPlayer() == null) {
                    return;
                }
                //scoreboard
                counter.incrementAndGet();
                updateBoard();

                //spirituality handling
                if (spirituality < maxSpirituality && counter.get() >= 8) {
                    counter.set(0);
                    spirituality++;
                }

                Player p = getPlayer();

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
            }
        }.runTaskTimer(Plugin.instance, 5, 5);
    }

    private void updateBoard() {
        board.updateTitle(pathway.getStringColor() + getPlayer().getName());
        board.updateLines("", "§5Pathway", "- " + pathway.getStringColor() + pathway.getName(), "", "§5Sequence", "- " + pathway.getStringColor() + pathway.getSequence().getCurrentSequence(), "", "§5Spirituality", "- " + pathway.getStringColor() + spirituality + "/" + maxSpirituality);
    }

    public void updateSpirituality() {
        if(pathway.getSequence().currentSequence > 8)
            spirituality = (int) Math.pow((double) (90 / pathway.getSequence().currentSequence), 2);
        else if(pathway.getSequence().currentSequence > 4)
            spirituality = (int) Math.pow((double) (90 / pathway.getSequence().currentSequence) * 2, 2);
        else if(pathway.getSequence().currentSequence < 5)
            spirituality = (int) Math.pow((double) (90 / pathway.getSequence().currentSequence), 3) ;
        maxSpirituality = spirituality;
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

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getSpirituality() {
        return spirituality;
    }

    public void setSpirituality(int spirituality) {
        this.spirituality = spirituality;
    }
}
