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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class FlaringSun extends Ability {
    public FlaringSun(int identifier, Pathway pathway) {
        super(identifier, pathway);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        //get block player is looking at
        BlockIterator iter = new BlockIterator(p, 15);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (!lastBlock.getType().isSolid()) {
                continue;
            }
            break;
        }

        Location loc = lastBlock.getLocation().add(0, 1, 0);
        loc.getBlock().setType(Material.LIGHT);
        loc.clone().add(0, 1, 0).getBlock().setType(Material.LIGHT);
        loc.clone().add(0, -1, 0).getBlock().setType(Material.LIGHT);
        loc.clone().add(1, 0, 0).getBlock().setType(Material.LIGHT);
        loc.clone().add(-1, 0, 0).getBlock().setType(Material.LIGHT);
        loc.clone().add(0, 0, 1).getBlock().setType(Material.LIGHT);
        loc.clone().add(0, 0, -1).getBlock().setType(Material.LIGHT);

        Location sphereLoc = loc.clone();
        new BukkitRunnable() {
            int counter = 0;
            public double sphereRadius = 5;
            @Override
            public void run() {
                counter++;

                loc.getWorld().spawnParticle(Particle.FLAME, loc, 100, 2, 2, 2, 0);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc, 100, 2, 2, 2, 0);
                for (double i = 0; i <= Math.PI; i += Math.PI / 17) {
                    double radius = Math.sin(i) * sphereRadius;
                    double y = Math.cos(i) * sphereRadius;
                    for (double a = 0; a < Math.PI * 2; a += Math.PI / 17) {
                        double x = Math.cos(a) * radius;
                        double z = Math.sin(a) * radius;
                        sphereLoc.add(x, y, z);
                        Particle.DustOptions dustSphere = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
                        Random random = new Random();
                        if(random.nextInt(10) == 0)
                            sphereLoc.getWorld().spawnParticle(Particle.END_ROD, sphereLoc, 1, 0.25, 0.25, 0.25, 0);
                        sphereLoc.getWorld().spawnParticle(Particle.REDSTONE, sphereLoc, 1, 0.25, 0.25, 0.25, 0, dustSphere);
                        sphereLoc.getWorld().spawnParticle(Particle.FLAME, sphereLoc, 1, 0.25, 0.25, 0.25, 0);
                        sphereLoc.subtract(x, y, z);
                    }
                }

                //damage nearby entities
                ArrayList<Entity> nearbyEntities = (ArrayList<Entity>) loc.getWorld().getNearbyEntities(loc, 2, 2, 2);
                for(Entity entity : nearbyEntities) {
                    if(entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        if (livingEntity.getCategory() == EntityCategory.UNDEAD) {
                            ((Damageable) entity).damage(10, p);
                            livingEntity.setFireTicks(10 * 20);
                        }
                        if(livingEntity.getUniqueId() != pathway.getUuid())
                            livingEntity.setFireTicks(10 * 20);

                    }
                }

                if(counter >= 20 * 20) {
                    loc.getBlock().setType(Material.AIR);
                    loc.clone().add(0, 1, 0).getBlock().setType(Material.AIR);
                    loc.clone().add(0, -1, 0).getBlock().setType(Material.AIR);
                    loc.clone().add(1, 0, 0).getBlock().setType(Material.AIR);
                    loc.clone().add(-1, 0, 0).getBlock().setType(Material.AIR);
                    loc.clone().add(0, 0, 1).getBlock().setType(Material.AIR);
                    loc.clone().add(0, 0, -1).getBlock().setType(Material.AIR);
                    cancel();
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    @Override
    public ItemStack getItem() {
        ItemStack currentItem = new ItemStack(Material.SUNFLOWER);
        ItemMeta itemMeta = currentItem.getItemMeta();
        itemMeta.setDisplayName("§6Flaring Sun");
        itemMeta.addEnchant(Enchantment.CHANNELING, 13, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.values());
        ArrayList<String> lore = new ArrayList<>();
        lore.clear();
        lore.add("§5Click to use");
        lore.add("§5Spirituality: §7200");
        lore.add("§8§l-----------------");
        lore.add("§6Sun - Pathway (4)");
        lore.add("§8" + Bukkit.getPlayer(pathway.getUuid()).getName());
        itemMeta.setLore(lore);
        currentItem.setItemMeta(itemMeta);
        return currentItem;
    }
}
