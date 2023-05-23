package de.firecreeper82.pathways.impl.sun.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.sun.SunItems;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.Objects;

public class SolarFlare extends Ability {

    private final int[] spirituality;

    public SolarFlare(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);

        spirituality = new int[]{
                15000,
                30000,
                45000,
                60000,
                85000,
                100000,
                180000,
                350000,
        };
    }

    int power = 1;

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        if (pathway.getBeyonder().getSpirituality() < spirituality[power - 1])
            return;

        pathway.getSequence().removeSpirituality(spirituality[power - 1]);

        //get block player is looking at
        BlockIterator iter = new BlockIterator(p, 300);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        Location loc = lastBlock.getLocation();

        if (loc.getWorld() == null)
            return;

        new BukkitRunnable() {
            int i = 0;
            final int tempPower = Math.min(power, 3);

            @Override
            public void run() {
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(255, 251, 0), 50f);
                Util.drawSphere(loc, (int) Math.round((i * power * 1.25)), 60, dust, null, .2);

                i += tempPower * 1.25;
                if (i >= (tempPower * 1.25 * 10)) {
                    cancel();
                    loc.getWorld().createExplosion(loc, power * 10, true, true, p);
                    for (double i = 0; i < (power * 1.25 * 10); i += (power * 1.25)) {
                        loc.getWorld().createExplosion(loc.clone().add(0, 0, i), Math.round((power * .5 * 10)), true, true, p);
                        loc.getWorld().createExplosion(loc.clone().add(0, 0, -i), Math.round((power * .5 * 10)), true, true, p);
                        loc.getWorld().createExplosion(loc.clone().add(i, 0, 0), Math.round((power * .5 * 10)), true, true, p);
                        loc.getWorld().createExplosion(loc.clone().add(-i, 0, 0), Math.round((power * .5 * 10)), true, true, p);
                    }
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 2);
    }

    @Override
    public ItemStack getItem() {
        return SunItems.createItem(Material.SOUL_TORCH, "Solar Flare", "varying", identifier, 1, Objects.requireNonNull(Bukkit.getPlayer(pathway.getUuid())).getName());
    }

    @Override
    public void leftClick() {
        p = pathway.getBeyonder().getPlayer();
        power++;
        if (power > 7)
            power = 1;
        p.sendMessage("§6Set power to " + power);
    }
}
