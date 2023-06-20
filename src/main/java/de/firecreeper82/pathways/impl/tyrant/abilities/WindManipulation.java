package de.firecreeper82.pathways.impl.tyrant.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.tyrant.TyrantItems;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class WindManipulation extends NPCAbility {

    private Category selectedCategory = Category.BLADE;
    private final Category[] categories = Category.values();
    private int selected = 0;

    private final boolean npc;

    public WindManipulation(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        this.npc = npc;
        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);
        p = pathway.getBeyonder().getPlayer();
    }

    enum Category {
        BLADE("§9Wind Blade"),
        FLIGHT("§9Flight"),
        BOOST("§9Boost"),
        GLIDE("§9Glide");

        private final String name;

        Category(String name) {
            this.name = name;
        }
    }


    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
        
        switch(selectedCategory) {
            case BLADE -> blade(p, getMultiplier());
        }
    }

    private void blade(Entity caster, double multiplier) {
        Vector direction = caster.getLocation().getDirection().normalize();
        Location loc = caster.getLocation().add(0, 1.5, 0);
        World world = loc.getWorld();

        if(world == null)
            return;

        loc.add(direction.clone().multiply(2));

        new BukkitRunnable() {
            int counter = 20;
            @Override
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    if(p.getWorld() != loc.getWorld() || p.getLocation().distance(loc) > 100)
                        continue;
                    drawBlade(loc, p, direction);
                }

                if(loc.getBlock().getType().isSolid()) {
                    if(loc.getBlock().getType().getHardness() < 0 || loc.getBlock().getType().getHardness() > .7)
                        counter = 0;
                    else
                        loc.getBlock().setType(Material.AIR);
                }

                for(Entity entity : world.getNearbyEntities(loc,  1, 1, 1)) {
                    if(entity instanceof LivingEntity livingEntity && entity != caster && entity.getType() != EntityType.ARMOR_STAND) {
                        livingEntity.damage(12 * multiplier);
                        counter = 0;
                    }
                }

                loc.add(direction);

                counter--;
                if(counter <= 0)
                    cancel();
            }
        }.runTaskTimer(Plugin.instance, 0, 1);
    }

    private void drawBlade(Location loc, Player drawPlayer, Vector direction) {
        Vector dir = direction.clone();
        dir.setY(0);
        dir.normalize().multiply(-.5);

        for(double d = 0; d < 1.25; d+=.15) {
            drawPlayer.spawnParticle(Particle.SPELL, loc.clone().add(0, d, 0).add(dir.clone().multiply(Math.pow(2.1, d))), 1, 0, 0, 0, 0);
        }
        for(double d = 0; d > -1.25; d-=.15) {
            drawPlayer.spawnParticle(Particle.SPELL, loc.clone().add(0, d, 0).add(dir.clone().multiply(Math.pow(2.1, d) * - 1)), 1, 0, 0, 0, 0);
        }
    }

    @Override
    public ItemStack getItem() {
        return TyrantItems.createItem(Material.FEATHER, "Wind Manipulation", "varying", identifier, sequence, p.getName());
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
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
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5Selected Spell: §f" + selectedCategory.name));
    }
}
