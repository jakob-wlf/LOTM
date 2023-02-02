package de.firecreeper82.pathways.pathways.sun;

import com.google.common.util.concurrent.AtomicDouble;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.VectorUtils;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Sequence;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SunSequence extends Sequence {

    public SunSequence(Pathway pathway) {
        super(pathway);
        currentSequence = 9;
        init();
    }

    public SunSequence(Pathway pathway, int optionalSequence) {
        super(pathway, optionalSequence);
        init();
    }

    public void init() {
        usesAbilities = new boolean[12];
        Arrays.fill(usesAbilities, false);

        sequenceEffects = new HashMap<>();
        initEffects();

        new BukkitRunnable() {
            @Override
            public void run() {
                Plugin.beyonders.get(pathway.uuid).updateSpirituality();
            }
        }.runTaskLater(Plugin.instance, 2);
    }

    //Passive effects
    public void initEffects() {
        PotionEffect[] effects7 = {
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 0, false, false, false),
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 0, false, false, false),
                new PotionEffect(PotionEffectType.SPEED, 60, 0, false, false, true),
        };
        sequenceEffects.put(7, effects7);

        PotionEffect[] effects6 = {
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 1, false, false, false),
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 2, false, false, false),
                new PotionEffect(PotionEffectType.SPEED, 60, 1, false, false, true),
        };
        sequenceEffects.put(6, effects6);
    }

    @Override
    public void useAbility(ItemStack item, PlayerInteractEvent e) {
        if(!checkValid(item))
            return;

        e.setCancelled(true);
        useAbility(item.getItemMeta().getEnchantLevel(Enchantment.CHANNELING), item);
    }


    @Override
    public void useAbility(int ability, ItemStack item) {

        int spiritualityDrainage = 0;
        try {
            String line = item.getItemMeta().getLore().get(1);
            spiritualityDrainage = Integer.parseInt(line.substring(18));
        }
        catch (Exception ignored) {}

        if(spiritualityDrainage > pathway.getBeyonder().getSpirituality())
            return;

        //TODO rework hitting enemies with the .damage function
        //  use BoundingBox.overlap to detect hit enemies
        //  check if entity is player by doing == p instead of checking uuid
        //  use players eye location instead of adding 1 to the location
        //Abilities
        switch(ability) {

            //Bard - Holy Song
            case 1 -> {
                Player p = pathway.getBeyonder().getPlayer();
                if(usesAbilities[0]) {
                    p.sendMessage("§6You are already singing");
                    return;
                }

                //remove spirituality
                removeSpirituality(spiritualityDrainage);

                p.getWorld().playSound(p, Sound.MUSIC_DISC_MELLOHI, 10f, 1f);
                AtomicInteger counter = new AtomicInteger();
                usesAbilities[0] = true;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.getWorld().spawnParticle(Particle.NOTE, p.getLocation(), 50, 5, 5, 5);
                        counter.getAndIncrement();
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 1, false, false, false));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1, false, false, false));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 0, false, false, false));
                        if(counter.get() >= 95) {
                            counter.set(0);
                            cancel();
                            usesAbilities[0] = false;
                        }
                    }
                }.runTaskTimer(Plugin.instance, 1, 20);
            }

            //TODO make it be able to hit enemies in the air too
            //Light Supplicant - Holy Light
            case 2 -> {
                Player p = pathway.getBeyonder().getPlayer();
                if(usesAbilities[1]) {
                    return;
                }

                //remove spirituality
                removeSpirituality(spiritualityDrainage);

                //get block player is looking at
                BlockIterator iter = new BlockIterator(p, 15);
                Block lastBlock = iter.next();
                while (iter.hasNext()) {
                    lastBlock = iter.next();
                    if (lastBlock.getType() == Material.AIR) {
                        continue;
                    }
                    break;
                }
                Location loc = lastBlock.getLocation();
                loc.add(0, 14, 0);

                usesAbilities[1] = true;
                AtomicInteger counter = new AtomicInteger();
                final Material[] lastMaterial = {loc.getBlock().getType()};
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        counter.getAndIncrement();

                        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
                        for(double i = 0; i < 3.2; i+=0.8) {
                            for(int j = 0; j < 100; j++) {
                                double x = i * Math.cos(j);
                                double z = i * Math.sin(j);
                                loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + x, loc.getY(), loc.getZ() + z, 5, dust);
                                loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc.getX() + x, loc.getY() + 1, loc.getZ() + z, 1, 0, 0, 0, 0);
                            }
                        }

                        loc.getBlock().setType(lastMaterial[0]);
                        loc.subtract(0, 0.75, 0);
                        lastMaterial[0] = loc.getBlock().getType();
                        loc.getBlock().setType(Material.LIGHT);

                        if((lastMaterial[0].isSolid() && counter.get() >= 18.6) || counter.get() >= 200) {
                            loc.getBlock().setType(lastMaterial[0]);
                            counter.set(0);
                            cancel();
                            usesAbilities[1] = false;

                            //damage nearby entities
                            ArrayList<Entity> nearbyEntities = (ArrayList<Entity>) loc.getWorld().getNearbyEntities(loc.subtract(5, 0, 5), 10, 10, 10);
                            for(Entity entity : nearbyEntities) {
                                if(entity instanceof LivingEntity) {
                                    LivingEntity livingEntity = (LivingEntity) entity;
                                    if (livingEntity.getCategory() == EntityCategory.UNDEAD) {
                                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1));
                                    } else {
                                        if(livingEntity.getUniqueId() != pathway.getUuid())
                                            ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 0));
                                    }
                                }
                            }
                        }
                    }
                }.runTaskTimer(Plugin.instance, 0, 1);
            }

            //Light Supplicant - Illuminate
            case 3 -> {
                Player p = pathway.getBeyonder().getPlayer();
                if(usesAbilities[2]) {
                    return;
                }

                //remove spirituality
                removeSpirituality(spiritualityDrainage);

                //get block player is looking at
                BlockIterator iter = new BlockIterator(p, 9);
                Block lastBlock = iter.next();
                Block previousBlock;
                while (iter.hasNext()) {
                    previousBlock = lastBlock;
                    lastBlock = iter.next();
                    if(lastBlock.getType().isSolid()) {
                        lastBlock = previousBlock;
                        break;
                    }

                }
                Location loc = lastBlock.getLocation();

                loc.getBlock().setType(Material.LIGHT);
                loc.add(0.5, 0.5, 0.5);

                AtomicInteger counter = new AtomicInteger();

                usesAbilities[2] = true;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        counter.getAndIncrement();
                        double x = Math.cos(counter.get());
                        double z = Math.sin(counter.get());
                        double y = Math.sin(counter.get());
                        loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() + x, loc.getY(), loc.getZ() + z, 1, 0, 0, 0, 0);
                        loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() + x, loc.getY() + y, loc.getZ(), 1, 0, 0, 0, 0);
                        y = Math.cos(counter.get());
                        loc.getWorld().spawnParticle(Particle.FLAME, loc.getX(), loc.getY() + y, loc.getZ() + z, 1, 0, 0, 0, 0);

                        loc.getWorld().spawnParticle(Particle.END_ROD, loc, 5, 0.25, 0.25, 0.25, 0);

                        if(counter.get() == 2 * 20) {
                            usesAbilities[2] = false;
                        }

                        if(counter.get() >= 15 * 20) {
                            loc.getBlock().setType(Material.AIR);
                            cancel();
                        }
                    }
                }.runTaskTimer(Plugin.instance, 0, 1);
            }

            //Solar High Priest - Fire of Light
            case 4 -> {
                Player p = pathway.getBeyonder().getPlayer();
                if(usesAbilities[3]) {
                    return;
                }

                //remove spirituality
                removeSpirituality(spiritualityDrainage);

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

                //setting the fire
                Location loc = lastBlock.getLocation().add(0, 1, 0);
                if(!loc.getBlock().getType().isSolid())
                    loc.getBlock().setType(Material.FIRE);
                loc.add(1, 0, 0);
                if(!loc.getBlock().getType().isSolid())
                    loc.getBlock().setType(Material.FIRE);
                loc.add(-2, 0, 0);
                if(!loc.getBlock().getType().isSolid())
                    loc.getBlock().setType(Material.FIRE);
                loc.add(1, 0, -1);
                if(!loc.getBlock().getType().isSolid())
                    loc.getBlock().setType(Material.FIRE);
                loc.add(0, 0, 2);
                if(!loc.getBlock().getType().isSolid())
                    loc.getBlock().setType(Material.FIRE);
                loc.subtract(0, 0, 1);

                loc.add(0.5, 0.5, 0.5);

                final Material[] lightBlock = {loc.getBlock().getType()};
                loc.getBlock().setType(Material.LIGHT);

                usesAbilities[3] = true;
                AtomicInteger counter = new AtomicInteger();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        counter.getAndIncrement();

                        loc.getWorld().spawnParticle(Particle.FLAME, loc, 50, 0.75, 0.75, 0.75, 0);
                        loc.getWorld().spawnParticle(Particle.END_ROD, loc, 8, 0.75, 0.75, 0.75, 0);

                        //damage nearby entities
                        ArrayList<Entity> nearbyEntities = (ArrayList<Entity>) loc.getWorld().getNearbyEntities(loc, 2, 2, 2);
                        for(Entity entity : nearbyEntities) {
                            if(entity instanceof LivingEntity) {
                                LivingEntity livingEntity = (LivingEntity) entity;
                                if (livingEntity.getCategory() == EntityCategory.UNDEAD) {
                                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 0));
                                }
                                if(livingEntity.getUniqueId() != pathway.getUuid())
                                    livingEntity.setFireTicks(10 * 20);

                            }
                        }

                        if(counter.get() >= 5 * 20) {
                            loc.getBlock().setType(Material.AIR);
                            cancel();
                            usesAbilities[3] = false;
                            loc.getBlock().setType(lightBlock[0]);
                        }
                    }
                }.runTaskTimer(Plugin.instance, 0, 1);
            }

            //TODO make it be able to hit enemies in the air too
            //Solar High Priest - Holy Light Summoning
            case 5 -> {
                Player p = pathway.getBeyonder().getPlayer();
                if(usesAbilities[4]) {
                    return;
                }

                //remove spirituality
                removeSpirituality(spiritualityDrainage);

                //get block player is looking at
                BlockIterator iter = new BlockIterator(p, 22);
                Block lastBlock = iter.next();
                while (iter.hasNext()) {
                    lastBlock = iter.next();
                    if (!lastBlock.getType().isSolid()) {
                        continue;
                    }
                    break;
                }
                Location loc = lastBlock.getLocation();
                loc.add(0, 14, 0);

                //Runnable
                usesAbilities[4] = true;
                AtomicInteger counter = new AtomicInteger();
                final Material[] lastMaterial = {loc.getBlock().getType()};
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        counter.getAndIncrement();

                        //Particles
                        loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + 3.2, loc.getY(), loc.getZ(), 6, 0.1, 0, 0.1, 0);
                        loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() - 3.2, loc.getY(), loc.getZ(), 6, 0.1, 0, 0.1, 0);
                        loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX(), loc.getY(), loc.getZ() + 3.2, 6, 0.1, 0, 0.1, 0);
                        loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX(), loc.getY(), loc.getZ() - 3.2, 6, 0.1, 0, 0.1, 0);
                        loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + 2.4, loc.getY(), loc.getZ() + 2.4, 5, 0.1, 0, 0.1, 0);
                        loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() - 2.4, loc.getY(), loc.getZ() - 2.4, 5, 0.1, 0, 0.1, 0);
                        loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() - 2.4, loc.getY(), loc.getZ() + 2.4, 5, 0.1, 0, 0.1, 0);
                        loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + 2.4, loc.getY(), loc.getZ() - 2.4, 5, 0.1, 0, 0.1, 0);

                        loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() + 2.7, loc.getY(), loc.getZ(), 12, 0.2, 0, 0.2, 0);
                        loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() - 2.7, loc.getY(), loc.getZ(), 12, 0.2, 0, 0.2, 0);
                        loc.getWorld().spawnParticle(Particle.FLAME, loc.getX(), loc.getY(), loc.getZ() + 2.7, 12, 0.2, 0, 0.2, 0);
                        loc.getWorld().spawnParticle(Particle.FLAME, loc.getX(), loc.getY(), loc.getZ() - 2.7, 12, 0.2, 0, 0.2, 0);
                        loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() + 1.9, loc.getY(), loc.getZ() + 1.9, 12, 0.2, 0, 0.2, 0);
                        loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() - 1.9, loc.getY(), loc.getZ() - 1.9, 12, 0.2, 0, 0.2, 0);
                        loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() - 1.9, loc.getY(), loc.getZ() + 1.9, 12, 0.2, 0, 0.2, 0);
                        loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() + 1.9, loc.getY(), loc.getZ() - 1.9, 12, 0.2, 0, 0.2, 0);


                        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1.25f);
                        for(double i = 0; i < 3.2; i+=0.8) {
                            for(int j = 0; j < 100; j++) {
                                double x = i * Math.cos(j);
                                double z = i * Math.sin(j);
                                loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + x, loc.getY(), loc.getZ() + z, 2, dust);
                                if(j % 2 == 0)
                                    loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc.getX() + x, loc.getY() + 1, loc.getZ() + z, 1, 0, 0, 0, 0);
                            }
                        }

                        //light at current loc
                        loc.getBlock().setType(lastMaterial[0]);
                        loc.subtract(0, 1, 0);
                        lastMaterial[0] = loc.getBlock().getType();
                        loc.getBlock().setType(Material.LIGHT);

                        if((lastMaterial[0].isSolid() && counter.get() >= 12) || counter.get() >= 200) {
                            loc.getBlock().setType(lastMaterial[0]);
                            counter.set(0);
                            cancel();

                            //Light that stays at the ground for a bit
                            Location lightLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
                            Material[] lightMaterial = {
                                    lightLoc.getBlock().getType(),
                                    lightLoc.add(1, 0, 0).getBlock().getType(),
                                    lightLoc.add(-2, 0, 0).getBlock().getType(),
                                    lightLoc.add(1, 0, 1).getBlock().getType(),
                                    lightLoc.add(0, 0, -2).getBlock().getType()
                            };

                            Block[] lightBlock = {
                                    lightLoc.getBlock(),
                                    lightLoc.add(1, 0, 0).getBlock(),
                                    lightLoc.add(-2, 0, 0).getBlock(),
                                    lightLoc.add(1, 0, 1).getBlock(),
                                    lightLoc.add(0, 0, -2).getBlock()
                            };

                            for(Block b : lightBlock) {
                                b.setType(Material.LIGHT);
                            }

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for(int i = 0; i < lightBlock.length; i++) {
                                        Block b = lightBlock[i];
                                        b.setType(lightMaterial[i]);
                                    }
                                }
                            }.runTaskLater(Plugin.instance, 2 * 20);


                            //damage nearby entities
                            ArrayList<Entity> nearbyEntities = (ArrayList<Entity>) loc.getWorld().getNearbyEntities(loc, 15, 15, 15);
                            for(Entity entity : nearbyEntities) {
                                if(entity instanceof LivingEntity) {
                                    LivingEntity livingEntity = (LivingEntity) entity;
                                    if (livingEntity.getCategory() == EntityCategory.UNDEAD) {
                                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 3));
                                    } else {
                                        if(livingEntity.getUniqueId() != pathway.getUuid())
                                            ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 1));
                                    }
                                }
                            }

                            //Particles on ground
                            AtomicDouble radius = new AtomicDouble();
                            AtomicInteger factor = new AtomicInteger();
                            radius.set(1.8);
                            loc.add(0, 1, 0);
                            Particle.DustOptions dustRipple = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1.5f);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    radius.set(radius.get() + 0.75);
                                    for(int i = 0; i < 100; i++) {
                                        factor.incrementAndGet();
                                        double x = radius.get() * Math.cos(factor.get());
                                        double z = radius.get() * Math.sin(factor.get());
                                        loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + x, loc.getY(), loc.getZ() + z, 1, 0, 0, 0, 0);
                                        loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc.getX() + x, loc.getY(), loc.getZ() + z, 2, 0.1, 0, 0.1, 0.15);
                                        loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + x + 0.2, loc.getY(), loc.getZ() + z + 0.2, 3, dustRipple);
                                    }

                                    if(radius.get() >= 9) {
                                        cancel();
                                        usesAbilities[4] = false;
                                    }
                                }
                            }.runTaskTimer(Plugin.instance, 0, 1);
                        }
                    }
                }.runTaskTimer(Plugin.instance, 0, 1);
            }

            //Solar High Priest - Holy Oath
            case 6 -> {
                Player p = pathway.getBeyonder().getPlayer();

                Location loc = p.getLocation();

                if(usesAbilities[5]) {
                    usesAbilities[5] = false;
                    return;
                }

                else usesAbilities[5] = true;

                //Particle effects
                double radius = 1;
                for(double y = 0; y <= 2; y+=0.05) {
                    double x = radius * Math.cos(y * 20);
                    double z = radius * Math.sin(y * 20);
                    double x2 = radius * Math.sin(y * 20);
                    double z2 = radius * Math.cos(y * 20);
                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1.25f);
                    loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + x, loc.getY() + y, loc.getZ() + z, 10, dust);
                    loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + x2, loc.getY() + y, loc.getZ() + z2, 2, 0, 0, 0, 0);
                }

                //Short light placement
                Material[] lightMaterial = {loc.add(0, 1, 0).getBlock().getType()};
                Block[] lightBlock = {loc.add(0, 1, 0).getBlock()};
                loc.getBlock().setType(Material.LIGHT);

                //Potion effects every second
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(!usesAbilities[5]) {
                            cancel();
                            return;
                        }
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 3, false, false, false));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 2, false, false, false));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 2, false, false, false));

                        pathway.getBeyonder().setSpirituality(pathway.getBeyonder().getSpirituality() - 5);

                        if(pathway.getBeyonder().getSpirituality() <= 5) {
                            usesAbilities[5] = false;
                            cancel();
                        }
                    }
                }.runTaskTimer(Plugin.instance, 0, 20);

                //Particle effects while active
                AtomicDouble counter = new AtomicDouble();
                counter.set(0);
                AtomicDouble counterY = new AtomicDouble();
                counterY.set(0);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(!usesAbilities[5]) {
                            cancel();
                            return;
                        }

                        counter.set(counter.get() + 0.25);
                        counterY.set(counterY.get() + 0.25);

                        double radiusActive = 0.75;
                        double x = radiusActive * Math.cos(counter.get());
                        double z = radiusActive * Math.sin(counter.get());

                        Location pLoc = p.getLocation();

                        pLoc.getWorld().spawnParticle(Particle.END_ROD, pLoc.getX() + x, pLoc.getY() + counterY.get(), pLoc.getZ() + z, 20, 0, 0, 0, 0);

                        if(counterY.get() >= 2)
                            counterY.set(0);

                    }
                }.runTaskTimer(Plugin.instance, 0, 1);

                //remove light
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        lightBlock[0].setType(lightMaterial[0]);
                    }
                }.runTaskLater(Plugin.instance, 40);
            }

            //Solar High Priest - Cleave of Purification
            case 7 -> {
                Player p = pathway.getBeyonder().getPlayer();
                if(usesAbilities[6]) {
                    return;
                }

                //remove spirituality
                removeSpirituality(spiritualityDrainage);

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
                                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 2));
                            } else {
                                if (livingEntity.getUniqueId() != pathway.getUuid())
                                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 1));
                            }
                            entLoc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, entLoc, 200, 0.2, 0.2, 0.2, 0.15);
                        }
                    }
                }
            }

            //Priest of Light - Light of Holiness
            case 10 -> {
                Player p = pathway.getBeyonder().getPlayer();
                if(usesAbilities[9]) {
                    return;
                }

                //remove spirituality
                removeSpirituality(spiritualityDrainage);

                //get block player is looking at
                BlockIterator iter = new BlockIterator(p, 22);
                Block lastBlock = iter.next();
                while (iter.hasNext()) {
                    lastBlock = iter.next();
                    if (!lastBlock.getType().isSolid()) {
                        continue;
                    }
                    break;
                }
                Location loc = lastBlock.getLocation();
                loc.add(0, 19, 0);

                //Runnable
                usesAbilities[9] = true;
                AtomicInteger counter = new AtomicInteger();
                final Material[] lastMaterial = {loc.getBlock().getType()};
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        counter.getAndIncrement();

                        //Particles
                        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
                        loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + 3.8, loc.getY(), loc.getZ(), 20, 0.15, 0, 0.15, 0, dust);
                        loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() - 3.8, loc.getY(), loc.getZ(), 20, 0.15, 0, 0.15, 0, dust);
                        loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX(), loc.getY(), loc.getZ() + 3.8, 20, 0.15, 0, 0.15, 0, dust);
                        loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX(), loc.getY(), loc.getZ() - 3.8, 20, 0.15, 0, 0.15, 0, dust);
                        loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + 3, loc.getY(), loc.getZ() + 3, 20, 0.15, 0, 0.15, 0, dust);
                        loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() - 3, loc.getY(), loc.getZ() - 3, 20, 0.15, 0, 0.15, 0, dust);
                        loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() - 3, loc.getY(), loc.getZ() + 3, 20, 0.15, 0, 0.15, 0, dust);
                        loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + 3, loc.getY(), loc.getZ() - 3, 20, 0.15, 0, 0.15, 0, dust);


                        for(double i = 0; i < 3.8; i+=0.4) {
                            for(int j = 0; j < 40; j++) {
                                double x = i * Math.cos(j);
                                double z = i * Math.sin(j);
                                loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + x, loc.getY(), loc.getZ() + z, 2, 0.1, 0.2, 0.1, 0);
                                if((int) loc.getY() % 2 == 0)
                                    loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() + x, loc.getY() + 0.25, loc.getZ() + z, 1, 0.2, 0, 0.2, 0);
                            }
                        }

                        //light at current loc
                        loc.getBlock().setType(lastMaterial[0]);
                        loc.subtract(0, 1, 0);
                        lastMaterial[0] = loc.getBlock().getType();
                        loc.getBlock().setType(Material.LIGHT);

                        //Reached ground
                        if((lastMaterial[0].isSolid() && counter.get() >= 17) || counter.get() >= 200) {
                            loc.getBlock().setType(lastMaterial[0]);
                            counter.set(0);
                            cancel();

                            //Light that stays at the ground for a bit
                            Location lightLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
                            Material[] lightMaterial = {
                                    lightLoc.getBlock().getType(),
                                    lightLoc.add(1, 0, 0).getBlock().getType(),
                                    lightLoc.add(-2, 0, 0).getBlock().getType(),
                                    lightLoc.add(1, 0, 1).getBlock().getType(),
                                    lightLoc.add(0, 0, -2).getBlock().getType()
                            };

                            Block[] lightBlock = {
                                    lightLoc.getBlock(),
                                    lightLoc.add(1, 0, 0).getBlock(),
                                    lightLoc.add(-2, 0, 0).getBlock(),
                                    lightLoc.add(1, 0, 1).getBlock(),
                                    lightLoc.add(0, 0, -2).getBlock()
                            };

                            for(Block b : lightBlock) {
                                b.setType(Material.LIGHT);
                            }

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for(int i = 0; i < lightBlock.length; i++) {
                                        Block b = lightBlock[i];
                                        b.setType(lightMaterial[i]);
                                    }
                                }
                            }.runTaskLater(Plugin.instance, 2 * 20);

                            //Replace the ground with burned stuff
                            int burnRadius = 8;
                            for(int i = 2; i > -4; i--)
                            for (int x = -burnRadius; x <= burnRadius; x++) {
                                for (int z = -burnRadius; z <= burnRadius; z++) {
                                    if( (x*x) + (z*z) <= Math.pow(burnRadius, 2)) {
                                        Block block = p.getWorld().getBlockAt((int) loc.getX() + x, (int) loc.getY() + i, (int) loc.getZ() + z);
                                        if(block.getType() == Material.DIRT || block.getType() == Material.DIRT_PATH || block.getType() == Material.COARSE_DIRT || block.getType() == Material.ROOTED_DIRT || block.getType() == Material.GRASS_BLOCK)
                                            block.setType(Material.NETHERRACK);
                                        if(block.getType() == Material.STONE || block.getType() == Material.COBBLESTONE || block.getType() == Material.DIORITE || block.getType() == Material.ANDESITE || block.getType() == Material.GRANITE || block.getType() == Material.DEEPSLATE || block.getType() == Material.TUFF || block.getType() == Material.CALCITE || block.getType() == Material.GRAVEL)
                                            block.setType(Material.BASALT);
                                        if(block.getType() == Material.WATER)
                                            block.setType(Material.AIR);
                                        if(block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR) {
                                            Random rand = new Random();
                                            if(rand.nextInt(4) == 0) {
                                                block.setType(Material.FIRE);
                                            }
                                        }
                                        if(block.getType() == Material.SAND || block.getType() == Material.RED_SAND)
                                            block.setType(Material.GLASS);
                                    }
                                }
                            }


                            //damage nearby entities
                            ArrayList<Entity> nearbyEntities = (ArrayList<Entity>) loc.getWorld().getNearbyEntities(loc, 15, 15, 15);
                            for(Entity entity : nearbyEntities) {
                                if(entity instanceof LivingEntity) {
                                    LivingEntity livingEntity = (LivingEntity) entity;
                                    if (livingEntity.getCategory() == EntityCategory.UNDEAD) {
                                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 5));
                                        entity.setFireTicks(100);
                                    } else {
                                        if(livingEntity.getUniqueId() != pathway.getUuid()) {
                                            ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 3));
                                            entity.setFireTicks(100);
                                        }
                                    }
                                }
                            }

                            //Particles on ground
                            AtomicDouble radius = new AtomicDouble();
                            AtomicDouble radiusFlame = new AtomicDouble();
                            radius.set(1.8);
                            radiusFlame.set(1.8);
                            loc.add(0, 1, 0);
                            Particle.DustOptions dustRipple = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1.5f);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    radiusFlame.set(radiusFlame.get() + 0.75);
                                    for(int j = 0; j < 150; j++) {
                                        double x = radiusFlame.get() * Math.cos(j);
                                        double z = radiusFlame.get() * Math.sin(j);
                                        loc.getWorld().spawnParticle(Particle.FLAME, loc.getX() + x, loc.getY(), loc.getZ() + z, 5, 0, 0, 0, 0);
                                    }

                                    if(radiusFlame.get() >= 11) {
                                        cancel();
                                        usesAbilities[9] = false;
                                    }
                                }
                            }.runTaskTimer(Plugin.instance, 0, 1);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    radius.set(radius.get() + 0.75);
                                    for(int i = 0; i < 150; i++) {
                                        double x = radius.get() * Math.cos(i);
                                        double z = radius.get() * Math.sin(i);
                                        loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + x, loc.getY(), loc.getZ() + z, 1, 0, 0, 0, 0);
                                        loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc.getX() + x, loc.getY(), loc.getZ() + z, 2, 0.1, 0, 0.1, 0.15);
                                        loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + x + 0.2, loc.getY(), loc.getZ() + z + 0.2, 3, dustRipple);
                                    }

                                    if(radius.get() >= 14) {
                                        cancel();
                                        usesAbilities[9] = false;
                                    }
                                }
                            }.runTaskTimer(Plugin.instance, 4, 1);
                        }
                    }
                }.runTaskTimer(Plugin.instance, 0, 1);
            }

            //Priest of Light - Light of Purification
            case 11 -> {
                Player p = pathway.getBeyonder().getPlayer();
                if(usesAbilities[10]) {
                    return;
                }
                //remove spirituality
                removeSpirituality(spiritualityDrainage);

                Location loc = p.getLocation();

                //Spawning Particles
                AtomicDouble radius = new AtomicDouble();
                radius.set(1.8);
                loc.add(0, 1, 0);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Particle.DustOptions dustRipple = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
                        radius.set(radius.get() + 0.75);
                        for(int j = 0; j < 30 * radius.get(); j++) {
                            double x = radius.get() * Math.cos(j);
                            double z = radius.get() * Math.sin(j);
                            loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + x, loc.getY(), loc.getZ() + z, 5, 0.2, 1, 0.2, 0, dustRipple);
                            Random rand = new Random();
                            if(j % (rand.nextInt(8) + 1) == 0)
                                loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + x, loc.getY(), loc.getZ() + z, 1, 0.2, 1, 0.2, 0);

                            //checking for entities
                            for(Entity entity : loc.getWorld().getNearbyEntities(new Location(loc.getWorld(), loc.getX() + x, loc.getY(), loc.getZ() + z), 1, 3, 1)) {
                                if(entity instanceof LivingEntity) {
                                    if(((LivingEntity) entity).getCategory() == EntityCategory.UNDEAD)
                                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 4, false, false));
                                }
                            }
                        }

                        if(radius.get() >= 20) {
                            cancel();
                            usesAbilities[9] = false;
                        }
                    }
                }.runTaskTimer(Plugin.instance, 0, 1);

            }

            //Unshadowed - Spear of Light
            case 12 -> {
                Player p = pathway.getBeyonder().getPlayer();
                if(usesAbilities[11]) {
                    return;
                }

                //remove spirituality
                removeSpirituality(spiritualityDrainage);

                //get block player is looking at
                BlockIterator iter = new BlockIterator(p, 40);
                Block lastBlock = iter.next();
                while (iter.hasNext()) {
                    lastBlock = iter.next();
                    if (!lastBlock.getType().isSolid()) {
                        continue;
                    }
                    break;
                }

                Location loc = lastBlock.getLocation();

                float angle = p.getEyeLocation().getYaw()/60;
                Location spearLoc = p.getEyeLocation().subtract(Math.cos(angle), 0, Math.sin(angle));


                Vector dir = p.getLocation().getDirection().normalize();
                Vector direction = dir.clone();
                Location spearLocation = spearLoc.clone();
                buildSpear(spearLoc, dir);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        spearLocation.add(direction);
                        buildSpear(spearLocation.clone(), direction.clone());

                        if(!spearLocation.getWorld().getNearbyEntities(spearLocation, 5, 5, 5).isEmpty()) {
                            for(Entity entity : spearLocation.getWorld().getNearbyEntities(spearLocation, 5, 5, 5)) {
                                if (entity instanceof LivingEntity) {
                                    // Ignore player that initiated the shot
                                    if (entity == p) {
                                        continue;
                                    }
                                    Vector particleMinVector = new Vector(
                                            spearLocation.getX() - 0.25,
                                            spearLocation.getY() - 0.25,
                                            spearLocation.getZ() - 0.25);
                                    Vector particleMaxVector = new Vector(
                                            spearLocation.getX() + 0.25,
                                            spearLocation.getY() + 0.25,
                                            spearLocation.getZ() + 0.25);

                                    if(entity.getBoundingBox().overlaps(particleMinVector,particleMaxVector)){

                                        spearLocation.getWorld().spawnParticle(Particle.END_ROD, spearLocation, 200, 0, 0, 0, 0.5);

                                        entity.setVelocity(entity.getVelocity().add(spearLocation.getDirection().normalize().multiply(1.5)));

                                        ((Damageable) entity).damage(5, p);
                                        cancel();
                                        return;
                                    }
                                }
                            }
                        }

                        if(spearLocation.getBlock().getType().isSolid()) {
                            spearLocation.getWorld().createExplosion(spearLocation, 4);
                            cancel();
                        }
                    }
                }.runTaskTimer(Plugin.instance, 5, 0);
            }
        }
    }

    @Override
    public boolean checkValid(ItemStack item) {
        if(pathway.getItems().getItems().contains(item))
            return true;
        else
            return false;
    }

    @Override
    public void destroyItem(ItemStack item, PlayerDropItemEvent e) {
        if(pathway.getItems().getItems().contains(item)) {
            e.getItemDrop().remove();
        }
    }

    @Override
    public void removeSpirituality(int remove) {
        pathway.getBeyonder().setSpirituality(pathway.getBeyonder().getSpirituality() - remove);
    }


    public void buildSpear(Location loc, Vector direc) {

        Particle.DustOptions dustRipple = new Particle.DustOptions(Color.fromBGR(0, 215, 255), .3f);

        for(int i = 0; i < 6; i++) {
            loc.subtract(direc);
        }

        int circlePoints = 10;
        double radius = 0.2;
        Location playerLoc = loc.clone();
        Vector dir = loc.clone().getDirection().normalize().multiply(0.15);
        double pitch = (playerLoc.getPitch() + 90.0F) * 0.017453292F;
        double yaw = -playerLoc.getYaw() * 0.017453292F;
        double increment = (2 * Math.PI) / circlePoints;
        for(int k = 0; k < 5; k++) {
            radius -= 0.009;
            for (int i = 0; i < circlePoints; i++) {
                double angle = i * increment;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);
                Vector vec = new Vector(x, 0, z);
                VectorUtils.rotateAroundAxisX(vec, pitch);
                VectorUtils.rotateAroundAxisY(vec, yaw);
                playerLoc.subtract(vec);
                playerLoc.getWorld().spawnParticle(Particle.REDSTONE, playerLoc.clone(), 1, 0, 0, 0, 0, dustRipple);
                playerLoc.add(vec);
            }
            playerLoc.subtract(dir);
        }

        direc.multiply(0.125);
        for(int i = 0; i < 64; i++) {
            loc.getWorld().spawnParticle(Particle.REDSTONE, loc.clone().subtract(.03, .03, .03), 30, 0.03, 0.03, 0.03, 0, dustRipple);
            loc.add(direc);
        }

        circlePoints = 20;
        radius = 0.25;
        playerLoc = loc.clone();
        dir = loc.clone().getDirection().normalize().multiply(0.15);
        pitch = (playerLoc.getPitch() + 90.0F) * 0.017453292F;
        yaw = -playerLoc.getYaw() * 0.017453292F;
        increment = (2 * Math.PI) / circlePoints;
        for(int k = 0; k < 13; k++) {
            radius -= 0.019;
            for (int i = 0; i < circlePoints; i++) {
                double angle = i * increment;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);
                Vector vec = new Vector(x, 0, z);
                VectorUtils.rotateAroundAxisX(vec, pitch);
                VectorUtils.rotateAroundAxisY(vec, yaw);
                playerLoc.add(vec);
                playerLoc.getWorld().spawnParticle(Particle.REDSTONE, playerLoc.clone().subtract(0, 0.1, 0), 1, 0, 0, 0, 0, dustRipple);
                playerLoc.subtract(vec);
            }
            playerLoc.add(dir);
        }
    }

    public double xPos(double time, double radius, double yaw){
        return Math.sin(time) * radius * Math.cos(Math.PI/180 * yaw);
    }

    public double yPos(double time, double radius){
        return Math.cos(time)*radius;
    }


}
