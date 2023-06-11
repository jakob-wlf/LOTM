package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.VectorUtils;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class AirBullet extends Ability {

    private static HashMap<Integer, double[]> valuesForSequence;

    private int sequencePower;
    private boolean wasAdjustedOnce;

    public AirBullet(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);

        valuesForSequence = new HashMap<>();
        valuesForSequence.put(7, new double[]{0.25, 20, 0, 1});
        valuesForSequence.put(6, new double[]{0.35, 40, 0, 2.5});
        valuesForSequence.put(5, new double[]{0.5, 50, 2, 3});
        valuesForSequence.put(4, new double[]{0.85, 80, 6, 5});
        valuesForSequence.put(3, new double[]{1.25, 100, 11, 6});
        valuesForSequence.put(2, new double[]{1.25, 100, 12, 6});
        valuesForSequence.put(1, new double[]{1.25, 100, 13, 6});

        sequencePower = pathway.getSequence().getCurrentSequence();
        wasAdjustedOnce = false;

        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility() {

        if (pathway.getSequence().getCurrentSequence() > 3)
            sequencePower = pathway.getSequence().getCurrentSequence();
        else if (!wasAdjustedOnce) {
            sequencePower = pathway.getSequence().getCurrentSequence();
            wasAdjustedOnce = true;
        }

        double multiplier = (valuesForSequence.get(sequencePower) != null ? valuesForSequence.get(sequencePower)[3] : 3);

        new BukkitRunnable() {

            final double circlePoints = valuesForSequence.get(pathway.getSequence().getCurrentSequence()) != null ? valuesForSequence.get(sequencePower)[1] : 20;
            double radius = valuesForSequence.get(sequencePower) != null ? valuesForSequence.get(sequencePower)[0] : 0.25;

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

                if (world == null)
                    return;

                //Particle effects
                //Calls rotateAroundAxis() functions from VectorUtils class
                for (int i = 0; i < circlePoints; i++) {
                    double angle = i * increment + circlePointOffset;
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);

                    Vector vec = new Vector(x, 0, z);
                    VectorUtils.rotateAroundAxisX(vec, pitch);
                    VectorUtils.rotateAroundAxisY(vec, yaw);
                    loc.add(vec);


                    world.spawnParticle(Particle.SPELL, loc, 0);
                    loc.subtract(vec);
                }
                circlePointOffset += increment / 3;
                if (circlePointOffset >= increment) {
                    circlePointOffset = 0;
                }
                loc.add(dir);
                radius -= (valuesForSequence.get(sequencePower) != null ? valuesForSequence.get(sequencePower)[0] : 0.25) / 70;

                //Check if hit Entity
                if (!world.getNearbyEntities(loc, 5, 5, 5).isEmpty()) {
                    for (Entity entity : world.getNearbyEntities(loc, 5, 5, 5)) {
                        Vector v1 = new Vector(
                                loc.getX() + radius / 2,
                                loc.getY() + radius / 2,
                                loc.getZ() + radius / 2
                        );
                        Vector v2 = new Vector(
                                loc.getX() - radius / 2,
                                loc.getY() - radius / 2,
                                loc.getZ() - radius / 2
                        );
                        if (entity.getBoundingBox().overlaps(v1, v2) && entity instanceof Damageable && entity != p) {
                            if (valuesForSequence.get(sequencePower) != null && valuesForSequence.get(sequencePower)[2] > 1)
                                world.createExplosion(entity.getLocation(), (int) (valuesForSequence.get(sequencePower)[2] - 1));
                            ((Damageable) entity).damage(7 * multiplier, p);
                            cancel();
                            return;
                        }
                    }
                }

                counter++;

                if (loc.getBlock().getType().isSolid() || counter >= 50) {
                    if (valuesForSequence.get(sequencePower) != null && valuesForSequence.get(sequencePower)[2] > 0)
                        world.createExplosion(loc, (int) (valuesForSequence.get(sequencePower)[2]));
                    cancel();
                }
            }

        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    @Override
    //If sequence is higher than 3, display actionbar with selected sequence power
    public void onHold() {
        p = pathway.getBeyonder().getPlayer();

        if (pathway.getSequence().getCurrentSequence() > 3)
            return;

        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5Selected: §8Sequence " + sequencePower));
    }

    @Override
    //Adjust sequence power on left click
    public void leftClick() {
        if (pathway.getSequence().getCurrentSequence() > 3)
            return;

        sequencePower--;
        if (sequencePower < pathway.getSequence().getCurrentSequence())
            sequencePower = 7;
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.GHAST_TEAR, "Air Bullet", "30", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}