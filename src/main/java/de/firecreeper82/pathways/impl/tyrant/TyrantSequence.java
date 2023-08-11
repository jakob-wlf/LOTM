package de.firecreeper82.pathways.impl.tyrant;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Sequence;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TyrantSequence extends Sequence implements Listener {

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
        if(currentSequence > 3)
            return;

        if( target == null)
            return;

        if(!target.isValid() || target == pathway.getBeyonder().getPlayer() || !(target instanceof LivingEntity)) {
            target = null;
            return;
        }

        if(pathway.getBeyonder().getPlayer().getLocation().getBlock().getType() != Material.WATER)
            return;

        for(Entity entity : pathway.getBeyonder().getPlayer().getNearbyEntities(200, 50, 200)) {
            if(!(entity instanceof Mob mob))
                continue;

            if(mob.getCategory() != EntityCategory.WATER && mob.getType() != EntityType.DROWNED)
                continue;

            mob.setTarget((LivingEntity) target);

            if(entity.getLocation().distance(target.getLocation()) > 1.5)
                entity.setVelocity(target.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(.5));
            else if(mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null)
                mob.attack(target);
            else
                ((LivingEntity) target).damage(.5, mob);
        }
    }


    @EventHandler
    public void onAirLoss(EntityAirChangeEvent e) {
        if(e.getEntity() != pathway.getBeyonder().getPlayer())
            return;

        if(currentSequence > 7)
            return;

        if(currentSequence > 6)
            e.setCancelled(true);

        pathway.getBeyonder().getPlayer().setMaximumAir(20 * 60 * 30);
    }

    Entity target = null;
    @EventHandler
    public void onDamageByPlayerTaken(EntityDamageByEntityEvent e) {
        if(currentSequence > 3)
            return;
        if(e.getEntity() == getPathway().getBeyonder().getPlayer()) {
            target = e.getDamager();
        }
        if(e.getDamager() == getPathway().getBeyonder().getPlayer()) {
            target = e.getEntity();
        }
    }
}
