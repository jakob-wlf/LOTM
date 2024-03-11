package dev.ua.ikeepcalm.mystical.sealedArtifacts.negativeEffects;

import dev.ua.ikeepcalm.mystical.sealedArtifacts.SealedArtifact;
import dev.ua.ikeepcalm.mystical.sealedArtifacts.negativeEffects.impl.Blindness;
import dev.ua.ikeepcalm.mystical.sealedArtifacts.negativeEffects.impl.HealthLoss;
import dev.ua.ikeepcalm.mystical.sealedArtifacts.negativeEffects.impl.Nausea;
import dev.ua.ikeepcalm.mystical.sealedArtifacts.negativeEffects.impl.SpiritSummoning;

import java.util.HashMap;
import java.util.Random;

public class NegativeEffects {

    private final HashMap<Integer, NegativeEffect[]> negativeEffects;

    public NegativeEffects() {
        negativeEffects = new HashMap<>();

        negativeEffects.put(9, new NegativeEffect[]{
                new HealthLoss(9, 40, false),
        });
        negativeEffects.put(7, new NegativeEffect[]{
                new Nausea(7, 20 * 8, false)
        });
        negativeEffects.put(6, new NegativeEffect[]{
                new HealthLoss(5, 15, false),
                new Nausea(6, 20 * 10, true),
        });
        negativeEffects.put(5, new NegativeEffect[]{
                new Blindness(7, 20 * 5, false),
                new SpiritSummoning(5, 20 * 5, false)
        });
        negativeEffects.put(4, new NegativeEffect[]{
                new Blindness(6, 20 * 5, true),
                new HealthLoss(5, 15, true),
                new SpiritSummoning(5, 20 * 5, true)
        });
    }

    public void addEffectToArtifact(SealedArtifact artifact, int sequence) {

        while (sequence <= 9 && negativeEffects.get(sequence) == null) {
            sequence++;
        }

        if (negativeEffects.get(sequence) == null)
            return;

        NegativeEffect effect = negativeEffects.get(sequence)[(new Random().nextInt(negativeEffects.get(sequence).length))];
        effect.start(artifact);
    }
}
