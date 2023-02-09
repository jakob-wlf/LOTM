package de.firecreeper82.lotm;

import de.firecreeper82.pathways.Pathway;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Beyonder implements Listener {

    public Pathway pathway;
    public UUID uuid;

    public double spirituality;
    public double maxSpirituality;

    public FastBoard board;

    public boolean beyonder;

    public Beyonder(UUID uuid, Pathway pathway) {
        this.pathway = pathway;
        this.uuid = uuid;

        pathway.setBeyonder(this);
        start();

        beyonder = true;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Bukkit.broadcastMessage(uuid + " -- " + e.getPlayer().getUniqueId());
        if(!e.getPlayer().getUniqueId().equals(uuid))
            return;
        Bukkit.broadcastMessage("test");
        start();
    }

    private void updateBoard() {
        board.updateTitle(pathway.getStringColor() + getPlayer().getName());
        board.updateLines("", "§5Pathway", "- " + pathway.getStringColor() + pathway.getName(), "", "§5Sequence", "- " + pathway.getStringColor() + pathway.getSequence().getCurrentSequence(), "", "§5Spirituality", "- " + pathway.getStringColor() + Math.round(spirituality) + "/" + Math.round(maxSpirituality));
    }

    public void start() {
        updateSpirituality();
        board = new FastBoard(getPlayer());
        board.updateTitle(pathway.getStringColor() + getPlayer().getName());
        board.updateLines("", "§5Pathway", "- " + pathway.getStringColor() + pathway.getName(), "", "§5Sequence", "- " + pathway.getStringColor() + pathway.getSequence().getCurrentSequence(), "", "§5Spirituality", "- " + pathway.getStringColor() + Math.round(spirituality) + "/" + Math.round(maxSpirituality));

        pathway.initItems();


        //constant loop
        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                if(!beyonder) {
                    cancel();
                    return;
                }

                if(getPlayer() == null) {
                    cancel();
                    return;
                }
                //scoreboard
                counter++;
                updateBoard();

                //spirituality handling
                if (spirituality < maxSpirituality && counter >= 8) {
                    counter = 0;
                    spirituality += (maxSpirituality / 200);
                    if(spirituality > maxSpirituality)
                        spirituality = maxSpirituality;
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

    /**
     * lostControl = 10: Death
     * lostControl = 9: 99% Chance of Death
     * lostControl = 8: 80% Chance of Death
     * lostControl = 7: 65% Chance of Death
     * lostControl = 6: 45% Chance of Death
     * lostControl = 5: 20% Chance of Death
     * lostControl = 4: 10% Chance of Death
     * lostControl = 3: 1% Chance of Death
     * lostControl = 2: Strong madness
     * lostControl = 1: Not so strong madness
     * lostControl = 0: Almost no signs of loosing control
     */
    public void looseControl(int lostControl) {
        //temporary so intellij shuts up abt not used variable
        getPlayer().sendMessage("Loosing control");
        getPlayer().setHealth(lostControl);
        getPlayer().setHealth(0);
        removeBeyonder();
    }

    public void consumePotion(int sequence) {
        if(sequence >= pathway.getSequence().getCurrentSequence())
            return;
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
        Plugin.beyonders.remove(getUuid());
        board.delete();
        beyonder = false;
        pathway = null;
    }
}
