package dev.ua.ikeepcalm.mystical.artifacts.negativeEffects.impl;

import dev.ua.ikeepcalm.LordOfTheMinecraft;
import dev.ua.ikeepcalm.mystical.artifacts.SealedArtifact;
import dev.ua.ikeepcalm.mystical.artifacts.negativeEffects.NegativeEffect;
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
