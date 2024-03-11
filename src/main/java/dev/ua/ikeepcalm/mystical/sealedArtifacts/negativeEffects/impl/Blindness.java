package dev.ua.ikeepcalm.mystical.sealedArtifacts.negativeEffects.impl;

import dev.ua.ikeepcalm.mystical.sealedArtifacts.SealedArtifact;
import dev.ua.ikeepcalm.mystical.sealedArtifacts.negativeEffects.NegativeEffect;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Blindness extends NegativeEffect {

    public Blindness(int sequence, int delay, boolean constant) {
        super(sequence, delay, constant);
    }

    @Override
    public void doEffect(SealedArtifact artifact, Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 6, 1, false, false));
    }
}
