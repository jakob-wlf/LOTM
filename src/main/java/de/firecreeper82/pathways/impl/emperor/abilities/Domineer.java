package de.firecreeper82.pathways.impl.emperor.abilities;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.emperor.EmperorItems;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.ArrayList;
import java.util.List;

public class Domineer extends Ability {
    public static int radius = 5;
    List<Player> affectedPlayers = new ArrayList<>();
    Player p = pathway.getBeyonder().getPlayer();

    public Domineer(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void leftClick() {
        p = pathway.getBeyonder().getPlayer();
        if (!(p.isSneaking())) {
            if (radius != 25) {
                radius++;
            } else {
                radius = 5;
            }
        } else {
            if (radius != 5) {
                radius--;
            } else {
                radius = 25;
            }
        }
    }

    @Override
    public void useAbility() {
        for (Entity entity : p.getWorld().getEntities()) {

            if (entity.getLocation().distance(p.getLocation()) <= radius && entity instanceof Player) {
                Beyonder checkSeq = Plugin.beyonders.get(entity.getUniqueId());
                if (checkSeq.getPathway().getSequence().getCurrentSequence() + 2 >= pathway.getSequence().getCurrentSequence()) {
                    affectedPlayers.add((Player) entity);
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2000, 10, false, false, false));
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 2000, 3, false, false, false));
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 2000, 3, false, false, false));
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2000,1,false,false,false));
                }
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return EmperorItems.createItem(Material.GOLDEN_HELMET, "Domineer", "2500", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
