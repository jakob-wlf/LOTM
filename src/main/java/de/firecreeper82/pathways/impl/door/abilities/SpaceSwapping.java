package de.firecreeper82.pathways.impl.door.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.door.DoorItems;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class SpaceSwapping extends Ability implements Listener {

    private int radius;
    private boolean isSwapping;

    private usages useCase;
    private final usages[] useCases;
    private int selected;

    private ArrayList<Block> swappedBlocks;
    private Location originLoc;

    public SpaceSwapping(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);

        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);

        radius = 30;
        isSwapping = false;

        useCases = usages.values();
        selected = 0;
        useCase = useCases[selected];
    }

    enum usages {
        SWAP("Swap two locations"),
        MOVE("Move space to a new location"),
        COPY("Copy Space two a new location");

        public String name;

        usages(String name) {
            this.name = name;
        }
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        Vector dir = p.getEyeLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();

        for(int i = 0; i < 80; i++) {
            if(loc.getBlock().getType().isSolid() || loc.getBlock().getType() == Material.WATER)
                break;
            loc.add(dir);
        }

        if(loc.getWorld() == null)
            return;

        if(!isSwapping) {
            isSwapping = true;

            swappedBlocks = Util.getBlocksInSquare(loc.getBlock(), radius);

            originLoc = loc.clone();

            for(Block block : swappedBlocks) {
                if(!block.getLocation().add(0, 1, 0).getBlock().getType().isSolid() && block.getType().isSolid())
                    p.getWorld().spawnParticle(Particle.SPELL_WITCH, block.getLocation().clone().add(0, 1, 0), 2, 0, 0, 0, 0);
            }
            return;
        }

        if(loc.getWorld() != originLoc.getWorld()) {
            isSwapping = false;
            swappedBlocks = null;

            p.sendMessage("§cThe two places have to be in the same Dimension");
            return;
        }

        isSwapping = false;

        Vector subtract = loc.clone().toVector().subtract(originLoc.clone().toVector());

        ArrayList<Block> newBlocks = Util.getBlocksInSquare(loc.getBlock(), radius);
        HashMap<Block, Material> materials = new HashMap<>();
        HashMap<Block, BlockData> blockDatas = new HashMap<>();

        for(Block block : newBlocks) {
            materials.put(block, block.getType());
            blockDatas.put(block, block.getBlockData());
        }

        Vector newVector = originLoc.clone().toVector().subtract(loc.clone().toVector());

        for(Block block : swappedBlocks) {
            block.getWorld().getBlockAt(block.getLocation().clone().add(subtract)).setType(block.getType());
            block.getWorld().getBlockAt(block.getLocation().clone().add(subtract)).setBlockData(block.getBlockData());

            if(useCase == usages.MOVE)
                block.setType(Material.AIR);
        }

        if(useCase == usages.SWAP) {
            for(Block block : newBlocks) {
                block.getWorld().getBlockAt(block.getLocation().clone().add(newVector)).setType(materials.get(block));
                block.getWorld().getBlockAt(block.getLocation().clone().add(newVector)).setBlockData(blockDatas.get(block));
            }
        }
    }

    @Override
    public void leftClick() {
        if(isSwapping) {
            p.sendMessage("§cYou are currently swapping spaces!", "§cCancelling swapping!");
            isSwapping = false;
            swappedBlocks = null;
            return;
        }
        selected++;

        if(selected >= useCases.length) {
            selected = 0;
        }

        useCase = useCases[selected];
    }

    @EventHandler
    public void onShift(PlayerToggleSneakEvent e) {
        p = pathway.getBeyonder().getPlayer();

        if(e.getPlayer() != p || e.getPlayer().isSneaking() || !p.getInventory().getItemInMainHand().isSimilar(getItem()) || isSwapping)
            return;

        radius++;

        if(radius >= 43)
            radius = 5;

        p.sendMessage("§bSet the radius to " + radius);
    }

    @Override
    public void onHold() {
        p = pathway.getBeyonder().getPlayer();

        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§bSelected use-case: §7" + useCase.name));
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.SCULK_SENSOR, "Space Swapping", "6250", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
