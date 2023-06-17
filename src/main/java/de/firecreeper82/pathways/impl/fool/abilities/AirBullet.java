package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.VectorUtils;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
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
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AirBullet extends NPCAbility {

    private static HashMap<Integer, double[]> valuesForSequence;

    private int sequencePower;
    private boolean wasAdjustedOnce;

    private final boolean npc;

    public AirBullet(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);

        this.npc = npc;

        valuesForSequence = new HashMap<>();
        valuesForSequence.put(7, new double[]{0.25, 20, 0, 1});
        valuesForSequence.put(6, new double[]{0.35, 40, 0, 2.5});
        valuesForSequence.put(5, new double[]{0.5, 50, 2, 3});
        valuesForSequence.put(4, new double[]{0.85, 80, 6, 5});
        valuesForSequence.put(3, new double[]{1.25, 100, 11, 6});
        valuesForSequence.put(2, new double[]{1.25, 100, 12, 6});
        valuesForSequence.put(1, new double[]{1.25, 100, 13, 6});


        if(!npc) {
            sequencePower = pathway.getSequence().getCurrentSequence();
            wasAdjustedOnce = false;

            items.addToSequenceItems(identifier - 1, sequence);
        }
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
        if(!npc) {
            if (pathway.getSequence().getCurrentSequence() > 3)
                sequencePower = pathway.getSequence().getCurrentSequence();
            else if (!wasAdjustedOnce) {
                sequencePower = pathway.getSequence().getCurrentSequence();
                wasAdjustedOnce = true;
            }
            multiplier = (valuesForSequence.get(sequencePower) != null ? valuesForSequence.get(sequencePower)[3] : 3);
        }

        double finalMultiplier = multiplier;

        final HashMap<Double, double[]> npcMultiplier = new HashMap<>();
        npcMultiplier.put(1.4, new double[]{0.25, 20, 0, 1});
        npcMultiplier.put(1.6, new double[]{0.35, 40, 0, 2.5});
        npcMultiplier.put(1.7, new double[]{0.5, 50, 2, 3});
        npcMultiplier.put(1.9, new double[]{0.85, 80, 6, 5});
        npcMultiplier.put(2.5, new double[]{1.25, 100, 11, 6});
        npcMultiplier.put(3.0, new double[]{1.25, 100, 12, 6});
        npcMultiplier.put(3.5, new double[]{1.25, 100, 13, 6});

        new BukkitRunnable() {

            final double circlePoints = npc ? npcMultiplier.get(finalMultiplier)[1] : (valuesForSequence.get(pathway.getSequence().getCurrentSequence()) != null ? valuesForSequence.get(sequencePower)[1] : 20);
            double radius = npc ? npcMultiplier.get(finalMultiplier)[0] : valuesForSequence.get(sequencePower) != null ? valuesForSequence.get(sequencePower)[0] : 0.25;

            final Location loc = caster.getLocation().add(0, 1.5, 0);
            final World world = loc.getWorld();
            final Vector dir = caster.getLocation().getDirection().normalize();

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
                radius -= npc ? npcMultiplier.get(finalMultiplier)[0] / 70 : (valuesForSequence.get(sequencePower) != null ? valuesForSequence.get(sequencePower)[0] : 0.25) / 70;

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
                        if (entity.getBoundingBox().overlaps(v1, v2) && entity instanceof Damageable && entity != caster && entity.getType() != EntityType.ARMOR_STAND) {
                            if (valuesForSequence.get(sequencePower) != null && valuesForSequence.get(sequencePower)[2] > 1 && !npc)
                                world.createExplosion(entity.getLocation(), (int) (valuesForSequence.get(sequencePower)[2] - 1));
                            if(npc)
                                world.createExplosion(entity.getLocation(), (int) (npcMultiplier.get(finalMultiplier)[2] - 1));
                            ((Damageable) entity).damage(7 * finalMultiplier, caster);
                            cancel();
                            return;
                        }
                    }
                }

                counter++;

                if (loc.getBlock().getType().isSolid() || counter >= 50) {
                    if (valuesForSequence.get(sequencePower) != null && valuesForSequence.get(sequencePower)[2] > 0 && !npc)
                        world.createExplosion(loc, (int) (valuesForSequence.get(sequencePower)[2]));
                    if(npc)
                        world.createExplosion(loc, (int) (npcMultiplier.get(finalMultiplier)[2]));
                    cancel();
                }
            }

        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    @Override
    public void useAbility() {
        useNPCAbility(p.getLocation(), p, getMultiplier());
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