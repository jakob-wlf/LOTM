package de.firecreeper82.pathways.impl.tyrant.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.tyrant.TyrantItems;
import jline.internal.Nullable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

public class WaterSpells extends NPCAbility {

    private Category selectedCategory = Category.LIGHT;
    private final Category[] categories = Category.values();
    private int selected = 0;

    private final boolean npc;

    public WaterSpells(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        this.npc = npc;
        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);
        if(!npc)
            p = pathway.getBeyonder().getPlayer();
    }

    enum Category {
        LIGHT("§9Aqueous Light", 7),
        BEAM("§9Water Beam", 7),
        BALL("§9Water Ball", 7),
        RAIN("§9Corrosive Rain", 5),
        WHIRL("§9Water Vortex", 5),
        SPHERE("§9Water Sphere", 5);


        private final String name;
        private final int sequence;

        Category(String name, int sequence) {
            this.name = name;
            this.sequence = sequence;
        }
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        switch (selectedCategory) {
            case LIGHT -> light(p);
            case BEAM -> beam(p, getMultiplier());
            case BALL -> waterBall(p, getMultiplier());
            case RAIN -> rain(p, null);
            case WHIRL -> vortex(p, getMultiplier());
            case SPHERE -> sphere(p, getMultiplier());
        }
    }

    private void waterBall(Entity caster, double multiplier) {
        Vector direction = caster.getLocation().getDirection().normalize().multiply(.55);
        Location loc = caster.getLocation().add(0, 1.5, 0);
        World world = loc.getWorld();

        if(world == null)
            return;

        world.playSound(loc, Sound.ENTITY_BOAT_PADDLE_WATER, 8, 1);

        new BukkitRunnable() {
            int counter = 20 * 30;
            @Override
            public void run() {

                double x = Math.cos(counter);
                double z = Math.sin(counter);
                double y = Math.sin(counter);

                for(Player p : Bukkit.getOnlinePlayers()) {
                    if(p.getWorld() != loc.getWorld() || p.getLocation().distance(loc) > 100)
                        continue;
                    if(loc.getWorld() == null)
                        return;

                    loc.getWorld().spawnParticle(Particle.WATER_WAKE, loc.getX() + x, loc.getY(), loc.getZ() + z, 15, 0.05, 0.05, 0.05, 0);
                    loc.getWorld().spawnParticle(Particle.WATER_WAKE, loc.getX() + x, loc.getY() + y, loc.getZ(), 15, 0.05, 0.05, 0.05, 0);
                    y = Math.cos(counter);
                    Util.drawParticleSphere(loc, .35, 10, null, null, 0, Particle.WATER_BUBBLE);
                    loc.getWorld().spawnParticle(Particle.WATER_WAKE, loc.getX(), loc.getY() + y, loc.getZ() + z, 15, 0.05, 0.05, 0.05, 0);
                }

                if(loc.getBlock().getType().isSolid())
                    counter = 0;

                for(Entity entity : world.getNearbyEntities(loc,  1, 1, 1)) {
                    if(entity instanceof LivingEntity livingEntity && entity != caster && entity.getType() != EntityType.ARMOR_STAND) {
                        livingEntity.damage(8.5 * multiplier);
                        counter = 0;
                    }
                }

                loc.add(direction);

                counter--;
                if(counter <= 0) {
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    private void beam(Entity caster, double multiplier) {
        if(!npc && caster.getLocation().getBlock().getType() != Material.WATER) {
            caster.sendMessage("§cYou have to be in water to use this");
            return;
        }

        Vector direction = caster.getLocation().getDirection().normalize();
        Location loc = caster.getLocation().add(0, 1.5, 0);
        World world = loc.getWorld();

        if(world == null)
            return;

        world.playSound(loc, Sound.ENTITY_BOAT_PADDLE_WATER, 8, 1);

        new BukkitRunnable() {
            int counter = 15;
            @Override
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    if(p.getWorld() != loc.getWorld() || p.getLocation().distance(loc) > 100)
                        continue;
                    p.spawnParticle(Particle.WATER_BUBBLE, loc, 15, .05, .05, .05, 0);
                }

                if(loc.getBlock().getType().isSolid())
                    counter = 0;

                for(Entity entity : world.getNearbyEntities(loc,  1, 1, 1)) {
                    if(entity instanceof LivingEntity livingEntity && entity != caster && entity.getType() != EntityType.ARMOR_STAND) {
                        livingEntity.damage(7.5 * multiplier);
                        counter = 0;
                    }
                }

                loc.add(direction);

                counter--;
                if(counter <= 0)
                    cancel();
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    private void vortex(Entity caster, double multiplier) {
        if(!npc && caster.getLocation().getBlock().getType() != Material.WATER) {
            caster.sendMessage("§cYou have to be in water to use this");
            return;
        }

        Location loc = caster.getLocation();
        World world = loc.getWorld();

        loc.add(0, -0.75, 0);

        if(world == null)
            return;

        for(int i = 0; i < 15; i++) {
            int j = i;
            new BukkitRunnable() {
                double spiralRadius = .65;

                double spiral = 0;
                double height = 0;
                double spiralX;
                double spiralZ;

                int counter = 20 * 45 - (j * 25);

                @Override
                public void run() {

                    spiralX = spiralRadius * Math.cos(spiral);
                    spiralZ = spiralRadius * Math.sin(spiral);
                    spiral += 0.75;
                    height += .025;
                    spiralRadius += .025;
                    if (height >= 3.5) {
                        height = 0;
                        spiralRadius = .65;
                    }

                    if (loc.getWorld() == null)
                        return;


                    counter--;
                    if(counter <= 0) {
                        cancel();
                        return;
                    }

                    if(j == 0) {
                        if (counter % 10 == 0)
                            Util.damageNearbyEntities(caster, loc, 6, 6, 6, 8 * multiplier);

                        for (Entity entity : loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
                            if (entity.getType() == EntityType.ARMOR_STAND || entity == caster)
                                continue;

                            Vector direction = loc.toVector().subtract(entity.getLocation().toVector()).normalize().multiply(.25);
                            entity.setVelocity(direction);
                        }
                    }

                    Util.drawParticlesForNearbyPlayers(Particle.WATER_WAKE, new Location(loc.getWorld(), spiralX + loc.getX(), height + loc.getY(), spiralZ + loc.getZ()), 15, 0.1, 0, 0.1, 0);
                    Util.drawParticlesForNearbyPlayers(Particle.WATER_BUBBLE, new Location(loc.getWorld(), spiralX + loc.getX(), height + loc.getY(), spiralZ + loc.getZ()), 15, 0.1, 0, 0.1, 0);
                }
            }.runTaskTimer(Plugin.instance, i * 25, 2);
        }
    }

    private void light(LivingEntity caster) {
        //get block player is looking at
        BlockIterator iter = new BlockIterator(caster, 9);
        Block lastBlock = iter.next();
        Block previousBlock;
        while (iter.hasNext()) {
            previousBlock = lastBlock;
            lastBlock = iter.next();
            if (lastBlock.getType().isSolid()) {
                lastBlock = previousBlock;
                break;
            }

        }
        Location loc = lastBlock.getLocation();

        loc.getBlock().setType(Material.LIGHT);
        loc.add(0.5, 0.5, 0.5);

        final Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(143, 255, 244), 1.75f);

        Random random = new Random();

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {

                if(loc.getWorld() == null)
                    return;

                counter++;
                double x = Math.cos(counter);
                double z = Math.sin(counter);
                double y = Math.sin(counter);
                if(random.nextBoolean())
                    loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + x, loc.getY(), loc.getZ() + z, 1, 0, 0, 0, 0);
                if(random.nextBoolean())
                    loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + x, loc.getY() + y, loc.getZ(), 1, 0, 0, 0, 0);
                y = Math.cos(counter);
                if(random.nextBoolean())
                    loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX(), loc.getY() + y, loc.getZ() + z, 1, 0, 0, 0, 0);

                loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 10, 0.25, 0.25, 0.25, dust);

                if (counter >= 15 * 20 && !npc) {
                    loc.getBlock().setType(Material.AIR);
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    private void rain(Entity caster, @Nullable Location loc) {
        if(loc == null) {
            Vector dir = caster.getLocation().getDirection().normalize();
            loc = caster.getLocation().add(0, 1.5, 0);
            if (loc.getWorld() == null)
                return;

            outerloop: for (int i = 0; i < 100; i++) {
                for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                    if (Util.testForValidEntity(entity, caster, true, true))
                        break outerloop;
                }

                loc.add(dir);

                if (loc.getBlock().getType().isSolid()) {
                    break;
                }
            }
        }
        loc.add(0, 8, 0);

        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(122, 255, 175), 1);

        Location finalLoc = loc;
        Random random = new Random();

        ArrayList<Block> blocks = Util.getBlocksInCircleRadius(loc.getBlock(), 12, true);

        new BukkitRunnable() {
            int counter = 20 * 30;
            @Override
            public void run() {
                Util.drawParticlesForNearbyPlayers(Particle.WATER_DROP, finalLoc.clone().subtract(0, 2.5, 0), 500, 12, 5, 12, 1);
                Util.drawParticlesForNearbyPlayers(Particle.SLIME, finalLoc.clone().subtract(0, 2.5, 0), 50, 12, 5, 12, 1);
                Util.drawDustsForNearbyPlayers(finalLoc, 30, 12, 12, 12, dust);
                Util.effectForNearbyEntities(caster, finalLoc.clone(), 12, 12, 12, new PotionEffect(PotionEffectType.POISON, 20 * 5, 4, false, false));
                Util.effectForNearbyEntities(caster, finalLoc.clone(), 12, 12, 12, new PotionEffect(PotionEffectType.WITHER, 20 * 2, 1, false, false));


                if(blocks.size() > 0) {
                    Block block = blocks.get(random.nextInt(blocks.size()));
                    if (block.getType().getHardness() > 0)
                        block.setType(Material.AIR);
                }

                counter--;
                if(counter <= 0)
                    cancel();
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    private void sphere(Entity caster, double multiplier) {
        Vector direction = caster.getLocation().getDirection().normalize().multiply(.55);
        Location loc = caster.getLocation().add(0, 1.5, 0);
        World world = loc.getWorld();

        if(world == null)
            return;

        world.playSound(loc, Sound.ENTITY_BOAT_PADDLE_WATER, 8, 1);

        new BukkitRunnable() {
            int counter = 20 * 45;
            @Override
            public void run() {

                double x = 2 * Math.cos(counter);
                double z = 2 * Math.sin(counter);
                double y = 2 * Math.sin(counter);

                for(Player p : Bukkit.getOnlinePlayers()) {
                    if(p.getWorld() != loc.getWorld() || p.getLocation().distance(loc) > 100)
                        continue;
                    if(loc.getWorld() == null)
                        return;

                    loc.getWorld().spawnParticle(Particle.WATER_WAKE, loc.getX() + x, loc.getY(), loc.getZ() + z, 15, 0.05, 0.05, 0.05, 0);
                    loc.getWorld().spawnParticle(Particle.WATER_WAKE, loc.getX() + x, loc.getY() + y, loc.getZ(), 15, 0.05, 0.05, 0.05, 0);
                    y = Math.cos(counter);
                    Util.drawParticleSphere(loc, 2, 20, null, null, 0, Particle.WATER_BUBBLE);
                    loc.getWorld().spawnParticle(Particle.WATER_WAKE, loc.getX(), loc.getY() + y, loc.getZ() + z, 15, 0.05, 0.05, 0.05, 0);
                }

                if(loc.getBlock().getType().isSolid()) {
                    counter = 0;

                    loc.getWorld().createExplosion(loc, 4);
                    for(Block block : Util.getNearbyBlocksInSphere(loc, 3, false, false, true)) {
                        if(!block.getType().isSolid())
                            block.setType(Material.WATER);
                    }
                }

                for(Entity entity : world.getNearbyEntities(loc,  2, 2, 2)) {
                    if(entity instanceof LivingEntity livingEntity && entity != caster && entity.getType() != EntityType.ARMOR_STAND) {
                        livingEntity.damage(15 * multiplier);
                        counter = 0;
                    }
                }

                loc.add(direction);

                counter--;
                if(counter <= 0) {
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
        if(multiplier < 1.5) {
            switch ((new Random()).nextInt(2)) {
                case 0 -> beam(caster, multiplier);
                case 1 -> waterBall(caster, multiplier);
            }
        }
        else {
            switch ((new Random()).nextInt(4)) {
                case 0 -> beam(caster, multiplier);
                case 1 -> waterBall(caster, multiplier);
                case 2 -> rain(caster, loc);
                case 3 -> sphere(caster, multiplier);
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return TyrantItems.createItem(Material.PRISMARINE_SHARD, "Water Spells", "40", identifier, sequence, p.getName());
    }

    @Override
    //Cycle through categories on left click
    public void leftClick() {
        selected++;
        if (selected >= categories.length || categories[selected].sequence < pathway.getSequence().getCurrentSequence())
            selected = 0;
        selectedCategory = categories[selected];
    }

    @Override
    //Display selected category
    public void onHold() {
        if (p == null)
            p = pathway.getBeyonder().getPlayer();
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5Selected Spell: §f" + selectedCategory.name));
    }
}
