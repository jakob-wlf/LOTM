package de.firecreeper82.pathways.impl.tyrant.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.tyrant.TyrantItems;
import de.firecreeper82.pathways.impl.tyrant.TyrantSequence;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class LightningStorm extends NPCAbility {

    boolean destruction;
    private final boolean npc;

    public LightningStorm(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        if(!npc)
            p = pathway.getBeyonder().getPlayer();

        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);

        this.npc = npc;
        destruction = false;
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        Vector dir = p.getLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();
        if (loc.getWorld() == null)
            return;

        outerloop:
        for (int i = 0; i < 80; i++) {
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

        loc.getWorld().setClearWeatherDuration(0);
        loc.getWorld().setStorm(true);
        loc.getWorld().setThunderDuration(120 * 60 * 20);

        useNPCAbility(loc, p, getMultiplier());
    }

    @Override
    public ItemStack getItem() {
        return TyrantItems.createItem(Material.LIGHT_BLUE_DYE, "Lightning Storm", "750", identifier, sequence, p.getName());
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
        if(loc.getWorld() == null)
            return;

        Random random = new Random();

        new BukkitRunnable() {
            int counter = 10 * 30;
            @Override
            public void run() {
                spawnLighting(loc.clone().add(random.nextInt(-25, 25), 0, random.nextInt(-25, 25)), caster, multiplier);

                counter--;
                if(counter <= 0) {
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 2);
    }

    private void spawnLighting(Location loc, Entity caster, double multiplier) {
        Integer sequence = npc ? null : pathway.getSequence().getCurrentSequence();
        TyrantSequence.spawnLighting(loc, caster, multiplier, npc, destruction, sequence);
    }

    @Override
    public void leftClick() {
        destruction = !destruction;
        p.sendMessage("§aSet destruction to: §7" + destruction);
    }
}
