package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.VectorUtils;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class AirBullet extends Ability {

    public AirBullet(int identifier, Pathway pathway) {
        super(identifier, pathway);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        double multiplier = getMultiplier();

        new BukkitRunnable() {
            final int circlePoints = 20;
            final double radius = 0.25;
            final Location loc = p.getEyeLocation();
            final World world = loc.getWorld();
            final Vector dir = p.getLocation().getDirection().normalize();
            final double pitch = (loc.getPitch() + 90.0F) * 0.017453292F;
            final double yaw = -loc.getYaw() * 0.017453292F;
            final double increment = (2 * Math.PI) / circlePoints;
            double circlePointOffset = 0;
            int counter = 0;
            @Override
            public void run() {
                for (int i = 0; i < circlePoints; i++) {
                    double angle = i * increment + circlePointOffset;
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    Vector vec = new Vector(x, 0, z);
                    VectorUtils.rotateAroundAxisX(vec, pitch);
                    VectorUtils.rotateAroundAxisY(vec, yaw);
                    loc.add(vec);
                    assert world != null;
                    world.spawnParticle(Particle.SPELL, loc, 0); // Reminder to self - the "data" option for a (particle, location, data) is speed, not count!!
                    loc.subtract(vec);
                }
                circlePointOffset += increment / 3;
                if (circlePointOffset >= increment) {
                    circlePointOffset = 0;
                }
                loc.add(dir);

                if(!world.getNearbyEntities(loc, 5, 5, 5).isEmpty()) {
                    for(Entity entity : world.getNearbyEntities(loc, 5, 5, 5)) {
                        Vector v1 = new Vector(
                                loc.getX() + 0.25,
                                loc.getY() + 0.25,
                                loc.getZ() + 0.25
                        );
                        Vector v2 = new Vector(
                                loc.getX() - 0.25,
                                loc.getY() - 0.25,
                                loc.getZ() - 0.25
                        );
                        if(entity.getBoundingBox().overlaps(v1, v2) && entity instanceof Damageable && entity != p) {
                            ((Damageable) entity).damage(7 * multiplier, p);
                            cancel();
                            return;
                        }
                    }
                }

                counter++;

                if(loc.getBlock().getType().isSolid() || counter >= 50) {
                    cancel();
                }
            }

        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.GHAST_TEAR, "Air Bullet", "30", identifier, 7, pathway.getBeyonder().getPlayer().getName());
    }
}
