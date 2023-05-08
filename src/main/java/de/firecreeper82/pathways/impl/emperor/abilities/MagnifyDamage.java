package de.firecreeper82.pathways.impl.emperor.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.emperor.EmperorItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MagnifyDamage extends Ability {
    private int damageBoost;

    public MagnifyDamage(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        if (!p.isSneaking()) {
            p.sendMessage("§fToggled Magnify Damage. Hold Shift and right click again to deactivate.");
            Plugin.emperorMagnifyDamage.put(p.getUniqueId(), damageBoost);
            pathway.getBeyonder().damageMagnified = true;
        } else if (p.isSneaking()) {
            p.sendMessage("§fDeactivated Magnify Damage. Press right-click to activate.");
            pathway.getBeyonder().damageMagnified = false;
        }
    }

    @Override
    public void leftClick() {
        if (pathway.getSequence().getCurrentSequence() <= 4) {
            if (p.isSneaking()) {
                if (damageBoost == 1) {
                    p.sendMessage("§CYour magnify value cannot be lower than 1!");
                } else {
                    damageBoost--;
                    p.sendMessage("§fSet your magnify damage boost to " + damageBoost);

                }
            } else if (!p.isSneaking()) {
                if (damageBoost == 3) {
                    p.sendMessage("§CYour magnify value cannot be higher than 3!");
                } else {
                    damageBoost++;
                    p.sendMessage("§fSet your magnify damage boost to " + damageBoost);
                }
            }
        } else if ((pathway.getSequence().getCurrentSequence() <= 2)) {
            if (p.isSneaking()) {
                if (damageBoost == 1) {
                    p.sendMessage("§CYour magnify value cannot be lower than 1!");
                } else {
                    damageBoost--;
                    p.sendMessage("§fSet your magnify damage boost to " + damageBoost);
                }
            } else if (!p.isSneaking()) {
                if (damageBoost == 5) {
                    p.sendMessage("§CYour magnify value cannot be higher than 5!");
                } else {
                    damageBoost++;
                    p.sendMessage("§fSet your magnify damage boost to " + damageBoost);
                }
            }
        }
    }

    @Override
    public ItemStack getItem() {


        return EmperorItems.createItem(Material.BLAZE_POWDER, "Magnify Damage", "Varying", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}