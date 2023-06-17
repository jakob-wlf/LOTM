package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import de.firecreeper82.pathways.impl.fool.marionettes.BeyonderMarionette;
import de.firecreeper82.pathways.impl.fool.marionettes.Marionette;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SpiritBodyThreads extends Ability implements Listener {

    private final HashMap<Integer, int[]> mobColors;
    private final HashMap<EntityCategory, Integer> categoryToInt;
    private final HashMap<String, EntityCategory> stringToCategory;

    private final HashMap<Integer, int[]> sequenceConversions;

    private final ArrayList<String> disabledCategories;
    private final ArrayList<EntityType> excludedEntities;
    private final ArrayList<EntityType> includedEntities;

    private boolean excluded;

    @SuppressWarnings("all")
    private final ArrayList<Entity> marionettes;

    private Entity selectedEntity;

    private int maxDistance;
    private int maxDistanceControl;
    private int preferredDistance;

    private int convertTimeSeconds;

    @SuppressWarnings("unused")
    private int maxMarionettes;

    private boolean turning;
    private int sequence;

    public SpiritBodyThreads(int identifier, Pathway pathway, int sequenceAbility, Items items) {
        super(identifier, pathway, sequenceAbility, items);

        items.addToSequenceItems(identifier - 1, sequenceAbility);

        Plugin.instance.getServer().getPluginManager().registerEvents(this, Plugin.instance);

        disabledCategories = new ArrayList<>();
        excludedEntities = new ArrayList<>();
        includedEntities = new ArrayList<>();
        excluded = true;

        marionettes = new ArrayList<>();

        mobColors = new HashMap<>();
        categoryToInt = new HashMap<>();
        stringToCategory = new HashMap<>();
        sequenceConversions = new HashMap<>();

        categoryToInt.put(EntityCategory.UNDEAD, 1);
        categoryToInt.put(EntityCategory.ARTHROPOD, 2);
        categoryToInt.put(EntityCategory.ILLAGER, 3);
        categoryToInt.put(EntityCategory.NONE, 4);
        categoryToInt.put(EntityCategory.WATER, 4);

        mobColors.put(0, new int[]{78, 78, 78});
        mobColors.put(1, new int[]{75, 133, 0});
        mobColors.put(2, new int[]{57, 0, 133});
        mobColors.put(3, new int[]{87, 43, 0});
        mobColors.put(4, new int[]{0, 38, 69});

        stringToCategory.put("undead", EntityCategory.UNDEAD);
        stringToCategory.put("normal", EntityCategory.NONE);
        stringToCategory.put("illager", EntityCategory.ILLAGER);
        stringToCategory.put("arthropod", EntityCategory.ARTHROPOD);

        sequence = 5;

        sequenceConversions.put(5, new int[]{50, 10, 90, 3});
        sequenceConversions.put(4, new int[]{200, 150, 15, 50});
        sequenceConversions.put(3, new int[]{500, 500, 10, 500});
        sequenceConversions.put(2, new int[]{1000, 750, 6, 5000});
        sequenceConversions.put(1, new int[]{2000, 750, 5, 5000});

        maxDistance = sequenceConversions.get(5)[0];
        maxDistanceControl = sequenceConversions.get(5)[1];
        preferredDistance = maxDistance;

        convertTimeSeconds = sequenceConversions.get(5)[2];

        maxMarionettes = sequenceConversions.get(5)[3];

        turning = false;

        p = pathway.getBeyonder().getPlayer();
    }


    @EventHandler
    //Check if turning process was interrupted by damage
    public void onDamage(EntityDamageEvent e) {
        if (turning && e.getEntity() == selectedEntity)
            turning = false;
    }

    @Override
    //Check if Player is already turning something into Marionette
    // if not -> calls the turningIntoMarionette function
    // else -> stops the turning process
    public void useAbility() {

        if (selectedEntity == null)
            return;

        if (!turning) {
            turnIntoMarionette(selectedEntity);
            return;
        }

        turning = false;
    }

    public void turnIntoMarionette(Entity e) {
        if (!(e instanceof LivingEntity) || (!(e instanceof Player) && e.getType() != EntityType.PLAYER)) {
            turning = false;
            return;
        }
        Player p = pathway.getBeyonder().getPlayer();

        //Make hostile entities aware of Player
        ((Damageable) e).damage(0, p);

        turning = true;


        final int beyonderMultiplier = (Plugin.beyonders.containsKey(e.getUniqueId()) && Plugin.beyonders.get(e.getUniqueId()).getPathway() != null && Plugin.beyonders.get(e.getUniqueId()).getPathway().getSequence() != null) ? (9 / Plugin.beyonders.get(e.getUniqueId()).getPathway().getSequence().getCurrentSequence()) : 1;

        //Runs every 1/2 seconds and gives Entity effects
        //At the end of the time if entity is still being turned, removes entity
        new BukkitRunnable() {
            long counter = 2L * convertTimeSeconds * beyonderMultiplier;

            @Override
            public void run() {
                if (!turning) {
                    cancel();
                    return;
                }

                //Check if entity is too far away
                Location entityLoc = e.getLocation().clone();
                entityLoc.add(0, 0.75, 0);
                if (entityLoc.distance(p.getEyeLocation()) > maxDistanceControl) {
                    turning = false;
                    cancel();
                    return;
                }

                counter--;


                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 5));
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 4));
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 1));

                if (!turning)
                    cancel();
                if (counter <= 0) {
                    turning = false;

                    //Marionette is a Player
                    if (e instanceof Player) {
                        ((Player) e).setHealth(0);
                        return;
                    } else {
                        if (selectedEntity.getMetadata("customEntityId").isEmpty())
                            new Marionette(selectedEntity.getType(), selectedEntity.getLocation(), pathway);
                        else
                            new BeyonderMarionette((String) selectedEntity.getMetadata("customEntityId").get(0).value(), selectedEntity.getLocation(), pathway);
                        selectedEntity.remove();
                    }

                    cancel();
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 10);

        //Particle effects for Entities
        new BukkitRunnable() {
            long counter = 10L * convertTimeSeconds;
            double spiralRadius = 2;

            double spiral = 0;
            double height = 0;
            double spiralX;
            double spiralZ;

            @Override
            public void run() {
                Location entityLoc = e.getLocation().clone();
                entityLoc.add(0, 0.75, 0);

                spiralX = spiralRadius * Math.cos(spiral);
                spiralZ = spiralRadius * Math.sin(spiral);
                spiral += 0.25;
                height += .05;
                if (height >= 2.5)
                    height = 0;
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(154, 0, 194), 1.25f);
                if (entityLoc.getWorld() != null)
                    entityLoc.getWorld().spawnParticle(Particle.REDSTONE, spiralX + entityLoc.getX(), height + entityLoc.getY(), spiralZ + entityLoc.getZ(), 5, dust);

                counter--;
                spiralRadius -= (1.5 / (10L * convertTimeSeconds));

                if (!turning)
                    cancel();
                if (counter <= 0) {
                    cancel();
                }

            }
        }.runTaskTimer(Plugin.instance, 0, 2);
    }


    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.LEAD, "Spirit Body Threads", "100", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }

    @Override
    public void onHold() {
        Player p = pathway.getBeyonder().getPlayer();

        //Check if sequence has updated and adjust values accordingly
        if (sequence != pathway.getSequence().getCurrentSequence()) {
            if (pathway.getSequence().getCurrentSequence() > 5)
                return;
            sequence = pathway.getSequence().getCurrentSequence();
            maxDistance = sequenceConversions.get(sequence)[0];
            maxDistanceControl = sequenceConversions.get(sequence)[1];

            convertTimeSeconds = sequenceConversions.get(sequence)[2];

            maxMarionettes = sequenceConversions.get(sequence)[3];
        }

        //Loop through hall entities and check their respective color and "draw" the Thread
        //Indicate selected entity on the actionbar
        outerloop:
        for (Entity e : p.getNearbyEntities(preferredDistance, preferredDistance, preferredDistance)) {
            if (e == p || !(e instanceof LivingEntity))
                continue;

            //Check if Thread is disabled via disable-thread command
            if (e instanceof Player && disabledCategories.contains("player"))
                continue;
            EntityCategory entityCategory = normalizeCategory(((LivingEntity) e).getCategory());
            for (String s : disabledCategories) {
                if (s.equals("player"))
                    continue;
                if (entityCategory == stringToCategory.get(s) && !(e instanceof Player))
                    continue outerloop;
            }

            //Check if entity is in the excludedEntities and if the mode is set to excluded
            if (excludedEntities.contains(e.getType()) && excluded)
                continue;

            //Check if entity is in the includedEntities and if the mode is set to !excluded
            if (!includedEntities.contains(e.getType()) && !excluded)
                continue;

            //Check if entity is already a Marionette
            if (e instanceof Mob m) {
                if (pathway.getBeyonder().getMarionetteEntities().contains(m))
                    continue;

                if (pathway.getBeyonder().getBeyonderMarionetteEntities().contains(m))
                    continue;
            }

            //Randomly sets the selected entity to an entity in the control range
            if (selectedEntity == null && e.getLocation().clone().add(0, 0.75, 0).distance(p.getEyeLocation()) <= maxDistanceControl) {
                selectedEntity = e;
            }

            if (selectedEntity != null && (selectedEntity.getWorld() != p.getWorld() || selectedEntity.getLocation().distance(p.getLocation()) > maxDistanceControl)) {
                selectedEntity = null;
            }

            //Getting the colors
            int[] colors;
            if (e == selectedEntity)
                colors = new int[]{255, 255, 255};
            else if (e instanceof Player)
                colors = mobColors.get(0);
            else
                colors = mobColors.get(categoryToInt.get(((LivingEntity) e).getCategory()));

            //Check if currently turning Entity into Marionette
            if (turning) {
                if (e != selectedEntity)
                    continue;
                colors = new int[]{145, 0, 194};
            }


            //Drawing the threads
            Location entityLoc = e.getLocation().clone().add(0, 0.75, 0);
            Location playerLoc = p.getEyeLocation().clone().subtract(0, 0.5, 0);
            Vector dir = entityLoc.toVector().subtract(playerLoc.toVector()).normalize().multiply(.65);

            int counter = 0;
            while (playerLoc.distance(entityLoc) > .5 && counter < 150) {
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(colors[0], colors[1], colors[2]), .75f);
                p.spawnParticle(Particle.REDSTONE, playerLoc, 1, .05, 0, .05, dust);
                playerLoc.add(dir);
                counter++;
            }

            //Displaying the actionbar
            String entityName;
            if (selectedEntity == null)
                entityName = "None";
            else
                entityName = selectedEntity.getType().name().substring(0, 1).toUpperCase() + selectedEntity.getType().name().substring(1).toLowerCase();
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5Selected: §8" + entityName));
        }
    }

    @Override
    public void leftClick() {
        Player p = pathway.getBeyonder().getPlayer();

        //Loop through hall entities and check their respective color and "draw" the Thread
        //Indicate selected entity on the actionbar
        List<Entity> entities = p.getNearbyEntities(preferredDistance, preferredDistance, preferredDistance);
        Collections.shuffle(entities);
        outerloop:
        for (Entity e : entities) {
            if (e == p || !(e instanceof LivingEntity) || e == selectedEntity)
                continue;

            //Check if Thread is disabled via disable-thread command
            if (e instanceof Player && disabledCategories.contains("player"))
                return;
            EntityCategory entityCategory = normalizeCategory(((LivingEntity) e).getCategory());
            for (String s : disabledCategories) {
                if (s.equals("player"))
                    continue;
                if (entityCategory == stringToCategory.get(s) && !(e instanceof Player))
                    continue outerloop;
            }

            //Check if entity is in the excludedEntities and if the mode is set to excluded
            if (excludedEntities.contains(e.getType()) && excluded)
                continue;

            //Check if entity is in the includedEntities and if the mode is set to !excluded
            if (!includedEntities.contains(e.getType()) && !excluded)
                continue;

            //Check if entity is already a Marionette
            if (e instanceof Mob m) {
                if (pathway.getBeyonder().getMarionetteEntities().contains(m))
                    continue;
            }


            //Randomly sets the selected entity to an entity in the control range
            if (e.getLocation().distance(p.getLocation()) <= maxDistanceControl) {
                selectedEntity = e;
                break;
            }
        }
    }

    //Disable / Enable a specific EntityCategory for the Threads
    //Return true if disabling and false if enabling
    public boolean disableCategory(String category) {
        if (!disabledCategories.contains(category.toLowerCase())) {
            disabledCategories.add(category.toLowerCase());
            return true;
        } else {
            disabledCategories.remove(category.toLowerCase());
            return false;
        }
    }

    //Disable / Enable a specific Entity for the Threads
    //Return true if disabling and false if enabling
    public boolean addExcludedEntity(EntityType entityType) {
        if (!excludedEntities.contains(entityType)) {
            excludedEntities.add(entityType);
            return true;
        } else {
            excludedEntities.remove(entityType);
            return false;
        }
    }

    //Disable / Enable a specific Entity for the Threads
    //Return true if enabling and false if disabling
    public boolean addIncludedEntity(EntityType entityType) {
        if (!includedEntities.contains(entityType)) {
            includedEntities.add(entityType);
            return true;
        } else {
            includedEntities.remove(entityType);
            return false;
        }
    }

    public boolean isExcluded() {
        return excluded;
    }

    public void setExcluded(boolean excluded) {
        this.excluded = excluded;
    }

    public void resetExcludedEntities() {
        excludedEntities.clear();
    }

    public void setPreferredDistance(int distance) {
        preferredDistance = Math.min(distance, maxDistance);
    }

    //If given EntityCategory.WATER returns EntityCategory.NONE
    public EntityCategory normalizeCategory(EntityCategory entityCategory) {
        if (entityCategory == EntityCategory.WATER)
            return EntityCategory.NONE;

        return entityCategory;
    }

    public int getMaxDistance() {
        return maxDistance;
    }
}
