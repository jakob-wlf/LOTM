package de.firecreeper82.handlers.mobs;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.BeyonderItems;
import de.firecreeper82.lotm.util.VectorUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

public class BeyonderMobs {


    private final HashMap<String, Entity> mobs = new HashMap<>();

    public BeyonderMobs() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Map.Entry<String, Entity> set : mobs.entrySet()) {
                    switch (set.getKey()) {
                        case "wraith" -> wraith(set.getValue());
                        case "gargoyle" -> gargoyle(set.getValue());
                        case "bane" -> bane(set.getValue());
                        case "plunderer" -> plunderer((Vex) set.getValue());
                        case "wolf" -> wolf((Wolf) set.getValue());
                        case "fog-wolf" -> fogWolf((Wolf) set.getValue());
                        case "rooster" -> rooster((Chicken) set.getValue());
                        case "divine-bird" -> divineBird((Parrot) set.getValue());
                    }

                    mobs.remove(set.getKey(), set.getValue());
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 40);
    }

    private void divineBird(Parrot parrot) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
        Random random = new Random();

        new BukkitRunnable() {
            Player target;
            @Override
            public void run() {
                if (!parrot.isValid()) {
                    cancel();
                    return;
                }

                parrot.getWorld().spawnParticle(Particle.REDSTONE, parrot.getLocation(), 5, 1, 2, 1, dust);

                if(target == null && parrot.getNearbyEntities(40, 40, 40).isEmpty())
                    return;

                if(target == null) {
                    for(Entity e : parrot.getNearbyEntities(40, 40, 40)) {
                        if(!(e instanceof Player))
                            continue;
                        target = (Player) e;
                    }
                }

                if(target == null)
                    return;

                parrot.setTarget(target);

                if(random.nextInt(120) == 0) {
                    switch (random.nextInt(4)) {
                        case 0, 1, 2 -> holyLightSummoning(parrot, target.getLocation(), 15);
                        case 3 -> flaringSun(parrot, target.getLocation(), 5);
                    }
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    private void flaringSun(Parrot parrot, Location loc, double damage) {
        ArrayList<Block> airBlocks = new ArrayList<>();

        int burnRadius = 7;
        for(int i = 3; i > -8; i--) {
            for (int x = -burnRadius; x <= burnRadius; x++) {
                for (int z = -burnRadius; z <= burnRadius; z++) {
                    if( (x*x) + (z*z) <= Math.pow(burnRadius, 2)) {
                        Block block = parrot.getWorld().getBlockAt((int) loc.getX() + x, (int) loc.getY() + i, (int) loc.getZ() + z);
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
        }

        Location sphereLoc = loc.clone();
        new BukkitRunnable() {
            int counter = 0;
            public final double sphereRadius = 3.5;
            @Override
            public void run() {
                counter++;

                //Spawn particles
                Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.FLAME, loc, 50, 1, 1, 1, 0);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc, 70, 1, 1, 1, 0);
                for (double i = 0; i <= Math.PI; i += Math.PI / 15) {
                    double radius = Math.sin(i) * sphereRadius;
                    double y = Math.cos(i) * sphereRadius;
                    for (double a = 0; a < Math.PI * 2; a += Math.PI / 15) {
                        double x = Math.cos(a) * radius;
                        double z = Math.sin(a) * radius;
                        sphereLoc.add(x, y, z);
                        Particle.DustOptions dustSphere = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
                        Objects.requireNonNull(sphereLoc.getWorld()).spawnParticle(Particle.REDSTONE, sphereLoc, 1, 0.25, 0.25, 0.25, 0, dustSphere);
                        sphereLoc.getWorld().spawnParticle(Particle.FLAME, sphereLoc, 1, 0.25, 0.25, 0.25, 0);
                        if(counter == 1 && !sphereLoc.getBlock().getType().isSolid()) {
                            airBlocks.add(sphereLoc.getBlock());
                            sphereLoc.getBlock().setType(Material.LIGHT);
                        }
                        sphereLoc.subtract(x, y, z);
                    }
                }

                //damage nearby entities
                ArrayList<Entity> nearbyEntities = (ArrayList<Entity>) loc.getWorld().getNearbyEntities(loc, 4, 4, 4);
                for(Entity entity : nearbyEntities) {
                    if(entity instanceof LivingEntity livingEntity) {
                        if (livingEntity.getCategory() == EntityCategory.UNDEAD) {
                            ((Damageable) entity).damage(damage * 1.5, parrot);
                            livingEntity.setFireTicks(50 * 20);
                        } else if(entity != parrot) {
                            livingEntity.setFireTicks(50 * 20);
                            ((Damageable) entity).damage(damage, parrot);
                        }
                    }
                }

                if(counter >= 20 * 20) {
                    for(Block b : airBlocks) {
                        b.setType(Material.AIR);
                    }
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    private void rooster(Chicken chicken) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1f);
        Random random = new Random();

        final HashMap<Long, Location> burnedLocations = new HashMap<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!chicken.isValid()) {
                    cancel();
                    return;
                }

                chicken.getWorld().spawnParticle(Particle.REDSTONE, chicken.getLocation(), 5, 1, 2, 1, dust);

                burnedLocations.put(System.currentTimeMillis(), chicken.getLocation());

                for(Location loc : burnedLocations.values()) {
                    Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.FLAME, loc, 2, .05, .05, .05, 0);
                    if(loc.getWorld().getNearbyEntities(loc, 1, 1, 1).isEmpty())
                        continue;
                    for(Entity entity : loc.getWorld().getNearbyEntities(loc, .5, .5, .5)) {
                        if(!(entity instanceof Damageable damageable) || damageable == chicken)
                            continue;
                        damageable.damage(6, chicken);
                    }
                }

                try {
                    for(Long l : burnedLocations.keySet()) {
                        long temp = l;
                        if((temp + (1000 * 5)) < System.currentTimeMillis())
                            burnedLocations.remove(l);
                    }
                }
                catch(Exception ignored) {}

                if(random.nextInt(40) == 0) {
                    chicken.getWorld().spawnParticle(Particle.REDSTONE, chicken.getLocation(), 200, 2, 2, 2, dust);
                    chicken.getWorld().spawnParticle(Particle.END_ROD, chicken.getLocation(), 350, 0.5, 0.5, 0.5, .25);
                    for(Entity entity : chicken.getNearbyEntities(5, 5, 5)) {
                        if(entity instanceof Damageable damageable)
                            damageable.damage(16, chicken);
                    }
                }

                if(random.nextInt(20 * 60 * 3) == 0) {
                    chicken.getWorld().dropItem(chicken.getLocation(), BeyonderItems.getRoosterComb());
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    private void fogWolf(Wolf wolf) {
        new BukkitRunnable() {
            Player target;
            @Override
            public void run() {
                if(!wolf.isValid()) {
                    cancel();
                }

                wolf.setCollarColor(DyeColor.RED);
                wolf.setTamed(false);
                wolf.setAngry(true);
                wolf.setInterested(false);

                Objects.requireNonNull(wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(Objects.requireNonNull(wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).getBaseValue() * 10);

                if(target == null && wolf.getNearbyEntities(40, 40, 40).isEmpty())
                    return;

                if(target == null) {
                    for(Entity e : wolf.getNearbyEntities(40, 40, 40)) {
                        if(!(e instanceof Player))
                            continue;
                        target = (Player) e;
                    }
                }

                if(target == null)
                    return;

                wolf.setTarget(target);

            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    private void wolf(Wolf wolf) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(216, 216, 216), 50f);

        new BukkitRunnable() {
            Player target;
            @Override
            public void run() {
                if(!wolf.isValid()) {
                    cancel();
                }

                wolf.setCollarColor(DyeColor.RED);
                wolf.setTamed(false);
                wolf.setAngry(true);
                wolf.setInterested(false);

                Objects.requireNonNull(wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(Objects.requireNonNull(wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).getBaseValue() * 10);
                Objects.requireNonNull(wolf.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(Objects.requireNonNull(wolf.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).getBaseValue() * 6);

                wolf.getWorld().spawnParticle(Particle.REDSTONE, wolf.getLocation(), 10, 1, 1, 1, dust);

                if(target == null && wolf.getNearbyEntities(40, 40, 40).isEmpty())
                    return;

                if(target == null) {
                    for(Entity e : wolf.getNearbyEntities(40, 40, 40)) {
                        if(!(e instanceof Player))
                            continue;
                        target = (Player) e;
                    }
                }

                if(target == null)
                    return;

                wolf.setTarget(target);

            }
        }.runTaskTimer(Plugin.instance, 0, 5);
    }

    public void addMob(@NonNull Entity mob, @NonNull String id) {
        mobs.put(id, mob);
    }

    private void wraith(Entity entity) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!entity.isValid()) {
                    cancel();
                    return;
                }

                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256)), .6f);
                entity.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation(), 50, .5, .5, .5, dust);
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    private void plunderer(Vex vex) {
        Random random = new Random();
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(154, 0, 194), 1.25f);

        new BukkitRunnable() {
            Player target;
            @Override
            public void run() {
                if(!vex.isValid()) {
                    cancel();
                }

                if(target == null && vex.getNearbyEntities(40, 40, 40).isEmpty())
                    return;

                if(target == null) {
                    for(Entity e : vex.getNearbyEntities(40, 40, 40)) {
                        if(!(e instanceof Player))
                            continue;
                        target = (Player) e;
                    }
                }

                if(target == null)
                    return;

                vex.setTarget(target);

                if(random.nextInt(25) == 0) {
                    switch (random.nextInt(10)) {
                        case 0, 1, 2 -> {
                            for(int i = 0; i < random.nextInt(5) + 1; i++) {
                                Location spawnLoc = vex.getLocation().add(random.nextInt(6) - 3, random.nextInt(6) - 3, random.nextInt(6) - 3);
                                vex.getWorld().spawnEntity(spawnLoc, EntityType.VEX);
                            }
                        }

                        case 3, 4, 5, 6, 7, 8, 9 -> {
                            if(target == null)
                                break;


                            Location loc = vex.getLocation().clone();
                            Vector vector = (target.getLocation().toVector().subtract(loc.toVector())).normalize();
                            final Player playerTarget = target;
                            new BukkitRunnable() {
                                int counter = 0;

                                @Override
                                public void run() {
                                    vex.getWorld().spawnParticle(Particle.REDSTONE, loc, 4, .05, .05, .05, dust);
                                    loc.add(vector);

                                    for(Entity e : vex.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                                        if(e != playerTarget)
                                            continue;
                                        playerTarget.damage(4, vex);
                                        playerTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 2));
                                        playerTarget.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 2));
                                    }

                                    counter++;
                                    if(counter > 50)
                                        cancel();
                                }
                            }.runTaskTimer(Plugin.instance, 0, 4);
                        }
                    }
                }


            }
        }.runTaskTimer(Plugin.instance, 0, 0);

        drawSpiral(vex, 0, 0);
        drawSpiral(vex, .25, 10);

    }

    private void drawSpiral(Entity entity, double heightOffset, int delay) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(154, 0, 194), 1.25f);
        new BukkitRunnable() {
            final double spiralRadius = 1;

            double spiral = 0;
            double height = heightOffset;
            double spiralX;
            double spiralZ;
            @Override
            public void run() {
                if(!entity.isValid()) {
                    cancel();
                }

                Location entityLoc = entity.getLocation().clone();
                entityLoc.add(0, 0.75, 0);

                spiralX = spiralRadius * Math.cos(spiral);
                spiralZ = spiralRadius * Math.sin(spiral);
                spiral += 0.25;
                height += .05;
                if(height >= 2.5)
                    height = 0;
                if(entityLoc.getWorld() != null)
                    entityLoc.getWorld().spawnParticle(Particle.REDSTONE, spiralX + entityLoc.getX(), height + entityLoc.getY(), spiralZ + entityLoc.getZ(), 5, dust);
            }
        }.runTaskTimer(Plugin.instance, delay, 0);
    }

    private void bane(Entity entity) {
        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(200, 60, 0), 2f);
        Random random = new Random();

        new BukkitRunnable() {
            Player target;
            @Override
            public void run() {
                if(!entity.isValid()) {
                    cancel();
                    return;
                }

                entity.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation(), 60, 1, 2, 1, dust);

                if(target == null && entity.getNearbyEntities(40, 40, 40).isEmpty())
                    return;

                if(target == null) {
                    for(Entity e : entity.getNearbyEntities(40, 40, 40)) {
                        if(!(e instanceof Player))
                            return;
                        target = (Player) e;
                    }
                }

                if(target == null)
                    return;

                ((Allay) entity).setTarget(target);

                if(random.nextInt(15) == 0)  {
                    Location loc = entity.getLocation().clone();
                    Vector vector = (target.getLocation().toVector().subtract(loc.toVector())).normalize();
                    new BukkitRunnable() {
                        int counter = 0;
                        @Override
                        public void run() {
                            entity.getWorld().spawnParticle(Particle.REDSTONE, loc, 45, .5, .5, .5, dust);
                            loc.add(vector);

                            for(Entity e : entity.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                                if(e != target)
                                    continue;
                                target.damage(12, entity);
                            }

                            counter++;
                            if(counter > 50)
                                cancel();
                        }
                    }.runTaskTimer(Plugin.instance, 0, 1);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 2);
    }

    private void gargoyle(Entity entity) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!entity.isValid()) {
                    cancel();
                    return;
                }

                for(Entity e : entity.getNearbyEntities(20, 20, 20)) {
                    if(!(e instanceof Player))
                        continue;

                    ((Mob) entity).setTarget((Player) e);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 10);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!entity.isValid()) {
                    cancel();
                    return;
                }

                drawWings(entity.getLocation().add(0, .5, 0));
            }
        }.runTaskTimer(Plugin.instance, 0, 2);
    }


    private final boolean x = true;
    private final boolean o = false;
    private final boolean[][] shape = {
            {o, o, o, x, o, o, o, o, o, o, o, o, x, o, o, o},
            {o, o, x, x, o, o, o, o, o, o, o, o, x, x, o, o},
            {o, o, x, x, o, o, o, o, o, o, o, o, x, x, o, o},
            {o, x, x, x, x, o, o, o, o, o, o, x, x, x, x, o},
            {o, x, x, x, x, o, o, o, o, o, o, x, x, x, x, o},
            {o, o, x, x, x, x, o, o, o, o, x, x, x, x, o, o},
            {o, o, x, x, x, x, x, o, o, x, x, x, x, x, o, o},
            {o, o, o, x, x, x, x, x, x, x, x, x, x, o, o, o},
            {o, o, o, o, o, x, x, x, x, x, x, o, o, o, o, o},
            {o, o, o, o, o, x, x, x, x, x, x, o, o, o, o, o},
            {o, o, o, o, x, x, x, o, o, x, x, x, o, o, o, o},
            {o, o, o, x, x, x, x, o, o, x, x, x, x, o, o, o},
            {o, o, o, x, x, x, o, o, o, o, x, x, x, o, o, o},
            {o, o, o, x, x, o, o, o, o, o, o, x, x, o, o, o},
            {o, o, o, x, x, o, o, o, o, o, o, x, x, o, o, o},
    };

    private void drawWings(Location loc) {
        double space = 0.24;
        double defX = loc.getX() - (space * shape[0].length / 2) + space;
        double x = defX;
        double y = loc.clone().getY() + 2.8;
        double fire = -((loc.getYaw() + 180) / 60);
        fire += (loc.getYaw() < -180 ? 3.25 : 2.985);

        for (boolean[] booleans : shape) {
            for (boolean aBoolean : booleans) {
                if (aBoolean) {

                    Location target = loc.clone();
                    target.setX(x);
                    target.setY(y);

                    Vector v = target.toVector().subtract(loc.toVector());
                    Vector v2 = VectorUtils.getBackVector(loc);
                    v = VectorUtils.rotateAroundAxisY(v, fire);
                    v2.setY(0).multiply(-0.5);

                    loc.add(v);
                    loc.add(v2);

                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(50, 50, 50), .6f);
                    for (int k = 0; k < 3; k++)
                        Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.REDSTONE, loc, 3, 0.02, 0.02, 0.02, dust);
                    loc.subtract(v2);
                    loc.subtract(v);
                }
                x += space;
            }
            y -= space;
            x = defX;
        }
    }

    private void holyLightSummoning(Entity caster, Location loc, double damage) {
        loc.add(0, 14, 0);

        //Runnable
        final Material[] lastMaterial = {loc.getBlock().getType()};
        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                counter++;

                //Particles
                Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.END_ROD, loc.getX() + 3.2, loc.getY(), loc.getZ(), 6, 0.1, 0, 0.1, 0);
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

                if((lastMaterial[0].isSolid() && counter >= 12) || counter >= 200) {
                    loc.getBlock().setType(lastMaterial[0]);
                    counter = 0;
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
                        if(entity instanceof LivingEntity livingEntity) {
                            if (livingEntity.getCategory() == EntityCategory.UNDEAD) {
                                ((Damageable) entity).damage(damage * 1.5, caster);
                            } else {
                                if(entity != caster)
                                    ((Damageable) entity).damage(damage, caster);
                            }
                        }
                    }

                    //Particles on ground
                    loc.add(0, 1, 0);
                    Particle.DustOptions dustRipple = new Particle.DustOptions(Color.fromBGR(0, 215, 255), 1.5f);
                    new BukkitRunnable() {
                        double radius = 1.8;
                        int factor = 0;
                        @Override
                        public void run() {
                            radius = radius + 0.75;
                            for(int i = 0; i < 100; i++) {
                                factor++;
                                double x = radius * Math.cos(factor);
                                double z = radius * Math.sin(factor);
                                loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + x, loc.getY(), loc.getZ() + z, 1, 0, 0, 0, 0);
                                loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc.getX() + x, loc.getY(), loc.getZ() + z, 2, 0.1, 0, 0.1, 0.15);
                                loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX() + x + 0.2, loc.getY(), loc.getZ() + z + 0.2, 3, dustRipple);
                            }

                            if(radius >= 9) {
                                cancel();
                            }
                        }
                    }.runTaskTimer(Plugin.instance, 0, 1);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }
}
