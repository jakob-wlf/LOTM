package de.firecreeper82.pathways.impl.emperor.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.emperor.EmperorItems;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

import java.util.Objects;

public class MagnifyDamage extends Ability {
    boolean inUse;
    private int damageBoost;

    public MagnifyDamage(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        ItemStack PrevItem, CurrentItem;
        Integer PrevDamageBoost, CurrentDamageBoost;
/*        if (!inUse) {
            inUse = true;
        } else {
            inUse = false;
        }
 */
        new BukkitRunnable() {

            @Override
            public void run() {
                if (CurrentItem == PrevItem) {
                    return;
                } else if (CurrentItem.getType() == Material.AIR) {
                    PrevItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, -PrevDamageBoost);
                    return;
                } else if (CurrentItem.getType() != Material.AIR) {

                }
            }
        }.runTaskTimer(Plugin.instance, 0, 20);


        @Override
        public void leftClick () {
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
        public ItemStack getItem () {


            return EmperorItems.createItem(Material.BLAZE_POWDER, "Magnify Damage", "Varying", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
        }
    }