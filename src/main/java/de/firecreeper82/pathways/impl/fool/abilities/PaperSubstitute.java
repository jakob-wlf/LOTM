package de.firecreeper82.pathways.impl.fool.abilities;

import com.mojang.authlib.properties.Property;
import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.NPC;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Recordable;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
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

        //Check if Player has paper in inv
        if(!p.getInventory().contains(Material.PAPER))
            return;

        ItemStack item;
        for(int i = 0; i < p.getInventory().getContents().length; i++) {
            item = p.getInventory().getItem(i);
            if(item == null)
                continue;
            if(item.getType() == Material.PAPER) {
                item.setAmount(item.getAmount() - 1);
                p.getInventory().setItem(i, item);
                break;
            }
        }

        ServerPlayer player = ((CraftPlayer) p).getHandle();
        Property property = player.getGameProfile().getProperties().get("textures").iterator().next();
        String[] skin = {
                property.getValue(),
                property.getSignature()
        };
        ServerPlayer npc = NPC.create(loc, p.getName(), skin, true);

        Random random = new Random();
        Location newLoc = loc.clone().add((random.nextInt(50) - 25), random.nextInt(25) - 12.5, random.nextInt(50) - 25);
        for(int i = 0; i < 500; i++) {
            if(!newLoc.getBlock().getType().isSolid())
                break;
            newLoc = loc.clone().add((random.nextInt(50) - 25), random.nextInt(25) - 12.5, random.nextInt(50) - 25);
        }
        p.teleport(newLoc);

        //remove FakePlayer after a few seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!Plugin.fakePlayers.containsKey(npc.getBukkitEntity().getUniqueId()))
                    return;
                Location loc = npc.getBukkitEntity().getLocation();
                if(loc.getWorld() != null)
                    loc.getWorld().spawnParticle(Particle.CLOUD, loc.clone().subtract(0, 0.25, 0), 100, 0.35, 1, 0.35, 0);
                ServerLevel nmsWorld = ((CraftWorld) npc.getBukkitEntity().getWorld()).getHandle();
                nmsWorld.removePlayerImmediately(Plugin.fakePlayers.get(npc.getBukkitEntity().getUniqueId()), Entity.RemovalReason.DISCARDED);
            }
        }.runTaskLater(Plugin.instance, 60);
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.ARMOR_STAND, "Paper Figurine Substitute", "35", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
