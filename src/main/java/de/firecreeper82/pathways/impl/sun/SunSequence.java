package de.firecreeper82.pathways.impl.sun;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Sequence;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SunSequence extends Sequence {

    public SunSequence(Pathway pathway) {
        super(pathway);
        currentSequence = 9;
        init();
    }

    public SunSequence(Pathway pathway, int optionalSequence) {
        super(pathway, optionalSequence);
        init();
    }

    public void init() {
        usesAbilities = new boolean[19];
        Arrays.fill(usesAbilities, false);

        abilities = new ArrayList<>();

        sequenceEffects = new HashMap<>();
        sequenceResistances = new HashMap<>();

        initEffects();

        sequenceMultiplier = new HashMap<>();
        sequenceMultiplier.put(5, 1.5);
        sequenceMultiplier.put(4, 2.0);
        sequenceMultiplier.put(3, 2.25);
        sequenceMultiplier.put(2, 3.5);
        sequenceMultiplier.put(1, 5.0);
    }

    //Passive effects
    public void initEffects() {
        PotionEffectType[] resistances = {
                PotionEffectType.POISON,
                PotionEffectType.BLINDNESS,
                PotionEffectType.DARKNESS
        };
        sequenceResistances.put(7, resistances);

        PotionEffect[] effects7 = {
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 0, false, false, false),
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 0, false, false, false),
                new PotionEffect(PotionEffectType.SPEED, 60, 1, false, false, true),
        };
        sequenceEffects.put(7, effects7);

        PotionEffect[] effects6 = {
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 1, false, false, false),
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 1, false, false, false),
                new PotionEffect(PotionEffectType.SPEED, 60, 1, false, false, true),
        };
        sequenceEffects.put(6, effects6);

        PotionEffect[] effects2 = {
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 2, false, false, false),
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 3, false, false, false),
                new PotionEffect(PotionEffectType.SPEED, 60, 2, false, false, true),
                new PotionEffect(PotionEffectType.SATURATION, 60, 10, false, false, true),
        };
        sequenceEffects.put(2, effects2);

    }

    @Override
    public void useAbility(ItemStack item, PlayerInteractEvent e) {
        if(!checkValid(item))
            return;

        e.setCancelled(true);
        useAbility(item.getItemMeta().getEnchantLevel(Enchantment.CHANNELING), item);
    }


    @Override
    public void useAbility(int ability, ItemStack item) {

        int spiritualityDrainage = 0;
        try {
            String line = item.getItemMeta().getLore().get(1);
            spiritualityDrainage = Integer.parseInt(line.substring(18));
        }
        catch (Exception ignored) {}

        if(spiritualityDrainage > pathway.getBeyonder().getSpirituality())
            return;

        if(usesAbilities[ability - 1]) {
            if(ability == 6 || ability == 15 || ability == 19)
                usesAbilities[ability - 1] = false;
            return;
        }

        //remove spirituality
        removeSpirituality(spiritualityDrainage);

        for(Ability a : abilities) {
            if(a.getIdentifier() == ability) {
                a.useAbility();
            }
        }
    }

    @Override
    public boolean checkValid(ItemStack item) {
        if(pathway.getItems().returnItemsFromSequence(currentSequence).contains(item))
            return true;
        else
            return false;
    }

    @Override
    public void destroyItem(ItemStack item, PlayerDropItemEvent e) {
        if(pathway.getItems().getItems().contains(item)) {
            e.getItemDrop().remove();
        }
    }

    @Override
    public void removeSpirituality(double remove) {
        pathway.getBeyonder().setSpirituality(pathway.getBeyonder().getSpirituality() - remove);
    }
}
