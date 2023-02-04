package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class CleaveOfPurification extends Ability {
    public CleaveOfPurification(int identifier, Pathway pathway) {
        super(identifier, pathway);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        Location loc = p.getLocation().add(0, 1, 0);
        Vector vector = loc.getDirection();

        for(int i = 0; i < 5; i++) {
            loc.add(vector);

            //Spawn Particles
            if(i == 2) {
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1.5f);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc, 10, 0.15, 0.15, 0.15, 0);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 80, 0.2, 0.2, 0.2, dust);
            }

            if(i < 2) {
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 2f);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc, 10, 0.25, 0.25, 0.25, 0);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 80, 0.3, 0.3, 0.3, dust);
            }

            if(loc.getWorld().getNearbyEntities(loc, 1, 1, 1).isEmpty())
                continue;

            for(Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if(entity.getUniqueId() == pathway.getUuid())
                    continue;
                Location entLoc = entity.getLocation();
                if(entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    if (livingEntity.getCategory() == EntityCategory.UNDEAD) {
                        ((Damageable) entity).damage(28, p);
                    } else {
                        if (entity != p)
                            ((Damageable) entity).damage(12, p);
                    }
                    entLoc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, entLoc, 200, 0.2, 0.2, 0.2, 0.15);
                }
            }
        }
    }

    @Override
    public ItemStack getItem() {
        ItemStack currentItem = new ItemStack(Material.HONEYCOMB);
        ItemMeta itemMeta = currentItem.getItemMeta();
        itemMeta.setDisplayName("§6Cleave of Purification");
        itemMeta.addEnchant(Enchantment.CHANNELING, 7, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.clear();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §720");
        lore.add("§8§l-----------------");
        lore.add("§6Sun - Pathway (7)");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        return currentItem;
    }
}
