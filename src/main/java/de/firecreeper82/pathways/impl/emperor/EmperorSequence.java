package de.firecreeper82.pathways.impl.emperor;

import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Sequence;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EmperorSequence extends Sequence implements Listener {

        public EmperorSequence(Pathway pathway, int optionalSequence) {
                super(pathway, optionalSequence);
                init();
        }

        @Override
        public List<Integer> getIds() {
                Integer[] ids = {1, 4, 7};
                return Arrays.asList(ids);
        }

        public void init() {
                usesAbilities = new boolean[19];
                Arrays.fill(usesAbilities, false);

                abilities = new ArrayList<>();
                recordables = new ArrayList<>();

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
                        new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 1, false, false, false),
                        new PotionEffect(PotionEffectType.SPEED, 60, 1, false, false, true),
                };
                sequenceEffects.put(8, effects9);
                PotionEffect[] effects8 = {
                        new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 2, false, false, false),
                        new PotionEffect(PotionEffectType.SPEED, 60, 2, false, false, true),
                        new PotionEffect(PotionEffectType.JUMP,60,2,false,false,true)
                };
                sequenceEffects.put(8,effects8);
        }
}
