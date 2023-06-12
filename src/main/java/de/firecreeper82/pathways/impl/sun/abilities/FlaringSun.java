package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.sun.SunItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class FlaringSun extends NPCAbility {

    private boolean npc;

    public FlaringSun(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);

        this.npc = npc;
    }

    private ArrayList<Block> airBlocks;

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {

        airBlocks = new ArrayList<>();

        int burnRadius = 10;
        for (int i = 3; i > -8; i--) {
            for (int x = -burnRadius; x <= burnRadius; x++) {
                for (int z = -burnRadius; z <= burnRadius; z++) {
                    if ((x * x) + (z * z) <= Math.pow(burnRadius, 2)) {
                        Block block = caster.getWorld().getBlockAt((int) loc.getX() + x, (int) loc.getY() + i, (int) loc.getZ() + z);
                        if (block.getType() == Material.DIRT || block.getType() == Material.DIRT_PATH || block.getType() == Material.COARSE_DIRT || block.getType() == Material.ROOTED_DIRT || block.getType() == Material.GRASS_BLOCK)
                            block.setType(Material.NETHERRACK);
                        if (block.getType() == Material.STONE || block.getType() == Material.COBBLESTONE || block.getType() == Material.DIORITE || block.getType() == Material.ANDESITE || block.getType() == Material.GRANITE || block.getType() == Material.DEEPSLATE || block.getType() == Material.TUFF || block.getType() == Material.CALCITE || block.getType() == Material.GRAVEL)
                            block.setType(Material.BASALT);
                        if (block.getType() == Material.WATER)
                            block.setType(Material.AIR);
                        if (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR) {
                            Random rand = new Random();
                            if (rand.nextInt(4) == 0) {
                                block.setType(Material.FIRE);
                            }
                        }
                        if (block.getType() == Material.SAND || block.getType() == Material.RED_SAND)
                            block.setType(Material.GLASS);
                    }
                }
            }
        }

        Location sphereLoc = loc.clone();
        new BukkitRunnable() {
            int counter = 0;
            public final double sphereRadius = 5;

            @Override
            public void run() {
                counter++;

                //Spawn particles
                Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.FLAME, loc, 50, 2, 2, 2, 0);
                loc.getWorld().spawnParticle(Particle.END_ROD, loc, 70, 2, 2, 2, 0);
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
                        if (counter == 1 && !sphereLoc.getBlock().getType().isSolid()) {
                            airBlocks.add(sphereLoc.getBlock());
                            sphereLoc.getBlock().setType(Material.LIGHT);
                        }
                        sphereLoc.subtract(x, y, z);
                    }
                }

                //damage nearby entities
                ArrayList<Entity> nearbyEntities = (ArrayList<Entity>) loc.getWorld().getNearbyEntities(loc, 10, 10, 10);
                for (Entity entity : nearbyEntities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        if (livingEntity.getCategory() == EntityCategory.UNDEAD) {
                            ((Damageable) entity).damage(7 * multiplier, caster);
                            livingEntity.setFireTicks(50 * 20);
                        } else if (entity != caster) {
                            livingEntity.setFireTicks(50 * 20);
                            ((Damageable) entity).damage(3 * multiplier, caster);
                        }
                    }
                }

                if (counter >= 20 * 20) {
                    for (Block b : airBlocks) {
                        b.setType(Material.AIR);
                    }
                    cancel();
                    if(!npc)
                        pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    @Override
    public void useAbility() {
        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        p = pathway.getBeyonder().getPlayer();

        double multiplier = getMultiplier();

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

        useNPCAbility(loc, p, multiplier);
    }

    @Override
    public ItemStack getItem() {
        return SunItems.createItem(Material.SUNFLOWER, "Flaring Sun", "200", identifier, 4, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }
}