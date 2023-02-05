package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class DayAndNight extends Ability {
    public DayAndNight(int identifier, Pathway pathway) {
        super(identifier, pathway);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;
        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                p.getWorld().setTime(p.getWorld().getTime() + 150);
                if(counter >= 20) {
                    pathway.getSequence().removeSpirituality(1000);
                    counter = 0;
                }
                counter++;
                if(!pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);

    }

    @Override
    public ItemStack getItem() {
        ItemStack currentItem = new ItemStack(Material.CLOCK);
        ItemMeta itemMeta = currentItem.getItemMeta();
        itemMeta.setDisplayName("§6Day and Night");
        itemMeta.addEnchant(Enchantment.CHANNELING, 18, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.clear();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §71000/s");
        lore.add("§8§l-----------------");
        lore.add("§6Sun - Pathway (2)");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        return currentItem;
    }
}
