package de.firecreeper82.pathways.impl.tyrant.abilities;

import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.tyrant.TyrantItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Lightning extends NPCAbility {

    boolean destruction;

    public Lightning(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        p = pathway.getBeyonder().getPlayer();

        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);

        destruction = true;
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        Vector dir = p.getLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();
        if (loc.getWorld() == null)
            return;

        outerloop:
        for (int i = 0; i < 50; i++) {
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if (entity.getType() == EntityType.ARMOR_STAND || entity == p)
                    continue;
                break outerloop;
            }

            loc.add(dir);

            if (loc.getBlock().getType().isSolid()) {
                break;
            }
        }

        useNPCAbility(loc, p, getMultiplier());
    }

    @Override
    public ItemStack getItem() {
        return TyrantItems.createItem(Material.LIGHT_BLUE_DYE, "Lightning", "50", identifier, sequence, p.getName());
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
        if(loc.getWorld() == null)
            return;

        Location particleLoc = loc.clone().add(0, 50, 0);

        Random random = new Random();

        final Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(143, 255, 244), 1.8f);

        ArrayList<Double> randoms1 = new ArrayList<>();
        for(int i = 0; i < 50; i++) {
            randoms1.add(i, random.nextDouble(-.5, .5));
        }

        ArrayList<Double> randoms2 = new ArrayList<>();
        for(int i = 0; i < 50; i++) {
            randoms2.add(i, random.nextDouble(-.5, .5));
        }

        for(int j = 0; j < 12; j++) {
            final Particle.DustOptions dust1 = new Particle.DustOptions(Color.fromRGB(143, 255, 244), random.nextFloat(.7f, 1.8f));

            int height = random.nextInt(8, 46);

            Location hitLoc = particleLoc.clone();
            for(int i = 0; i < height; i++) {
                hitLoc.add(randoms1.get(i), -1, randoms2.get(i));
            }
            Location loc1 = particleLoc.clone().add(random.nextInt(-15, 15), random.nextInt(8) * -1, random.nextInt(-15, 15));
            double distance = loc.distance(hitLoc);
            Vector vector1 = hitLoc.toVector().subtract(loc1.toVector()).normalize().multiply(distance);

            for (int i = height; i > 0; i--) {
                Util.drawDustsForNearbyPlayers(loc1, 5, 0.15, 0.15, 0.15, dust1);
                loc1.add(vector1.clone().multiply((1.0 / distance)));
            }
        }

        for(int i = 0; i < 50; i++) {
            Util.drawDustsForNearbyPlayers(particleLoc.add(0, -1, 0), 20, 0, 0, 0, dust);
            particleLoc.add(randoms1.get(i), 0, randoms2.get(i));
        }

        for(Entity entity : loc.getWorld().getNearbyEntities(particleLoc, 4, 2, 4)) {
            if(Util.testForValidEntity(entity, caster, true, true)) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.damage(18 * multiplier, caster);
                livingEntity.setFireTicks(20 * 5);
            }

            Util.drawParticlesForNearbyPlayers(Particle.ELECTRIC_SPARK, entity.getLocation(), 100, 0.1, 0.1, 0.1, .75);
        }

        Material[] burnMaterials = {
                Material.GRASS_BLOCK,
                Material.SAND,
                Material.GRAVEL,
                Material.DIRT,
                Material.DIRT_PATH,
                Material.COARSE_DIRT,
                Material.STONE,
                Material.OAK_LOG,
                Material.JUNGLE_LOG,
                Material.ACACIA_LOG,
                Material.DARK_OAK_LOG,
                Material.BIRCH_LOG,
                Material.SPRUCE_LOG,
                Material.MANGROVE_LOG

        };

        loc.getWorld().playSound(particleLoc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 2, 1);

        if(destruction) {
            loc.getWorld().createExplosion(particleLoc, 5, true);

            ArrayList<Block> blocks = Util.getBlocksInCircleRadius(particleLoc.getBlock(), 6, true);
            for (Block block : blocks) {
                if (random.nextInt(3) == 0) {
                    Block fire = block.getLocation().add(0, 1, 0).getBlock();
                    if (!fire.getType().isSolid())
                        fire.setType(Material.FIRE);
                }

                if (Arrays.asList(burnMaterials).contains(block.getType())) {
                    if (random.nextInt(3) != 0)
                        block.setType(Material.BASALT);
                }
            }
        }
    }

    @Override
    public void leftClick() {
        destruction = !destruction;
        p.sendMessage("§aSet destruction to: §7" + destruction);
    }
}
