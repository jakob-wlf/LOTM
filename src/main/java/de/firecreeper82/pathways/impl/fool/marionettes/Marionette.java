package de.firecreeper82.pathways.impl.fool.marionettes;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Pathway;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class Marionette implements Listener {

    private Mob entity;
    private Pathway pathway;
    private EntityType type;

    private boolean active;
    private boolean alive;

    private Mob currentTarget;

    private final EntityType[] rangedEntities;

    public Marionette(EntityType entityType, Location loc, Pathway pathway) {

        rangedEntities = new EntityType[]{
                EntityType.WITHER,
                EntityType.GHAST,
                EntityType.EVOKER,
                EntityType.WITCH,
                EntityType.BLAZE
        };

        World world = loc.getWorld();
        if(world == null)
            return;
        Entity e = world.spawnEntity(loc, entityType);
        if(!(e instanceof LivingEntity livingEntity))
            return;

        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);

        this.entity = (Mob) livingEntity;
        this.pathway = pathway;
        this.type = entityType;

        active = true;
        alive = true;

        entity.setRemoveWhenFarAway(false);
        entity.setCustomName("§5" + pathway.getBeyonder().getPlayer().getName() + "'s Marionette");

        pathway.getBeyonder().getMarionettes().add(this);
        pathway.getBeyonder().getMarionetteEntities().add(entity);

        new BukkitRunnable() {
            @Override
            public void run() {

                if(!active)
                    return;

                if(!alive)
                    cancel();

                if(currentTarget == entity || pathway.getBeyonder().getMarionetteEntities().contains(currentTarget))
                    currentTarget = null;

                if(currentTarget == null && pathway.getBeyonder().getPlayer().getLocation().distance(entity.getLocation()) > 2.5) {
                    if(!Arrays.asList(rangedEntities).contains(entity.getType()))
                        entity.setTarget(pathway.getBeyonder().getPlayer());
                    else {
                        if(pathway.getBeyonder().getPlayer().getLocation().distance(entity.getLocation()) > 6)
                            entity.setVelocity(pathway.getBeyonder().getPlayer().getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(.25));
                        entity.setTarget(null);
                    }
                }

                else if(currentTarget != null)
                    entity.setTarget(currentTarget);
                else
                    entity.setTarget(null);
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent e) {
        if(e.getEntity() == entity && entity.getTarget() == pathway.getBeyonder().getPlayer())
            e.setCancelled(true);
        else if(entity.getTarget() != null)
            Bukkit.getConsoleSender().sendMessage(entity.getTarget().getType().name());
        else
            Bukkit.getConsoleSender().sendMessage("No target");
    }

    @EventHandler
    public void entityDamageByEntity(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Mob m && m == currentTarget && e.getDamage() > m.getHealth())
            currentTarget = null;

        if(e.getEntity() instanceof Mob m && e.getDamager() == entity && pathway.getBeyonder().getMarionetteEntities().contains(m))
            e.setCancelled(true);

        if(e.getEntity() instanceof Mob m && m == entity && e.getDamage() > m.getHealth()) {
            pathway.getBeyonder().getMarionettes().remove(this);
            pathway.getBeyonder().getMarionetteEntities().remove(entity);
            alive = false;
        }
    }


    @EventHandler
    public void onTargetEntity(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Player p) || p != pathway.getBeyonder().getPlayer() || !(e.getEntity() instanceof Mob ent))
            return;

        currentTarget = ent;
    }

    public void removeEntity() {
        pathway.getBeyonder().getMarionetteEntities().remove(entity);
        active = false;
        entity.remove();
    }

    public void respawnEntity() {
        active = true;

        Location loc = pathway.getBeyonder().getPlayer().getLocation();
        World world = loc.getWorld();
        if(world == null)
            return;
        Entity e = world.spawnEntity(loc, type);
        if(!(e instanceof LivingEntity livingEntity))
            return;

        this.entity = (Mob) livingEntity;
    }

    public boolean isActive() {
        return active;
    }

    public EntityType getType() {
        return type;
    }
}
