package dev.ua.ikeepcalm.mystical.sealedArtifacts.negativeEffects.impl;

import dev.ua.ikeepcalm.LordOfTheMinecraft;
import dev.ua.ikeepcalm.mystical.sealedArtifacts.SealedArtifact;
import dev.ua.ikeepcalm.mystical.sealedArtifacts.negativeEffects.NegativeEffect;
import org.bukkit.entity.Player;

public class SpiritSummoning extends NegativeEffect {
    public SpiritSummoning(int sequence, int delay, boolean constant) {
        super(sequence, delay, constant);
    }

    @Override
    public void doEffect(SealedArtifact artifact, Player p) {
        LordOfTheMinecraft.instance.getSpiritHandler().spawnRandom(p.getLocation(), p, false, false, true);
    }
}
