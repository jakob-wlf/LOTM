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

    protected ArrayList<Recordable> recordables;

    public Sequence(Pathway pathway, int optionalSequence) {
        this.pathway = pathway;
        this.currentSequence = optionalSequence;
    }

    public void useAbility(ItemStack item, PlayerInteractEvent e) {

        if (checkValid(item) == 1) {
            for (Recordable recordable : recordables) {
                if (recordable.getItem().isSimilar(item))
                    recordable.useAbility(pathway.getBeyonder().getPlayer(), pathway.getSequence().getSequenceMultiplier().get(pathway.getSequence().getCurrentSequence()), pathway.getBeyonder(), true);
            }
            return;
        }
        if (checkValid(item) == 2)
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

    public void addRecordable(Recordable recordable) {
        recordables.add(recordable);
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

    public int checkValid(ItemStack item) {
        if (item == null)
            return 2;
        ItemStack checkItem = item.clone();
        checkItem.setAmount(1);

        ArrayList<ItemStack> recordedItems = new ArrayList<>();

        for (Recordable recordable : recordables) {
            recordedItems.add(recordable.getItem());
        }

        if (pathway.getItems().returnItemsFromSequence(currentSequence).contains(checkItem))
            return 0;
        if (recordedItems.contains(checkItem))
            return 1;

        return 2;
    }

    public void removeSpirituality(double remove) {
        pathway.getBeyonder().setSpirituality(pathway.getBeyonder().getSpirituality() - remove);
    }

    public void onHold(ItemStack item) {
        if (checkValid(item) == 2)
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

    public ArrayList<Recordable> getRecordables() {
        return recordables;
    }
}
