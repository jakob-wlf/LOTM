package de.firecreeper82.pathways.impl.emperor;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Sequence;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class EmperorSequence extends Sequence implements Listener {

    public EmperorSequence(Pathway pathway, int optionalSequence) {
        super(pathway, optionalSequence);
        init();
    }

    @Override
    public List<Integer> getIds() {
        Integer[] ids = {1, 4, 7};
        return Arrays.asList(ids);
    }

    @EventHandler
    public void onUseMagnify(PlayerItemHeldEvent event) {
        pathway = Plugin.beyonders.get(event.getPlayer().getUniqueId()).getPathway();
        if (Plugin.beyonders.containsKey(event.getPlayer().getUniqueId()) && pathway.getPathway() instanceof EmperorPathway && pathway.getSequence().getCurrentSequence() >= 4 && pathway.getBeyonder().isMagnifyingDamage) {
            Player player = event.getPlayer();
            int SharpBonus = pathway.getBeyonder().damageMagnified;
            int PrevSharpBonus = pathway.getBeyonder().damageMagnifiedOld;
            int newSlot = event.getNewSlot();
            int oldSlot = event.getPreviousSlot();

            ItemStack NewItem = player.getInventory().getItem(newSlot);
            ItemStack PrevItem = player.getInventory().getItem(oldSlot);

            Map<Enchantment, Integer> Nec = Objects.requireNonNull(NewItem).getEnchantments();
            Map<Enchantment, Integer> Oec = Objects.requireNonNull(PrevItem).getEnchantments();

            Integer PrevItemSharp = Oec.get(Enchantment.DAMAGE_ALL);
            Integer NewItemSharp = Nec.get(Enchantment.DAMAGE_ALL);

            PrevItem.removeEnchantment(Enchantment.DAMAGE_ALL);
            if (PrevItemSharp - PrevSharpBonus != -1) {
                PrevItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, PrevItemSharp - PrevSharpBonus);
            }

            NewItem.removeEnchantment(Enchantment.DAMAGE_ALL);
            NewItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, SharpBonus + NewItemSharp);

            pathway.getBeyonder().damageMagnifiedOld = SharpBonus;
        }
    }

    public void init() {
        usesAbilities = new boolean[19];
        Arrays.fill(usesAbilities, false);

        abilities = new ArrayList<>();
        recordables = new ArrayList<>();

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
        PotionEffect[] effects9 = {
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 0, false, false, false),
                new PotionEffect(PotionEffectType.SPEED, 60, 0, false, false, true),
        };
        sequenceEffects.put(8, effects9);
        PotionEffect[] effects8 = {
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 1, false, false, false),
                new PotionEffect(PotionEffectType.SPEED, 60, 1, false, false, true),
                new PotionEffect(PotionEffectType.JUMP, 60, 1, false, false, true),
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 1, false, false, false),
        };
        sequenceEffects.put(8, effects8);
        PotionEffect[] effects7 = {
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 2, false, false, false),
                new PotionEffect(PotionEffectType.SPEED, 60, 2, false, false, true),
                new PotionEffect(PotionEffectType.JUMP, 60, 2, false, false, true),
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 1, false, false, false),
                new PotionEffect(PotionEffectType.HEALTH_BOOST, 60, 0, false, false, false)
        };
        sequenceEffects.put(7, effects7);
        PotionEffect[] effects4 = {
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 3, false, false, false),
                new PotionEffect(PotionEffectType.SPEED, 60, 3, false, false, true),
                new PotionEffect(PotionEffectType.JUMP, 60, 2, false, false, true),
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 2, false, false, false),
                new PotionEffect(PotionEffectType.HEALTH_BOOST, 60, 2, false, false, false)
        };
        sequenceEffects.put(4, effects4);
    }
}
