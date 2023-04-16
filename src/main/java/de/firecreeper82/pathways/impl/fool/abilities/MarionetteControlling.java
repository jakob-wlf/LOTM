package de.firecreeper82.pathways.impl.fool.abilities;

import com.mojang.authlib.properties.Property;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.NPC;
import de.firecreeper82.lotm.util.UtilItems;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import de.firecreeper82.pathways.impl.fool.marionettes.Marionette;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public class MarionetteControlling extends Ability implements Listener {

    private int currentIndex;
    private boolean controlling;

    private Mob attackMob;
    private Marionette controlledMarionette;

    private Inventory playerInv;

    private final EntityType[] rangedEntities;
    private final EntityType[] flyingEntities;

    private ServerPlayer fakePlayer;

    public MarionetteControlling(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);

        currentIndex = 0;
        controlling = false;

        rangedEntities = new EntityType[] {
                EntityType.BLAZE,
                EntityType.ENDER_DRAGON,
                EntityType.EVOKER,
                EntityType.ELDER_GUARDIAN,
                EntityType.GHAST,
                EntityType.GUARDIAN,
                EntityType.ILLUSIONER,
                EntityType.PILLAGER,
                EntityType.SHULKER,
                EntityType.SKELETON,
                EntityType.STRAY,
                EntityType.WARDEN,
                EntityType.WITCH,
                EntityType.WITHER
        };

        flyingEntities = new EntityType[] {
                EntityType.BAT,
                EntityType.BEE,
                EntityType.BLAZE,
                EntityType.ENDER_DRAGON,
                EntityType.GHAST,
                EntityType.PARROT,
                EntityType.PHANTOM,
                EntityType.VEX,
                EntityType.WITHER
        };

        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        if(pathway.getBeyonder().getMarionettes().isEmpty())
            return;

        Marionette marionette = pathway.getBeyonder().getMarionettes().get(currentIndex);
        if(!marionette.isActive()) {
            p.sendTitle("", "§cThe selected marionette is not active", 10, 70, 10);
            return;
        }

        controlling = !controlling;

        if(!controlling)
            return;

        Location loc = p.getLocation();

        //Create the inventory that holds the items of the player
        playerInv = Bukkit.createInventory(p, InventoryType.PLAYER);
        playerInv.setContents(p.getInventory().getContents());

        final ItemStack attackItem = UtilItems.getAttack();

        //spawning Fake Player where Player was standing
        boolean hiding = false;
        for(Ability ability : pathway.getSequence().getAbilities()) {
            if(ability instanceof Hiding hidingAbility) {
                hiding = hidingAbility.isHiding();
            }
        }

        ServerPlayer npc;
        if(!hiding) {
            ServerPlayer player = ((CraftPlayer) p).getHandle();
            Property property = player.getGameProfile().getProperties().get("textures").iterator().next();
            String[] skin = {
                    property.getValue(),
                    property.getSignature()
            };
            npc = NPC.create(loc, p.getName(), skin, false);
            npc.setHealth((float) p.getHealth());
            npc.setNoGravity(false);

            for(ArrayList<Entity> list : Plugin.instance.getConcealedEntities()) {
                if(!list.contains(p))
                    continue;
                list.add(npc.getBukkitEntity());
            }

            fakePlayer = npc;
        }
        else
            npc = null;

        //teleporting the player to the location of the entity
        p.teleport(marionette.getEntity().getLocation());

        p.setInvisible(true);
        p.setInvulnerable(true);

        marionette.setBeingControlled(true);
        controlledMarionette = marionette;

        final boolean setFlyingTrue = Arrays.asList(flyingEntities).contains(marionette.getType()) && !p.getAllowFlight();

        if(setFlyingTrue) {
            p.setAllowFlight(true);
        }

        double radius = Arrays.asList(rangedEntities).contains(marionette.getType()) ? 100 : 5;

        //constantly teleporting entity to player
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!controlling || !marionette.isAlive() || !marionette.isBeingControlled()) {
                    controlling = false;
                    cancel();
                    return;
                }

                marionette.getEntity().teleport(p.getLocation().subtract(p.getLocation().getDirection().normalize().multiply(.75)));
                Location tempLoc = marionette.getEntity().getLocation();
                while(tempLoc.getBlock().getType().isSolid()) {
                    tempLoc.add(0, .15, 0);
                }
                marionette.getEntity().teleport(tempLoc);

                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 40, 1));


                for(Player hidePlayer : Bukkit.getOnlinePlayers()) {
                    hidePlayer.hidePlayer(Plugin.instance, p);
                }

                for(ItemStack item : p.getInventory().getContents()) {
                    if(pathway.getSequence().checkValid(item) == 0)
                        continue;

                    if(item == null)
                        continue;

                    if(item.isSimilar(attackItem))
                        continue;

                    p.getInventory().remove(item);
                }

                if(!p.getInventory().contains(attackItem))
                    p.getInventory().addItem(attackItem);

            }
        }.runTaskTimer(Plugin.instance, 0, 0);

        //Select the targeted mob
        new BukkitRunnable() {
            @Override
            public void run() {
                Vector v = p.getEyeLocation().clone().getDirection().normalize();
                Location startLoc = p.getEyeLocation().clone();
                World world = startLoc.getWorld();

                if(!controlling || controlledMarionette == null) {
                    cancel();
                    return;
                }
                if(world == null)
                    return;

                for(int i = 0; i < radius; i++) {
                    startLoc.add(v);
                    if(world.getNearbyEntities(startLoc, 2, 2, 2).isEmpty())
                        continue;

                    boolean hasFoundEntity = false;
                    for(Entity entity : world.getNearbyEntities(startLoc, 2, 2, 2)) {
                        if(!(entity instanceof Mob m))
                            continue;
                        if(entity == controlledMarionette.getEntity() || entity == p)
                            continue;
                        attackMob = m;
                        hasFoundEntity = true;
                    }
                    if(!hasFoundEntity)
                        attackMob = null;
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 10);


        //Remove the Fake Player when Player stops controlling the marionette
        new BukkitRunnable() {
            @Override
            public void run() {
                if(controlling)
                    return;

                if(npc != null) {
                    if(!Plugin.fakePlayers.containsKey(npc.getBukkitEntity().getUniqueId())) {
                        cancel();
                        return;
                    }

                    ServerLevel nmsWorld = ((CraftWorld) npc.getBukkitEntity().getWorld()).getHandle();
                    nmsWorld.removePlayerImmediately(Plugin.fakePlayers.get(npc.getBukkitEntity().getUniqueId()), net.minecraft.world.entity.Entity.RemovalReason.DISCARDED);
                    p.teleport(npc.getBukkitEntity().getLocation());
                }

                p.getInventory().remove(UtilItems.getAttack());
                p.getInventory().setContents(playerInv.getContents());
                p.setInvisible(false);
                p.setInvulnerable(false);

                fakePlayer = null;

                for(Player hidePlayer : Bukkit.getOnlinePlayers()) {
                    hidePlayer.showPlayer(Plugin.instance, p);
                }

                marionette.setBeingControlled(false);
                attackMob = null;
                controlledMarionette = null;
                if(setFlyingTrue)
                    p.setAllowFlight(false);
                cancel();
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    //Remove fall damage for Marionette
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(controlledMarionette == null)
            return;
        if(e.getEntity() != controlledMarionette.getEntity() || (e.getCause() != EntityDamageEvent.DamageCause.FALL && e.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION) ||!controlling)
            return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if(fakePlayer == null)
            return;
        if(e.getEntity() == fakePlayer.getBukkitEntity().getPlayer()) {
            controlling = false;
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        p = pathway.getBeyonder().getPlayer();

        if(controlledMarionette == null)
            return;

        if(e.getDamager() == p && e.getEntity() == controlledMarionette.getEntity())
            e.setCancelled(true);

        if(controlling && e.getDamager() == p)
            e.setCancelled(true);

        if(e.getDamager() != p || !controlling || controlledMarionette == null)
            return;

        if(!p.getInventory().getItemInMainHand().isSimilar(UtilItems.getAttack()))
            return;

        if(Arrays.asList(rangedEntities).contains(controlledMarionette.getType()))
            attack(controlledMarionette.getType(), p);

        if(attackMob == null)
            return;


        //if entity doesn't have attack damage attribute
        try {
            if(!Arrays.asList(rangedEntities).contains(controlledMarionette.getType()))
                controlledMarionette.getEntity().attack(attackMob);
        }
        catch (Exception ignored) {}
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        p = pathway.getBeyonder().getPlayer();

        if(e.getItem() == null)
            return;

        if(e.getPlayer() != p || !controlling || !UtilItems.getAttack().isSimilar(e.getItem()) || controlledMarionette == null)
            return;

        if(Arrays.asList(rangedEntities).contains(controlledMarionette.getType()))
            attack(controlledMarionette.getType(), p);

        e.setCancelled(true);

        if(attackMob == null)
            return;

        //if entity doesn't have attack damage attribute
        try {
            if(!Arrays.asList(rangedEntities).contains(controlledMarionette.getType()))
                controlledMarionette.getEntity().attack(attackMob);
        }
        catch (Exception ignored) {}

    }

    public void attack(EntityType entityType, Player p) {
        switch(entityType) {
            case BLAZE ->
                    new BukkitRunnable() {
                        int counter = 0;
                        @Override
                        public void run() {
                            SmallFireball fireball = (SmallFireball) p.getWorld().spawnEntity(p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().normalize()), EntityType.SMALL_FIREBALL);
                            fireball.setShooter(controlledMarionette.getEntity());
                            fireball.setVelocity(p.getLocation().getDirection().normalize());
                            counter++;
                            if(counter >= 3)
                                cancel();
                        }
                    }.runTaskTimer(Plugin.instance, 0, 6);

            case ENDER_DRAGON -> {
                DragonFireball fireball = (DragonFireball) p.getWorld().spawnEntity(p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().normalize()), EntityType.DRAGON_FIREBALL);
                fireball.setShooter(controlledMarionette.getEntity());
                fireball.setVelocity(p.getLocation().getDirection().normalize());
            }

            case EVOKER -> {
                int vectorMultiplier = 1;
                for(int i = 0; i < 16; i++) {
                    p.getWorld().spawnEntity(p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().normalize().multiply(vectorMultiplier)), EntityType.EVOKER_FANGS);
                    vectorMultiplier++;
                }
            }

            case GHAST -> {
                Fireball fireball = (Fireball) p.getWorld().spawnEntity(p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().normalize()), EntityType.FIREBALL);
                fireball.setShooter(controlledMarionette.getEntity());
                fireball.setVelocity(p.getLocation().getDirection().normalize());
            }

            case PILLAGER, SKELETON, STRAY -> {
                Arrow arrow = (Arrow) p.getWorld().spawnEntity(p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().normalize()), EntityType.ARROW);
                arrow.setShooter(controlledMarionette.getEntity());
                arrow.setVelocity(p.getLocation().getDirection().normalize());
            }

            case WARDEN -> {
                Location sonicLoc = p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().normalize());
                Vector vector = p.getEyeLocation().getDirection().normalize();
                World world = p.getWorld();
                for(int i = 0; i < 24; i++) {
                    sonicLoc.add(vector);
                    world.spawnParticle(Particle.SONIC_BOOM, sonicLoc, 1, 0, 0, 0 ,0);
                    if(world.getNearbyEntities(sonicLoc, 3, 3, 3).isEmpty())
                        continue;

                    for(Entity entity : world.getNearbyEntities(sonicLoc, 3, 3, 3)) {
                        if(entity instanceof LivingEntity livingEntity && entity != p && entity != controlledMarionette.getEntity()) {
                            float knockback = 10;
                            livingEntity.damage(16, controlledMarionette.getEntity());
                            Vector knockBackVector = livingEntity.getLocation().toVector().subtract(p.getEyeLocation().toVector()).normalize().multiply(knockback);
                            livingEntity.setVelocity(knockBackVector);
                        }
                    }
                }
            }

            case WITCH -> {
                ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
                PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
                assert potionMeta != null;
                potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 20 * 5, 0), true);
                itemStack.setItemMeta(potionMeta);
                ThrownPotion potion = (ThrownPotion) p.getWorld().spawnEntity(p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().normalize()), EntityType.SPLASH_POTION);
                potion.setItem(itemStack);
                potion.setVelocity(p.getLocation().getDirection().normalize());
            }

            case WITHER -> {
                WitherSkull skull = (WitherSkull) p.getWorld().spawnEntity(p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().normalize()), EntityType.WITHER_SKULL);
                skull.setCharged(true);
                skull.setShooter(controlledMarionette.getEntity());
                skull.setVelocity(p.getLocation().getDirection().normalize().multiply(3.5));
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.STRING, "Marionette Controlling", "None", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }

    @Override
    public void leftClick() {
        if(pathway.getBeyonder().getMarionettes().isEmpty())
            return;

        if(controlling) {
            p.sendTitle("", "§cYou can't switch while controlling", 10, 70, 10);
            return;
        }

        if(currentIndex == pathway.getBeyonder().getMarionettes().size() - 1)
            currentIndex = 0;
        else
            currentIndex++;
    }

    @Override
    public void onHold() {
        p = pathway.getBeyonder().getPlayer();

        if(pathway.getBeyonder().getMarionettes().isEmpty()) {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cYou don't have any Marionettes!"));
            return;
        }

        if(controlling) {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5You are currently controlling a marionette"));
            return;
        }

        while(currentIndex >= pathway.getBeyonder().getMarionettes().size()) {
            currentIndex--;
        }

        Marionette marionette = pathway.getBeyonder().getMarionettes().get(currentIndex);

        String entityName = pathway.getBeyonder().getMarionettes().get(currentIndex).getType().name().substring(0, 1).toUpperCase() + pathway.getBeyonder().getMarionettes().get(currentIndex).getType().name().substring(1).toLowerCase();
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5Selected: §7" + entityName + " §5-- §7Right-Click §5to control "));

        for(Marionette m : pathway.getBeyonder().getMarionettes()) {
            if(!m.isActive())
                continue;

            Location playerLoc = p.getEyeLocation().clone().subtract(0, .4, 0);
            Location mobLoc = m.getEntity().getLocation().clone().add(0, .5, 0);
            Vector vector = mobLoc.toVector().subtract(playerLoc.toVector()).normalize().multiply(.75);
            World world = p.getWorld();

            int[] colors = m == marionette ? new int[]{145, 0, 194} : new int[]{255, 255, 255};

            Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(colors[0], colors[1], colors[2]), .75f);

            while(playerLoc.distance(mobLoc) > 1.5) {
                playerLoc.add(vector);
                world.spawnParticle(Particle.REDSTONE, playerLoc, 2, .025, .025, .025, dust);
            }
        }
    }

    public boolean isControlling() {
        return controlling;
    }
}