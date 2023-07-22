package de.firecreeper82.pathways.sealedArtifacts.negativeEffects.impl;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.sealedArtifacts.SealedArtifact;
import de.firecreeper82.pathways.sealedArtifacts.negativeEffects.NegativeEffect;
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
