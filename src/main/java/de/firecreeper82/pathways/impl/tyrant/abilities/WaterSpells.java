package de.firecreeper82.pathways.impl.tyrant.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.tyrant.TyrantItems;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.Random;

public class WaterSpells extends NPCAbility {

    private Category selectedCategory = Category.LIGHT;
    private final Category[] categories = Category.values();
    private int selected = 0;

    private final boolean npc;

    public WaterSpells(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        this.npc = npc;
        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);
        p = pathway.getBeyonder().getPlayer();
    }

    enum Category {
        LIGHT("§9Aqueous Light"),
        BEAM("§9Water Beam");

        private final String name;

        Category(String name) {
            this.name = name;
        }
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        switch (selectedCategory) {
            case LIGHT -> light(p);
            case BEAM -> beam(p);
        }
    }

    private void beam(Entity caster) {
        if(!npc && caster.getLocation().getBlock().getType() != Material.WATER) {
            caster.sendMessage("§cYou have to be in water to use this");
            return;
        }
    }

    private void light(LivingEntity caster) {
        //get block player is looking at
        BlockIterator iter = new BlockIterator(caster, 9);
        Block lastBlock = iter.next();
        Block previousBlock;
        while (iter.hasNext()) {
            previousBlock = lastBlock;
            lastBlock = iter.next();
            if (lastBlock.getType().isSolid()) {
                lastBlock = previousBlock;
                break;
            }

        }
        Location loc = lastBlock.getLocation();

        loc.getBlock().setType(Material.LIGHT);
        loc.add(0.5, 0.5, 0.5);

        final Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(143, 255, 244), 1.75f);

        Random random = new Random();

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {

                if(loc.getWorld() == null)
                    return;

                counter++;
                double x = Math.cos(counter);
                double z = Math.sin(counter);
                double y = Math.sin(counter);
                if(random.nextBoolean())
                    loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + x, loc.getY(), loc.getZ() + z, 1, 0, 0, 0, 0);
                if(random.nextBoolean())
                    loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX() + x, loc.getY() + y, loc.getZ(), 1, 0, 0, 0, 0);
                y = Math.cos(counter);
                if(random.nextBoolean())
                    loc.getWorld().spawnParticle(Particle.END_ROD, loc.getX(), loc.getY() + y, loc.getZ() + z, 1, 0, 0, 0, 0);

                loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 10, 0.25, 0.25, 0.25, dust);

                if (counter == 2 * 20) {
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                }

                if (counter >= 15 * 20) {
                    loc.getBlock().setType(Material.AIR);
                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {

    }

    @Override
    public ItemStack getItem() {
        return TyrantItems.createItem(Material.PRISMARINE_SHARD, "Water Spells", "60", identifier, 7, p.getName());
    }

    @Override
    //Cycle through categories on left click
    public void leftClick() {
        selected++;
        if (selected >= categories.length)
            selected = 0;
        selectedCategory = categories[selected];
    }

    @Override
    //Display selected category
    public void onHold() {
        if (p == null)
            p = pathway.getBeyonder().getPlayer();
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5Selected Calamity: §f" + selectedCategory.name));
    }
}
