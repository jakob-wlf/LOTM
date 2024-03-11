package dev.ua.ikeepcalm.mystical.sealedArtifacts.negativeEffects.impl;

import dev.ua.ikeepcalm.mystical.sealedArtifacts.SealedArtifact;
import dev.ua.ikeepcalm.mystical.sealedArtifacts.negativeEffects.NegativeEffect;
import org.bukkit.entity.Player;

public class HealthLoss extends NegativeEffect {
    public HealthLoss(int sequence, int delay, boolean constant) {
        super(sequence, delay, constant);
    }

    @Override
    public void doEffect(SealedArtifact artifact, Player p) {
        p.damage(p.getHealth() / 20f, p);
    }
}
