package dev.ua.ikeepcalm.mystical.artifacts.negativeEffects.impl;

import dev.ua.ikeepcalm.mystical.artifacts.SealedArtifact;
import dev.ua.ikeepcalm.mystical.artifacts.negativeEffects.NegativeEffect;
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
