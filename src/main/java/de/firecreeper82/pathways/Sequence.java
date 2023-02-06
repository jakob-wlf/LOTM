package de.firecreeper82.pathways;

import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;

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

    public abstract void useAbility(ItemStack item, PlayerInteractEvent e);

    public abstract void destroyItem(ItemStack item, PlayerDropItemEvent e);

    public abstract void useAbility(int ability, ItemStack item);

    public abstract boolean checkValid(ItemStack item);

    public abstract void removeSpirituality(double remove);

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
