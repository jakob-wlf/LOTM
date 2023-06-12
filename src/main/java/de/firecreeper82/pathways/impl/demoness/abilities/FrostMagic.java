package de.firecreeper82.pathways.impl.demoness.abilities;

import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.demoness.DemonessItems;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FrostMagic extends NPCAbility {

    private Category selectedCategory = Category.Attack;
    private final Category[] categories = Category.values();
    private int selected = 0;

    private final Material[] convertMaterials;

    public FrostMagic(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);

        convertMaterials = new Material[]{
                Material.GRASS_BLOCK,
                Material.DIRT_PATH,
                Material.DIRT,
                Material.ROOTED_DIRT,
                Material.MYCELIUM,
                Material.PODZOL,
                Material.STONE,
                Material.GRANITE,
                Material.DIORITE,
                Material.ANDESITE,
                Material.GRAVEL,
                Material.SAND
        };
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
        if((new Random()).nextBoolean())
            attack(true, loc, caster, multiplier);
        else
            freeze(true, caster, multiplier);
    }

    enum Category {
        Attack("Attack targets"),
        Freeze("Freeze an area");

        private final String name;

        Category(String name) {
            this.name = name;
        }
    }

    @Override
    public void useAbility() {
        if (selectedCategory == Category.Attack)
            attack(false, null, null, getMultiplier());
        if (selectedCategory == Category.Freeze)
            freeze(false, null, getMultiplier());
    }

    private void attack(boolean npc, Location target, Entity e, double multiplier) {
        Entity caster = (npc) ? e : pathway.getBeyonder().getPlayer();

        Location loc = caster.getLocation().add(0, 1.5, 0).clone();
        Vector vector;
        if(!npc)
            vector = caster.getLocation().getDirection().normalize().multiply(.5);
        else
            vector = target.toVector().subtract(loc.toVector()).normalize().multiply(.5);
        if (loc.getWorld() == null)
            return;
        World world = loc.getWorld();

        for (int i = 0; i < 30; i++) {
            loc.add(vector);
            world.spawnParticle(Particle.SNOWFLAKE, loc, 40, .25, .25, .25, 0);

            if (world.getNearbyEntities(loc, 1, 1, 1).isEmpty())
                continue;

            if (loc.getBlock().getType().isSolid()) {
                loc.clone().subtract(vector).getBlock().setType(Material.SOUL_FIRE);
                break;
            }

            boolean cancelled = false;
            for (Entity entity : world.getNearbyEntities(loc, 1, 1, 1)) {
                if ((!(entity instanceof Mob) && !(entity instanceof Player)) || entity == caster)
                    continue;
                ((LivingEntity) entity).damage(15 * multiplier, caster);
                entity.setFreezeTicks(20 * 40);
                cancelled = true;
            }

            if (cancelled)
                break;
        }
    }

    private void freeze(boolean npc, Entity e, double multiplier) {

        Entity caster;
        caster = (npc) ? e : pathway.getBeyonder().getPlayer();
        ArrayList<Block> blocks = Util.getBlocksInCircleRadius(caster.getLocation().subtract(0, .5, 0).getBlock(), 8, true);

        Random random = new Random();

        for (Block block : blocks) {
            if (block.getType() == Material.WATER)
                block.setType(Material.PACKED_ICE);

            if (!Arrays.asList(convertMaterials).contains(block.getType()))
                continue;

            if (random.nextInt(3) == 0)
                continue;

            block.setType(Material.PACKED_ICE);
        }

        caster.getWorld().spawnParticle(Particle.SNOWFLAKE, caster.getLocation().add(0, 1.5, 0), 70, 5, 5, 5, 0);

        for (Entity entity : caster.getNearbyEntities(8, 8, 8)) {
            if (!(entity instanceof LivingEntity livingEntity))
                continue;

            livingEntity.damage(4 * multiplier, caster);
            livingEntity.setFreezeTicks(20 * 6);
        }
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
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5Selected Use-case: §f" + selectedCategory.name));
    }

    @Override
    public ItemStack getItem() {
        return DemonessItems.createItem(Material.ICE, "Frost Magic", "35", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
