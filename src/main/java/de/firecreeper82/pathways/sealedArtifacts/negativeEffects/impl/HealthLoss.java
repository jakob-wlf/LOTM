package de.firecreeper82.pathways.sealedArtifacts.negativeEffects.impl;

import de.firecreeper82.pathways.sealedArtifacts.SealedArtifact;
import de.firecreeper82.pathways.sealedArtifacts.negativeEffects.NegativeEffect;
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
