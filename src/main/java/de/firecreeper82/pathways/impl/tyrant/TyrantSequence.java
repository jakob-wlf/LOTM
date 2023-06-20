package de.firecreeper82.pathways.impl.tyrant;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Sequence;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
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
    }

    @EventHandler
    public void onAirLoss(EntityAirChangeEvent e) {
        if(e.getEntity() != pathway.getBeyonder().getPlayer())
            return;

        if(pathway.getSequence().getCurrentSequence() > 7)
            return;

        pathway.getBeyonder().getPlayer().setMaximumAir(20 * 60 * 30);
    }
}
