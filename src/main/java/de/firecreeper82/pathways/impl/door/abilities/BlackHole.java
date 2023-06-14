package de.firecreeper82.pathways.impl.door.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.DoorItems;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

public class BlackHole extends NPCAbility {

    private final boolean npc;

    public BlackHole(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);

        this.npc = npc;

        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);


    }

    @Override
    public void useNPCAbility(Location target, Entity caster, double multiplier) {
        Vector dir = caster.getLocation().getDirection().normalize();
        Location loc = caster.getLocation().add(0, 1.5, 0);

        for (int i = 0; i < 18; i++) {
            if (loc.getBlock().getType().isSolid())
                break;
            loc.add(dir);
        }

        loc.subtract(dir.multiply(2));

        if (loc.getWorld() == null)
            return;

        Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(0, 0, 0), 2f);
        Random random = new Random();

        int[] npcCounter = {20 * 25};

        new BukkitRunnable() {
            ArrayList<Block> blocks = Util.getNearbyBlocksInSphere(loc.getBlock().getLocation(), 32, false, true, true);

            int counter = 0;

            @Override
            public void run() {

                if (!npc && !pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    cancel();
                    return;
                }

                if(npc) {
                    npcCounter[0]--;
                    if(npcCounter[0] <= 0) {
                        cancel();
                        return;
                    }
                }

                Util.drawSphere(loc, 1, 20, dust, null, 0);

                counter++;

                if (counter >= 3 * 20) {
                    counter = 0;
                    blocks = Util.getNearbyBlocksInSphere(loc.getBlock().getLocation(), 32, false, true, true);
                }

                for (int i = 0; i < 5; i++) {
                    if (blocks.isEmpty())
                        continue;
                    Block b = blocks.get(random.nextInt(blocks.size()));

                    Material blockMaterial = b.getType();
                    b.setType(Material.AIR);

                    if (blockMaterial == Material.WATER || blockMaterial == Material.LAVA)
                        continue;

                    FallingBlock fallingBlock = b.getWorld().spawnFallingBlock(b.getLocation().clone().add(0, 1, 0), blockMaterial.createBlockData());
                    fallingBlock.setGravity(false);
                    fallingBlock.setDropItem(false);

                    if (fallingBlock.getBlockData().getMaterial() == Material.WATER) {
                        fallingBlock.remove();
                        continue;
                    }

                    Vector dir = loc.clone().toVector().subtract(fallingBlock.getLocation().toVector()).normalize().multiply(.55);
                    fallingBlock.setVelocity(dir);

                    new BukkitRunnable() {
                        int life = 20 * 6;

                        @Override
                        public void run() {
                            if (!fallingBlock.isValid()) {
                                cancel();
                                return;
                            }

                            life--;

                            if (life <= 0 || fallingBlock.getBlockData().getMaterial() == Material.WATER) {
                                fallingBlock.remove();
                                cancel();
                                return;
                            }

                            Vector direction = loc.clone().toVector().subtract(fallingBlock.getLocation().toVector()).normalize().multiply(.55);
                            fallingBlock.setVelocity(direction);

                            if (!npc && !pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                                fallingBlock.remove();
                            }
                        }
                    }.runTaskTimer(Plugin.instance, 0, 0);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!npc && !pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    cancel();
                    return;
                }

                if(npc) {
                    npcCounter[0]--;
                    if(npcCounter[0] <= 0) {
                        cancel();
                        return;
                    }
                }

                if (loc.getWorld() == null)
                    return;

                for (Entity entity : loc.getWorld().getNearbyEntities(loc, 30, 30, 30)) {
                    if (entity == caster)
                        continue;

                    Vector dir = loc.clone().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(.5);
                    entity.setVelocity(dir);
                }

                for (Entity entity : loc.getWorld().getNearbyEntities(loc, 2, 2, 2)) {
                    if (!(entity instanceof LivingEntity livingEntity)) {
                        if (!(entity instanceof Item))
                            entity.remove();
                        continue;
                    }


                    livingEntity.damage(8 * multiplier, caster);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        useNPCAbility(p.getEyeLocation(), p, getMultiplier() );
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.ENDERMAN_SPAWN_EGG, "Black Hole", "10000", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
