package dev.ua.ikeepcalm.pathways.sealedArtifacts.negativeEffects.impl;

import dev.ua.ikeepcalm.pathways.sealedArtifacts.SealedArtifact;
import dev.ua.ikeepcalm.pathways.sealedArtifacts.negativeEffects.NegativeEffect;
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
