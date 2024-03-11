package dev.ua.ikeepcalm.pathways.sealedArtifacts.negativeEffects.impl;

import dev.ua.ikeepcalm.Plugin;
import dev.ua.ikeepcalm.pathways.sealedArtifacts.SealedArtifact;
import dev.ua.ikeepcalm.pathways.sealedArtifacts.negativeEffects.NegativeEffect;
import org.bukkit.entity.Player;

public class SpiritSummoning extends NegativeEffect {
    public SpiritSummoning(int sequence, int delay, boolean constant) {
        super(sequence, delay, constant);
    }

    @Override
    public void doEffect(SealedArtifact artifact, Player p) {
        Plugin.instance.getSpiritHandler().spawnRandom(p.getLocation(), p, false, false, true);
    }
}
