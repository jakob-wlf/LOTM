package de.firecreeper82.pathways.impl.tyrant;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Sequence;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class TyrantSequence extends Sequence implements Listener {

    private static final HashMap<Integer, Particle.DustOptions> lightningColor = new HashMap<>();

    public TyrantSequence(Pathway pathway, int optionalSequence) {
        super(pathway, optionalSequence);
        init();

        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
    }

    @Override
    public List<Integer> getIds() {
        Integer[] ids = {};
        return Arrays.asList(ids);
    }

    public void init() {
        usesAbilities = new boolean[19];
        Arrays.fill(usesAbilities, false);

        abilities = new ArrayList<>();

        sequenceEffects = new HashMap<>();
        sequenceResistances = new HashMap<>();

        initEffects();

        sequenceMultiplier = new HashMap<>();
        sequenceMultiplier.put(5, 1.5);
        sequenceMultiplier.put(4, 2.0);
        sequenceMultiplier.put(3, 2.25);
        sequenceMultiplier.put(2, 3.5);
        sequenceMultiplier.put(1, 5.0);

        lightningColor.put(5, new Particle.DustOptions(Color.fromRGB(143, 255, 244), 1.75f));
        lightningColor.put(4, new Particle.DustOptions(Color.fromRGB(143, 255, 244), 1.9f));
        lightningColor.put(3, new Particle.DustOptions(Color.fromRGB(143, 255, 244), 1.95f));
        lightningColor.put(2, new Particle.DustOptions(Color.fromRGB(237, 189, 78), 1.95f));
        lightningColor.put(1, new Particle.DustOptions(Color.fromRGB(100, 20, 204), 2f));
        lightningColor.put(0, new Particle.DustOptions(Color.fromRGB(137, 20, 204), 2f));
    }

    public HashMap<Integer, Particle.DustOptions> getLightningColor() {
        return lightningColor;
    }

    //Passive effects
    public void initEffects() {
        PotionEffect[] effects9 = {
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 0, false, false, false),
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 0, false, false, false),
                new PotionEffect(PotionEffectType.NIGHT_VISION, 500, 5, false, false, true),
                new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 80, 1, false, false, true),
        };
        sequenceEffects.put(9, effects9);

        PotionEffect[] effects8 = {
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 1, false, false, false),
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 1, false, false, false),
                new PotionEffect(PotionEffectType.NIGHT_VISION, 500, 5, false, false, true),
                new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 80, 4, false, false, true),
                new PotionEffect(PotionEffectType.SPEED, 60, 0, false, false, true),
        };
        sequenceEffects.put(8, effects8);
        PotionEffect[] effects2 = {
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 1, false, false, false),
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 1, false, false, false),
                new PotionEffect(PotionEffectType.NIGHT_VISION, 500, 5, false, false, true),
                new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 80, 4, false, false, true),
                new PotionEffect(PotionEffectType.SPEED, 60, 0, false, false, true),
                new PotionEffect(PotionEffectType.SATURATION, 60, 10, false, false, true),
        };
        sequenceEffects.put(2, effects2);
    }

    @Override
    public void run() {
        aquaticLifeManipulation();
    }

    private void aquaticLifeManipulation() {
        if (currentSequence > 3)
            return;

        if (target == null)
            return;

        if (!target.isValid() || target == pathway.getBeyonder().getPlayer() || !(target instanceof LivingEntity)) {
            target = null;
            return;
        }

        if (pathway.getBeyonder().getPlayer().getLocation().getBlock().getType() != Material.WATER)
            return;

        for (Entity entity : pathway.getBeyonder().getPlayer().getNearbyEntities(200, 50, 200)) {
            if (!(entity instanceof Mob mob))
                continue;

            if (mob.getCategory() != EntityCategory.WATER && mob.getType() != EntityType.DROWNED)
                continue;

            mob.setTarget((LivingEntity) target);

            if (entity.getLocation().distance(target.getLocation()) > 1.5)
                entity.setVelocity(target.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(.5));
            else if (mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null)
                mob.attack(target);
            else
                ((LivingEntity) target).damage(.5, mob);
        }
    }

    public static void spawnLighting(Location loc, Entity caster, double multiplier, boolean npc, boolean destruction, Integer sequence) {
        Location particleLoc = loc.clone().add(0, 50, 0);

        Random random = new Random();

        final Particle.DustOptions dust = (!npc) ? (lightningColor.get(sequence)) : new Particle.DustOptions(Color.fromRGB(143, 255, 244), 1.9f);

        ArrayList<Double> randoms1 = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            randoms1.add(i, random.nextDouble(-.5, .5));
        }

        ArrayList<Double> randoms2 = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            randoms2.add(i, random.nextDouble(-.5, .5));
        }

        for (int j = 0; j < 12; j++) {
            final Particle.DustOptions dust1 = (!npc) ? (lightningColor.get(sequence)) : new Particle.DustOptions(Color.fromRGB(143, 255, 244), 1.9f);

            int height = random.nextInt(8, 28);

            Location hitLoc = particleLoc.clone();
            for (int i = 0; i < height; i++) {
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

        for (int i = 0; i < 50; i++) {
            Util.drawDustsForNearbyPlayers(particleLoc.add(0, -1, 0), 20, 0, 0, 0, dust);
            particleLoc.add(randoms1.get(i), 0, randoms2.get(i));
        }

        int counter = 100;
        while (!particleLoc.getBlock().getType().isSolid() && counter > 0) {
            Util.drawDustsForNearbyPlayers(particleLoc.add(0, -1, 0), 20, 0, 0, 0, dust);
            particleLoc.add(random.nextDouble(-.5, .5), 0, random.nextDouble(-.5, .5));
            counter--;
        }

        int damageRadius = 11;

        if (loc.getWorld() == null)
            return;

        for (Entity entity : loc.getWorld().getNearbyEntities(particleLoc, damageRadius, damageRadius / 2f, damageRadius)) {
            if (Util.testForValidEntity(entity, caster, true, true)) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.damage(12 * multiplier, caster);
                livingEntity.setFireTicks(20 * 5);
                Util.drawParticlesForNearbyPlayers(Particle.ELECTRIC_SPARK, entity.getLocation(), 100, 0.1, 0.1, 0.1, .75);
            }

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

        if (destruction) {
            int radius = !npc ? sequence > 4 ? 6 : 12 : 10;
            int power = !npc ? (sequence > 4 ? 5 : sequence > 2 ? 11 : sequence > 1 ? 15 : 30) : 8;

            loc.getWorld().createExplosion(particleLoc, power, true);


            ArrayList<Block> blocks = Util.getBlocksInCircleRadius(particleLoc.getBlock(), radius, true);
            for (Block block : blocks) {
                if (random.nextInt(22) == 0) {
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


    @EventHandler
    public void onAirLoss(EntityAirChangeEvent e) {
        if (e.getEntity() != pathway.getBeyonder().getPlayer())
            return;

        if (currentSequence > 7)
            return;

        if (currentSequence > 6)
            e.setCancelled(true);

        pathway.getBeyonder().getPlayer().setMaximumAir(20 * 60 * 30);
    }

    Entity target = null;

    @EventHandler
    public void onDamageByPlayerTaken(EntityDamageByEntityEvent e) {
        if (currentSequence > 3)
            return;
        if (e.getEntity() == getPathway().getBeyonder().getPlayer()) {
            target = e.getDamager();
        }
        if (e.getDamager() == getPathway().getBeyonder().getPlayer()) {
            target = e.getEntity();
        }
    }
}
