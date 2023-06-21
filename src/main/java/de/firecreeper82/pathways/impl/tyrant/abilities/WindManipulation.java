package de.firecreeper82.pathways.impl.tyrant.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.tyrant.TyrantItems;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class WindManipulation extends NPCAbility {

    private Category selectedCategory = Category.BLADE;
    private final Category[] categories = Category.values();
    private int selected = 0;

    private final boolean npc;

    private boolean flying;

    public WindManipulation(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);
        this.npc = npc;
        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);
        p = pathway.getBeyonder().getPlayer();
        flying = false;
    }

    enum Category {
        BLADE("§9Wind Blade"),
        FLIGHT("§9Flight"),
        BOOST("§9Boost"),
        BIND("§9Wind Binding");

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
            case BOOST -> boost(p);
            case FLIGHT -> flight(p);
            case BIND -> bind(p);
        }
    }

    private void bind(Entity caster) {
        Vector dir = caster.getLocation().getDirection().normalize();
        Location loc = caster.getLocation().add(0, 1.5, 0);
        if (loc.getWorld() == null)
            return;

        Entity target = null;

        outerloop:
        for (int i = 0; i < 50; i++) {
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if (entity.getType() == EntityType.ARMOR_STAND || entity == caster)
                    continue;
                target = entity;
                break outerloop;
            }

            loc.add(dir);

            if (loc.getBlock().getType().isSolid()) {
                break;
            }
        }

        if (target == null) {
            return;
        }

        Entity finalTarget = target;

        if(pathway.getBeyonder().getSpirituality() <= 25)
            return;

        pathway.getSequence().removeSpirituality(25);

        if(finalTarget instanceof LivingEntity livingEntity) {
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 25, 8, false, false));
        }

        for(int i = 0; i < 10; i++) {
            new BukkitRunnable() {
                final double spiralRadius = finalTarget.getWidth();

                double spiral = 0;
                double height = 0;
                double spiralX;
                double spiralZ;

                double counter = 20 * 25;

                @Override
                public void run() {
                    Location entityLoc = finalTarget.getLocation().clone();
                    entityLoc.add(0, -0.75, 0);

                    counter--;
                    if(counter <= 0) {
                        cancel();
                        return;
                    }

                    spiralX = spiralRadius * Math.cos(spiral);
                    spiralZ = spiralRadius * Math.sin(spiral);
                    spiral += 0.25;
                    height += .025;
                    if (height >= 2.3) {
                        height = 0;
                    }

                    if (entityLoc.getWorld() == null)
                        return;

                    for(Player player : Bukkit.getOnlinePlayers()) {
                        if(player.getWorld() != entityLoc.getWorld() || player.getLocation().distance(entityLoc) > 100)
                            continue;
                        player.spawnParticle(Particle.SPELL, spiralX + entityLoc.getX(), height + entityLoc.getY(), spiralZ + entityLoc.getZ(), 5, 0, 0, 0, 0);
                    }
                }
            }.runTaskTimer(Plugin.instance, i * 15, 2);
        }

    }

    private void boost(Entity caster) {
        if(!caster.isOnGround() && !flying) {
            caster.sendMessage("§cYou have to be on ground to use this ability!");
            return;
        }

        if(pathway.getBeyonder().getSpirituality() <= 20)
            return;

        pathway.getSequence().removeSpirituality(20);

        caster.setVelocity(caster.getLocation().getDirection().normalize().multiply(5));
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.getWorld() != caster.getWorld() || p.getLocation().distance(caster.getLocation()) > 100)
                continue;

            p.spawnParticle(Particle.CLOUD, caster.getLocation(), 100, .05, .05, .05, .65);
        }
    }

    private void flight(Player caster) {
        flying = !flying;

        if(!flying)
            return;

        boolean allowFlight = caster.getAllowFlight();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(pathway.getBeyonder().getSpirituality() <= 6) {
                    flying = false;
                    cancel();
                    return;
                }

                pathway.getSequence().removeSpirituality(6);

                if(!flying)
                    cancel();
            }
        }.runTaskTimer(Plugin.instance, 0, 20);

        for(int i = 0; i < 12; i++) {
            new BukkitRunnable() {
                double spiralRadius = .1;

                double spiral = 0;
                double height = 0;
                double spiralX;
                double spiralZ;

                @Override
                public void run() {
                    Location entityLoc = caster.getLocation().clone();
                    entityLoc.add(0, -0.75, 0);

                    spiralX = spiralRadius * Math.cos(spiral);
                    spiralZ = spiralRadius * Math.sin(spiral);
                    spiral += 0.25;
                    height += .025;
                    spiralRadius += .015;
                    if (height >= 2.3) {
                        height = 0;
                        spiralRadius = .1;
                    }

                    if (entityLoc.getWorld() == null)
                        return;

                    for(Player player : Bukkit.getOnlinePlayers()) {
                        if(player.getWorld() != entityLoc.getWorld() || player.getLocation().distance(entityLoc) > 100)
                            continue;
                        player.spawnParticle(Particle.SPELL, spiralX + entityLoc.getX(), height + entityLoc.getY(), spiralZ + entityLoc.getZ(), 5, 0, 0, 0, 0);
                    }

                    caster.setAllowFlight(true);
                    caster.setFlying(true);

                    if(!flying) {
                        caster.setAllowFlight(allowFlight);
                        cancel();
                    }
                }
            }.runTaskTimer(Plugin.instance, i * 15, 2);
        }
    }


    private void blade(Entity caster, double multiplier) {
        Vector direction = caster.getLocation().getDirection().normalize();
        Location loc = caster.getLocation().add(0, 1.5, 0);
        World world = loc.getWorld();

        if(world == null)
            return;

        loc.add(direction.clone().multiply(2));

        if(pathway.getBeyonder().getSpirituality() <= 45)
            return;
        pathway.getSequence().removeSpirituality(45);

        world.playSound(loc, Sound.ENTITY_ARROW_SHOOT, 1, 1);

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

                for(Entity entity : world.getNearbyEntities(loc,  1, 3, 1)) {
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

        Random random = new Random();

        for(double d = 0; d < 1.75; d+=.15) {
            drawPlayer.spawnParticle(Particle.SPELL, loc.clone().add(0, d, 0).add(dir.clone().multiply(Math.pow(2.25, d))), 1, 0, 0, 0, 0);
            if(random.nextInt(4) == 0)
                drawPlayer.spawnParticle(Particle.CLOUD, loc.clone().add(0, d, 0).add(dir.clone().multiply(Math.pow(2.5, d))), 1, 0, 0, 0, 0);
        }
        for(double d = 0; d > -1.75; d-=.15) {
            drawPlayer.spawnParticle(Particle.SPELL, loc.clone().add(0, d, 0).add(dir.clone().multiply(Math.pow(2.25, d * -1))), 1, 0, 0, 0, 0);
            if(random.nextInt(4) == 0)
                drawPlayer.spawnParticle(Particle.CLOUD, loc.clone().add(0, d, 0).add(dir.clone().multiply(Math.pow(2.5, d))), 1, 0, 0, 0, 0);
        }
    }

    @Override
    public ItemStack getItem() {
        return TyrantItems.createItem(Material.FEATHER, "Wind Manipulation", "varying", identifier, sequence, p.getName());
    }

    @Override
    public void useNPCAbility(Location loc, Entity caster, double multiplier) {
        switch ((new Random()).nextInt(2)) {
            case 0 -> bind(caster);
            case 1 -> blade(caster, multiplier);
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
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5Selected Spell: §f" + selectedCategory.name));
    }
}
