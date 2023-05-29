package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.npc.RemoveOnDamageTrait;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Recordable;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class PaperSubstitute extends Recordable {

    public PaperSubstitute(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);

        items.addToSequenceItems(identifier - 1, sequence);
    }

    @Override
    public void useAbility(Player p, double multiplier, Beyonder beyonder, boolean recorded) {
        destroy(beyonder, recorded);

        Location loc = p.getLocation();

        if (loc.getWorld() == null)
            return;

        //Check if Player has paper in inv
        if (!p.getInventory().contains(Material.PAPER))
            return;

        ItemStack item;
        for (int i = 0; i < p.getInventory().getContents().length; i++) {
            item = p.getInventory().getItem(i);
            if (item == null)
                continue;
            if (item.getType() == Material.PAPER) {
                item.setAmount(item.getAmount() - 1);
                p.getInventory().setItem(i, item);
                break;
            }
        }

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, p.getName());
        npc.addTrait(new RemoveOnDamageTrait());
        npc.spawn(loc);
        npc.setProtected(false);

        Random random = new Random();
        Location newLoc = loc.clone().add((random.nextInt(50) - 25), random.nextInt(25) - 12.5, random.nextInt(50) - 25);
        for (int i = 0; i < 500; i++) {
            if (!newLoc.getBlock().getType().isSolid())
                break;
            newLoc = loc.clone().add((random.nextInt(50) - 25), random.nextInt(25) - 12.5, random.nextInt(50) - 25);
        }
        p.teleport(newLoc);

        //remove FakePlayer after a few seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                loc.getWorld().spawnParticle(Particle.CLOUD, loc.clone().subtract(0, 0.25, 0), 100, 0.35, 1, 0.35, 0);
                npc.destroy();
            }
        }.runTaskLater(Plugin.instance, 60);
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.ARMOR_STAND, "Paper Figurine Substitute", "35", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
