package de.firecreeper82.pathways.impl.emperor.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.emperor.EmperorItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MagnifySelf extends Ability {
    public final List<String> magnifyUsesGet = new ArrayList<>();
    public int magnifyUse;

    /*
    1: Speed
    2: Jump
    2: Resistance
    3: Fly
    */
    public MagnifySelf(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
        magnifyUsesGet.add(0, "§cHow did you get here?");
        magnifyUsesGet.add(1, "§fMovement speed");
        magnifyUsesGet.add(2, "§fJump height");
    }

    @Override
    public void leftClick() {
        if (pathway.getSequence().getCurrentSequence() <= 4) {
            if (magnifyUse == 2) {
                magnifyUse--;
                p.sendMessage("§fSet your magnify mode to " + pathway.getStringColor() + magnifyUsesGet.get(magnifyUse));
            } else {
                magnifyUse++;
                p.sendMessage("§fSet your magnify mode to " + pathway.getStringColor() + magnifyUsesGet.get(magnifyUse));
            }
        }
    }

    @Override
    public void useAbility() {
        //set magnified to 1 if 0
        if (!pathway.getBeyonder().damageMagnified) {
            pathway.getBeyonder().damageMagnified = true;
        } else {
            switch (magnifyUse) {
                case 0:
                    p.sendMessage("§cThere appears to be a bug here ! Contact clement#0841 on discord and signal what happened.");
                case 1:
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
                        }
                    }.runTaskTimer(Plugin.instance, 0, 20);
                    break;
                case 2:
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
                        }
                    }.runTaskTimer(Plugin.instance, 0, 20);

                    break;

            }
        }
    }

    @Override
    public ItemStack getItem() {
        return EmperorItems.createItem(Material.GLASS_PANE, "Magnify", "Varying", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}