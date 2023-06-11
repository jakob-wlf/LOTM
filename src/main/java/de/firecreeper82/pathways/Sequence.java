package de.firecreeper82.pathways;

import de.firecreeper82.lotm.util.UtilItems;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public abstract class Sequence {

    protected int currentSequence;
    protected Pathway pathway;

    protected boolean[] usesAbilities;
    protected ArrayList<Ability> abilities;

    protected HashMap<Integer, PotionEffect[]> sequenceEffects;
    protected HashMap<Integer, PotionEffectType[]> sequenceResistances;

    protected HashMap<Integer, Double> sequenceMultiplier;


    public Sequence(Pathway pathway, int optionalSequence) {
        this.pathway = pathway;
        this.currentSequence = optionalSequence;
    }

    public void useAbility(ItemStack item, PlayerInteractEvent e) {
        if (!checkValid(item))
            return;

        e.setCancelled(true);
        if (e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_BLOCK) {
            useAbility(Objects.requireNonNull(item.getItemMeta()).getEnchantLevel(Enchantment.CHANNELING), item);
            return;
        }

        int id = Objects.requireNonNull(item.getItemMeta()).getEnchantLevel(Enchantment.CHANNELING);
        for (Ability a : abilities) {
            if (a.getIdentifier() == id) {
                a.leftClick();
                break;
            }
        }
    }

    public void destroyItem(ItemStack item, PlayerDropItemEvent e) {
        if (pathway.getItems().getItems().contains(item)) {
            e.getItemDrop().remove();
        }

        for (ItemStack itemStack : UtilItems.returnAllItems()) {
            if (itemStack.isSimilar(item))
                e.getItemDrop().remove();
        }
    }

    public void useAbility(int ability, ItemStack item) {

        int spiritualityDrainage = 0;
        try {
            String line = Objects.requireNonNull(Objects.requireNonNull(item.getItemMeta()).getLore()).get(1);
            spiritualityDrainage = Integer.parseInt(line.substring(18));
        } catch (Exception ignored) {
        }

        if (spiritualityDrainage > pathway.getBeyonder().getSpirituality())
            return;

        if (usesAbilities[ability - 1]) {
            if (getIds().contains(ability))
                usesAbilities[ability - 1] = false;
            return;
        }

        //remove spirituality
        removeSpirituality(spiritualityDrainage);

        for (Ability a : abilities) {
            if (a.getIdentifier() == ability) {
                a.useAbility();
                pathway.getBeyonder().acting(pathway.getItems().getSequenceOfAbility(a));
                break;
            }
        }
    }

    public abstract List<Integer> getIds();

    public boolean checkValid(ItemStack item) {
        if (item == null)
            return false;
        ItemStack checkItem = item.clone();
        checkItem.setAmount(1);

        if (pathway.getItems().returnItemsFromSequence(currentSequence).contains(checkItem))
            return true;

        return false;
    }

    public void removeSpirituality(double remove) {
        pathway.getBeyonder().setSpirituality(pathway.getBeyonder().getSpirituality() - remove);
    }

    public void onHold(ItemStack item) {
        if (!checkValid(item))
            return;

        int id = Objects.requireNonNull(item.getItemMeta()).getEnchantLevel(Enchantment.CHANNELING);
        for (Ability a : abilities) {
            if (a.getIdentifier() == id) {
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

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(ArrayList<Ability> abilities) {
        this.abilities = abilities;
    }

    public boolean[] getUsesAbilities() {
        return usesAbilities;
    }

    public HashMap<Integer, PotionEffectType[]> getSequenceResistances() {
        return sequenceResistances;
    }

    public HashMap<Integer, Double> getSequenceMultiplier() {
        return sequenceMultiplier;
    }

}
