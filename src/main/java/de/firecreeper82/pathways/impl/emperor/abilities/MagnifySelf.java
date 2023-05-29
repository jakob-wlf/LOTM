package de.firecreeper82.pathways.impl.emperor.abilities;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Recordable;
import de.firecreeper82.pathways.impl.emperor.EmperorItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class MagnifySelf extends Recordable {
    public boolean magnifyUse;
    public MagnifySelf(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void leftClick() {

        if (pathway.getSequence().getCurrentSequence() <= 4) {
            if (magnifyUse) {
                magnifyUse = false;
                p.sendMessage("§fSet your magnify mode to speed !");
            } else {
                magnifyUse = true;
                p.sendMessage("§fSet your magnify mode to jump !");
            }
        }
    }

    @Override
    public void useAbility(Player p, double multiplier, Beyonder beyonder, boolean recorded) {
        if (!recorded) pathway.getSequence().getUsesAbilities()[identifier - 1] = true;
        destroy(beyonder, recorded);
        //set magnified to 1 if 0
        if (!pathway.getBeyonder().damageMagnified) {
            pathway.getBeyonder().damageMagnified = true;
        } else {

            if (magnifyUse) {
                new BukkitRunnable() {
                    int timer = 30;

                    @Override
                    public void run() {
                        if (pathway.getBeyonder().getSpirituality() >= 200 && timer != 0) {
                            p.removePotionEffect(PotionEffectType.SPEED);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 0, 10, false, false, false));
                            pathway.getBeyonder().setSpirituality(pathway.getBeyonder().getSpirituality() - 200);
                        } else {
                            cancel();
                        }
                        timer--;
                        if (timer == 0) {
                            cancel();
                        }
                    }
                }.runTaskTimer(Plugin.instance, 0, 20);
            }
            if (!magnifyUse) {
                new BukkitRunnable() {
                    int timer = 30;

                    @Override
                    public void run() {
                        if (pathway.getBeyonder().getSpirituality() >= 200 && timer != 0) {
                            p.removePotionEffect(PotionEffectType.JUMP);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 0, 10, false, false, false));
                            pathway.getBeyonder().setSpirituality(pathway.getBeyonder().getSpirituality() - 200);
                        } else {
                            cancel();
                        }
                        timer--;
                        if (timer == 0) {
                            cancel();
                        }
                    }
                }.runTaskTimer(Plugin.instance, 0, 20);
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return EmperorItems.createItem(Material.GLASS_PANE, "Magnify", "Varying", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}