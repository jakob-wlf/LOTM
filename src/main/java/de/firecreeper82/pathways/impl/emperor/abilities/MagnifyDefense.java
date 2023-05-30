package de.firecreeper82.pathways.impl.emperor.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.emperor.EmperorItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MagnifyDefense extends Ability {
    private int defenseUse;
    public MagnifyDefense(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
    }

    @Override
    public void leftClick() {
        if (pathway.getSequence().getCurrentSequence() <= 4) {
            if (p.isSneaking()) {
                if (defenseUse == 1) {
                    p.sendMessage("§CYour magnify value cannot be lower than 1!");
                } else {
                    defenseUse--;
                    p.sendMessage("§fSet your magnify defense boost to " + defenseUse);

                }
            } else if (!p.isSneaking()) {
                if (defenseUse == 3) {
                    p.sendMessage("§CYour magnify value cannot be higher than 3!");
                } else {
                    defenseUse++;
                    p.sendMessage("§fSet your magnify defense boost to " + defenseUse);
                }
            }
        } else if ((pathway.getSequence().getCurrentSequence() <= 2)) {
            if (p.isSneaking()) {
                if (defenseUse == 1) {
                    p.sendMessage("§CYour magnify value cannot be lower than 1!");
                } else {
                    defenseUse--;
                    p.sendMessage("§fSet your magnify defense boost to " + defenseUse);
                }
            } else if (!p.isSneaking()) {
                if (defenseUse == 5) {
                    p.sendMessage("§CYour magnify value cannot be higher than 5!");
                } else {
                    defenseUse++;
                    p.sendMessage("§fSet your magnify defense boost to " + defenseUse);
                }
            }
        }
    }

    @Override
    public void useAbility() {
        if (!p.isSneaking()) {
            p.sendMessage("§fToggled Magnify Defense. Hold Shift and right click again to deactivate.");
            Plugin.emperorMagnifyDamageDown.put(p.getUniqueId(), defenseUse);
            pathway.getBeyonder().damageMagnifiedDown = true;
        } else if (p.isSneaking()) {
            p.sendMessage("§fDeactivated Magnify Damage. Press right-click to activate.");
            pathway.getBeyonder().damageMagnifiedDown = false;
        }
    }

    @Override
    public ItemStack getItem() {
        return EmperorItems.createItem(Material.SCUTE, "Magnify Defense", "1024/s", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
