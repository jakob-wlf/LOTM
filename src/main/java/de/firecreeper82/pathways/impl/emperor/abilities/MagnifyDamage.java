package de.firecreeper82.pathways.impl.emperor.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.emperor.EmperorItems;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MagnifyDamage extends Ability {
    int damageBoost = 1;
    boolean isMagnifyingReach = true;

    public MagnifyDamage(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        Plugin.emperorMagnifyDamage.put(p.getUniqueId(),damageBoost);
    }

    @Override
    public void leftClick() {
        if (pathway.getSequence().getCurrentSequence() <= 4) {
            if (p.isSneaking()) {
                if (damageBoost == 1) {
                    p.sendMessage("Your magnify value cannot be lower than 1!");
                } else {
                    damageBoost--;
                    p.sendMessage("Set your magnify damage boost to " + damageBoost);
                }
            } else if (!p.isSneaking()) {
                if (damageBoost == 3) {
                    p.sendMessage("Your magnify value cannot be higher than 3!");
                } else {
                    damageBoost++;
                    p.sendMessage("Set your magnify damage boost to " + damageBoost);
                }
            }
        } else if ((pathway.getSequence().getCurrentSequence() <= 2)) {
            if (p.isSneaking()) {
                if (damageBoost == 1) {
                    p.sendMessage("Your magnify value cannot be lower than 1!");
                } else {
                    damageBoost--;
                    p.sendMessage("Set your magnify damage boost to " + damageBoost);
                }
            } else if (!p.isSneaking()) {
                if (damageBoost == 5) {
                    p.sendMessage("Your magnify value cannot be higher than 5!");
                } else {
                    damageBoost++;
                    p.sendMessage("Set your magnify damage boost to " + damageBoost);
                }
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return EmperorItems.createItem(Material.DIAMOND_SWORD, "Magnify", "Varying", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}