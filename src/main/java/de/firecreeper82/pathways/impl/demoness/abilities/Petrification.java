package de.firecreeper82.pathways.impl.demoness.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.demoness.DemonessItems;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Petrification extends NPCAbility {

    private final ArrayList<Entity> cooldownEntities;

    private final boolean npc;

    public Petrification(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);

        this.npc = npc;

        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);

        cooldownEntities = new ArrayList<>();
    }

    @Override
    public void useNPCAbility(Location targetLoc, Entity caster, double multiplier) {
        Vector dir = caster.getLocation().getDirection().normalize();
        Location loc = caster.getLocation().add(0, 1.5, 0);
        if (loc.getWorld() == null)
            return;

        LivingEntity target = null;

        outerloop:
        for (int i = 0; i < 50; i++) {
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if ((!(entity instanceof Mob) && !(entity instanceof Player)) || entity == caster)
                    continue;
                target = (LivingEntity) entity;
                break outerloop;
            }

            loc.add(dir);

            if (loc.getBlock().getType().isSolid()) {
                petrifyLoc(loc.clone().subtract(dir));
                break;
            }
        }

        if (target == null) {
            return;
        }

        LivingEntity finalTarget = target;

        if(!npc) {
            if (cooldownEntities.contains(finalTarget)) {
                caster.sendMessage("§cYou can't petrify that entity again yet!");
                return;
            }
            cooldownEntities.add(finalTarget);
        }

        HashMap<Block, Material> blocks = new HashMap<>();
        final Location eLoc = finalTarget.getLocation();

        new BukkitRunnable() {
            int counter = 120 * 20;
            boolean cancelled = false;

            @Override
            public void run() {
                counter--;

                if (counter <= 0) {
                    cancelled = true;
                }

                if (!finalTarget.isValid()) {
                    if(!npc)
                        cooldownEntities.remove(finalTarget);
                    cancelled = true;
                }

                if (eLoc.distance(finalTarget.getLocation()) > 3) {
                    cancelled = true;
                }

                if (cancelled) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(!npc)
                                cooldownEntities.remove(finalTarget);
                        }
                    }.runTaskLater(Plugin.instance, 20 * 20);
                    cancel();
                    return;
                }

                for (Map.Entry<Block, Material> entry : blocks.entrySet()) {
                    entry.getKey().setType(entry.getValue());
                }

                blocks.clear();

                finalTarget.setVelocity(new Vector(0, 0, 0));

                if (counter % 10 == 0) {
                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(100, 100, 100), 2f);
                    finalTarget.getWorld().spawnParticle(Particle.REDSTONE, finalTarget.getEyeLocation().clone().subtract(0, .5, 0), 50, .5, 1, .5, dust);
                }

                if (counter % 20 == 0) {
                    finalTarget.damage(25, p);
                }

                finalTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 120));

                for (double x = -finalTarget.getWidth() + .5; x < finalTarget.getWidth() + .5; x++) {
                    for (double z = -finalTarget.getWidth() + .5; z < finalTarget.getWidth() + .5; z++) {
                        for (int i = 0; i < finalTarget.getHeight(); i++) {
                            blocks.put(finalTarget.getLocation().clone().add(x, i, z).getBlock(), finalTarget.getLocation().clone().add(0, i, 0).getBlock().getType());
                        }
                    }
                }

                for (Block block : blocks.keySet()) {
                    block.setType(Material.STONE);
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        useNPCAbility(p.getEyeLocation(), p, getMultiplier());
    }

    private void petrifyLoc(Location loc) {

        ArrayList<Block> blocks = Util.getNearbyBlocksInSphere(loc, 6, false, true, true);
        for (Block block : blocks) {
            if (block.getType().getHardness() < 0 || !block.getType().isSolid())
                continue;
            block.setType(Material.STONE);
        }
    }

    @Override
    public ItemStack getItem() {
        return DemonessItems.createItem(Material.ENDER_EYE, "Petrification", "1500", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
