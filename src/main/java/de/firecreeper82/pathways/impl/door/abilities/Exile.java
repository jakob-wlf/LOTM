package de.firecreeper82.pathways.impl.door.abilities;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.VectorUtils;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.DoorItems;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class Exile extends Ability {

    public Exile(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        Vector dir = p.getEyeLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();

        for (int i = 0; i < 20; i++) {
            if (loc.getBlock().getType().isSolid())
                break;
            loc.add(dir);
        }

        loc.subtract(dir);
        loc.add(0, .4, 0);

        if (loc.getWorld() == null)
            return;

        Random random = new Random();
        Location[] locations = new Location[12];

        for (int i = 0; i < locations.length; i++) {
            locations[i] = loc.clone().add(random.nextInt(-4, 4), random.nextInt(-4, 4), random.nextInt(-4, 4));
            locations[i].setPitch(random.nextInt(45));
            locations[i].setYaw(random.nextInt(360));
        }

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                counter++;
                if (counter >= 20 * 60) {
                    cancel();
                    return;
                }

                for (Location location : locations) {
                    drawDoor(location);
                }

                for (Entity entity : loc.getWorld().getNearbyEntities(loc, 5, 5, 5)) {
                    if (entity == p)
                        continue;

                    if (entity instanceof Player player && Plugin.beyonders.containsKey(player.getUniqueId())) {
                        Beyonder beyonder = Plugin.beyonders.get(player.getUniqueId());
                        if (random.nextInt(Math.round(160f / beyonder.getPathway().getSequence().getCurrentSequence())) == 0) {
                            Location startLoc = player.getLocation();
                            Location teleportLoc = new Location(startLoc.getWorld(), 1000, 10000, 1000);
                            player.teleport(teleportLoc);

                            new BukkitRunnable() {
                                int c = 0;

                                @Override
                                public void run() {
                                    if (c >= (20 * 1.5 * Math.pow(beyonder.getPathway().getSequence().getCurrentSequence(), 1.2))) {
                                        player.teleport(startLoc);
                                        cancel();
                                        return;
                                    }
                                    c++;
                                    player.teleport(teleportLoc);
                                }
                            }.runTaskTimer(Plugin.instance, 0, 0);
                        }
                        continue;
                    }

                    if (random.nextInt(15) == 0) {
                        Location startLoc = entity.getLocation();
                        Location teleportLoc = new Location(startLoc.getWorld(), random.nextInt(1000, 2000), 10000, random.nextInt(1000, 2000));
                        entity.teleport(teleportLoc);
                        new BukkitRunnable() {
                            int c = 0;

                            @Override
                            public void run() {
                                if (c >= 20 * 30) {
                                    entity.teleport(startLoc);
                                    cancel();
                                    return;
                                }
                                c++;
                                entity.teleport(teleportLoc);
                            }
                        }.runTaskTimer(Plugin.instance, 0, 0);
                    }

                }

                if (!pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.CRIMSON_DOOR, "Exile", "450", identifier, 4, pathway.getBeyonder().getPlayer().getName());
    }

    int o = 0;
    int x = 1;
    int y = 2;

    private final int[][] shape = {
            {o, o, o, o, o, o, o, o, o, o, o, o, o, o, o, o},
            {o, o, o, o, o, o, o, x, x, o, o, o, o, o, o, o},
            {o, o, o, o, o, x, x, x, x, x, x, o, o, o, o, o},
            {o, o, o, o, x, x, x, y, y, x, x, x, o, o, o, o},
            {o, o, o, x, x, x, y, y, y, y, x, x, x, o, o, o},
            {o, o, o, x, x, y, y, y, y, y, y, x, x, o, o, o},
            {o, o, o, x, x, y, y, y, y, y, y, x, x, o, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
            {o, o, x, x, y, y, y, y, y, y, y, y, x, x, o, o},
    };

    private void drawDoor(Location loc) {

        if (loc.getWorld() == null)
            return;

        double space = 0.24;
        double defX = loc.getX() - (space * shape[0].length / 2) + space;
        double x = defX;
        double y = loc.clone().getY() + 2.8;
        double fire = -((loc.getYaw() + 180) / 60);
        final double pitch = -loc.getPitch();
        fire += (loc.getYaw() < -180 ? 3.25 : 2.985);

        for (int[] i : shape) {
            for (int j : i) {
                if (j != 0) {

                    Location target = loc.clone();
                    target.setX(x);
                    target.setY(y);

                    Vector v = target.toVector().subtract(loc.toVector());
                    Vector v2 = VectorUtils.getBackVector(loc);
                    v = VectorUtils.rotateAroundAxisY(v, fire);
                    VectorUtils.rotateAroundAxisX(v, pitch);
                    v2.setY(0).multiply(-0.5);

                    loc.add(v);
                    loc.add(v2);

                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(255, 251, 0), .5f);
                    if (j == 1)
                        dust = new Particle.DustOptions(Color.fromBGR(150, 12, 171), .6f);
                    loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, .05, .05, .05, dust);

                    loc.subtract(v2);
                    loc.subtract(v);
                }
                x += space;
            }
            y -= space;
            x = defX;
        }
    }
}
