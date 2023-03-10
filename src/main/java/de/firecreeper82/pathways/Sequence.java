package de.firecreeper82.pathways;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public abstract class Sequence {

    public int currentSequence;
    public Pathway pathway;

    public boolean[] usesAbilities;
    public ArrayList<Ability> abilities;

    public HashMap<Integer, PotionEffect[]> sequenceEffects;
    public HashMap<Integer, PotionEffectType[]> sequenceResistances;

    public HashMap<Integer, Double> sequenceMultiplier;

    public Sequence(Pathway pathway) {
        this.pathway = pathway;
    }
    public Sequence(Pathway pathway, int optionalSequence) {
        this.pathway = pathway;
        this.currentSequence = optionalSequence;
    }

    public void useAbility(ItemStack item, PlayerInteractEvent e) {
        if(!checkValid(item))
            return;

        e.setCancelled(true);
        useAbility(Objects.requireNonNull(item.getItemMeta()).getEnchantLevel(Enchantment.CHANNELING), item);
    }

    public void destroyItem(ItemStack item, PlayerDropItemEvent e) {
        if(pathway.getItems().getItems().contains(item)) {
            e.getItemDrop().remove();
        }
    }

    public abstract void useAbility(int ability, ItemStack item);

    public boolean checkValid(ItemStack item) {
        if(item == null)
            return false;
        ItemStack checkItem = item.clone();
        checkItem.setAmount(1);
        return pathway.getItems().returnItemsFromSequence(currentSequence).contains(checkItem);
    }

    public void removeSpirituality(double remove) {
        pathway.getBeyonder().setSpirituality(pathway.getBeyonder().getSpirituality() - remove);
    }

    public void onHold(ItemStack item) {
        if(!checkValid(item))
            return;

        int id = Objects.requireNonNull(item.getItemMeta()).getEnchantLevel(Enchantment.CHANNELING);
        for(Ability a : abilities) {
            if(a.getIdentifier() == id) {
                a.onHold();
                break;
            }
        }
    }

    public int getCurrentSequence() {
        return currentSequence;
    }

    public void setCurrentSequence(int currentSequence) {
        this.currentSequence = currentSequence;
    }

    public Pathway getPathway() {
        return pathway;
    }

    public void setPathway(Pathway pathway) {
        this.pathway = pathway;
    }

    public HashMap<Integer, PotionEffect[]> getSequenceEffects() {
        return sequenceEffects;
    }

    public void setSequenceEffects(HashMap<Integer, PotionEffect[]> sequenceEffects) {
        this.sequenceEffects = sequenceEffects;
    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(ArrayList<Ability> abilities) {
        this.abilities = abilities;
    }

    public boolean[] getUsesAbilities() {
        return usesAbilities;
    }

    public void setUsesAbilities(boolean[] usesAbilities) {
        this.usesAbilities = usesAbilities;
    }

    public HashMap<Integer, PotionEffectType[]> getSequenceResistances() {
        return sequenceResistances;
    }

    public void setSequenceResistances(HashMap<Integer, PotionEffectType[]> sequenceResistances) {
        this.sequenceResistances = sequenceResistances;
    }

    public HashMap<Integer, Double> getSequenceMultiplier() {
        return sequenceMultiplier;
    }

    public void setSequenceMultiplier(HashMap<Integer, Double> sequenceMultiplier) {
        this.sequenceMultiplier = sequenceMultiplier;
    }
}
