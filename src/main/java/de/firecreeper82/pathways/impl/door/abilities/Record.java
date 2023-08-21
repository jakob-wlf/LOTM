package de.firecreeper82.pathways.impl.door.abilities;


import de.firecreeper82.lotm.AbilityUtilHandler;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.DoorItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Record extends NPCAbility {


    private final HashMap<Entity, List<NPCAbility>> npcRecordedAbilities;

    public Record(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);

        npcRecordedAbilities = new HashMap<>();
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
        Random random = new Random();

        if(!npcRecordedAbilities.containsKey(caster)) {

            ArrayList<NPCAbility> abilities = new ArrayList<>();

            for(int j = 0; j < 4; j++) {
                NPCAbility ability = AbilityUtilHandler.getAbilities()
                        .get(random.nextInt(AbilityUtilHandler.getAbilities().size()))
                        .get(random.nextInt(AbilityUtilHandler.getAbilities().get(random.nextInt(AbilityUtilHandler.getAbilities().size())).size()));

                int maxIterations = 500;

                int tempSequence = 6;
                int sequence;

                double[] multiplierTable = new double[]{
                        0, 3.5, 3, 2.5, 1.9, 1.7, 1.6, 1.4, 1.3, 1
                };

                for (int i = 1; i < 10; i++) {
                    if (multiplier != multiplierTable[i])
                        continue;
                    tempSequence = i;
                    break;
                }

                final double[] PROBABILITY_DISTRIBUTION = {0.02, 0.04, 0.05, 0.06, 0.11, 0.12, 0.13, 0.15, 0.28};
                final int MIN_VALUE = 1;

                sequence = Util.biasedRandomNumber(PROBABILITY_DISTRIBUTION, MIN_VALUE);
                if(sequence > tempSequence)
                    sequence = tempSequence;


                while (ability.getSequence() < sequence && maxIterations >= 0) {
                    ability = AbilityUtilHandler.getAbilities()
                            .get(random.nextInt(AbilityUtilHandler.getAbilities().size()))
                            .get(random.nextInt(AbilityUtilHandler.getAbilities().get(random.nextInt(AbilityUtilHandler.getAbilities().size())).size()));

                    maxIterations--;
                }

                abilities.add(ability);
            }

            npcRecordedAbilities.put(caster, abilities);
        }

        if(caster instanceof Player castPlayer && castPlayer.isSneaking()) {
            npcRecordedAbilities.get(caster).forEach(npcAbility -> castPlayer.sendMessage(npcAbility.getClass().getSimpleName()));
        }

        npcRecordedAbilities.get(caster).get(random.nextInt(npcRecordedAbilities.get(caster).size())).useNPCAbility(loc, caster, multiplier);

        for(Entity temp : npcRecordedAbilities.keySet()) {
            if(!temp.isValid())
                npcRecordedAbilities.remove(temp);
        }
    }

    @Override
    public void useAbility() {



    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.BOOK, "Record (temporarily removed)", "15/s", identifier, 8, pathway.getBeyonder().getPlayer().getName());
    }
}
