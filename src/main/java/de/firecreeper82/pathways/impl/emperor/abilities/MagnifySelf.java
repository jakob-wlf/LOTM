package de.firecreeper82.pathways.impl.emperor.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.emperor.EmperorItems;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MagnifySelf extends Ability {
    public final List<String> magnifyUsesGet = new ArrayList<>();
    public int magnifyUse;

    /*
    1: Speed
    2: Jump
    3: at seqReach
    */
    public MagnifySelf(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
        magnifyUsesGet.add(0, "§cHow did you get here?");
        magnifyUsesGet.add(1, "Distance");
        magnifyUsesGet.add(2, "Resistance");
        magnifyUsesGet.add(3, "Gravity");
    }

    @Override
    public void leftClick() {
        if (pathway.getSequence().getCurrentSequence() <= 4) {
            if (magnifyUse == 2) {
                magnifyUse--;
                p.sendMessage("Set your magnify mode to " + pathway.getStringColor() + magnifyUsesGet.get(magnifyUse));
            } else {
                magnifyUse++;
                p.sendMessage("Set your magnify mode to " + pathway.getStringColor() + magnifyUsesGet.get(magnifyUse));
            }
        } else if ((pathway.getSequence().getCurrentSequence() <= 2)) {
            {
                if (magnifyUse == 3) {
                    magnifyUse = 1;
                    p.sendMessage("Set your magnify mode to " + pathway.getStringColor() + magnifyUsesGet.get(magnifyUse));
                } else {
                    magnifyUse++;
                    p.sendMessage("Set your magnify mode to " + pathway.getStringColor() + magnifyUsesGet.get(magnifyUse));
                }
            }
        }
    }

    @Override
    public void useAbility() {
        if (pathway.getBeyonder().isMagnified) {
            return;
        } else {
            switch (magnifyUse) {
                case 1:
                    p.sendMessage("This function has not been implemented yet !");
                    break;
                case 2:
                    Plugin.emperorMagnifyDamageDown.put(p.getUniqueId(), 4);
                    break;
                case 3:
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (pathway.getBeyonder().getSpirituality() <= 200) {
                                p.setAllowFlight(true);
                                pathway.getBeyonder().setSpirituality(pathway.getBeyonder().getSpirituality() - 200);
                            } else {
                                p.setAllowFlight(false);
                                cancel();
                            }
                        }

                    }.runTaskTimer(Plugin.instance, 0, 20);
                    break;
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return EmperorItems.createItem(Material.BLACK_STAINED_GLASS_PANE, "Magnify", "Varying", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}