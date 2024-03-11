package dev.ua.ikeepcalm.mystical.pathways.tyrant.abilities;

import dev.ua.ikeepcalm.LordOfTheMinecraft;
import dev.ua.ikeepcalm.mystical.Items;
import dev.ua.ikeepcalm.mystical.NpcAbility;
import dev.ua.ikeepcalm.mystical.Pathway;
import dev.ua.ikeepcalm.mystical.pathways.tyrant.TyrantItems;
import dev.ua.ikeepcalm.mystical.pathways.tyrant.TyrantSequence;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class LightningStorm extends NpcAbility {

    boolean destruction;
    private final boolean npc;

    public LightningStorm(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        if (!npc)
            p = pathway.getBeyonder().getPlayer();

        if (!npc)
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
        if (loc.getWorld() == null)
            return;

        Random random = new Random();

        new BukkitRunnable() {
            int counter = 10 * 30;

            @Override
            public void run() {
                spawnLighting(loc.clone().add(random.nextInt(-25, 25), 0, random.nextInt(-25, 25)), caster, multiplier);

                counter--;
                if (counter <= 0) {
                    cancel();
                }
            }
        }.runTaskTimer(LordOfTheMinecraft.instance, 0, 2);
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
