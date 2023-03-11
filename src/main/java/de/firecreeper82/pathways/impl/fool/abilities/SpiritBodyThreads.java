package de.firecreeper82.pathways.impl.fool.abilities;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class SpiritBodyThreads extends Ability {

    private final HashMap<Integer, int[]> mobColors;
    private final HashMap<EntityCategory, Integer> categoryToInt;
    private final HashMap<String, EntityCategory> stringToCategory;

    private final ArrayList<String> disabledCategories;

    private Entity selectedEntity;

    private int maxDistance;
    private int maxDistanceControl;

    private int preferredDistance;

    public SpiritBodyThreads(int identifier, Pathway pathway) {
        super(identifier, pathway);

        disabledCategories = new ArrayList<>();

        mobColors = new HashMap<>();
        categoryToInt = new HashMap<>();
        stringToCategory = new HashMap<>();

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

        maxDistance = 50;
        maxDistanceControl = 10;
        preferredDistance = maxDistance;
    }

    @Override
    public void useAbility() {

    }

    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.LEAD, "Spirit Body Threads", "100", identifier, 5, pathway.getBeyonder().getPlayer().getName());
    }

    @Override
    public void onHold() {
        Player p = pathway.getBeyonder().getPlayer();

        //Loop through hall entities and check their respective color and "draw" the Thread
        //Indicate selected entity on the actionbar
        outerloop: for(Entity e : p.getNearbyEntities(preferredDistance, preferredDistance, preferredDistance)) {
            if(e == p || !(e instanceof LivingEntity))
                continue;

            //Check if Thread is disabled via disable-thread command
            if(e instanceof Player && disabledCategories.contains("player"))
                continue ;
            EntityCategory entityCategory = normalizeCategory(((LivingEntity) e).getCategory());
            for(String s : disabledCategories) {
                if(s.equals("player"))
                    continue;
                if(entityCategory == stringToCategory.get(s))
                    continue outerloop;
            }

            //Randomly sets the selected entity to an entity in the control range
            if(selectedEntity == null && e.getLocation().distance(p.getLocation()) <= maxDistanceControl) {
                selectedEntity = e;
            }

            if(selectedEntity != null && selectedEntity.getLocation().distance(p.getLocation()) > maxDistanceControl) {
                selectedEntity = null;
            }

            //Getting the colors
            int[] colors;
            if(e instanceof Player)
                colors = mobColors.get(0);
            else if(e == selectedEntity)
                colors = new int[]{255, 255, 255};
            else
                colors = mobColors.get(categoryToInt.get(((LivingEntity) e).getCategory()));


            //Drawing the threads
            Location entityLoc = e.getLocation();
            Location playerLoc = p.getEyeLocation();
            Vector dir = entityLoc.toVector().subtract(playerLoc.toVector()).normalize().multiply(1);

            int counter = 0;
            while(playerLoc.distance(entityLoc) > .5 && counter < 150) {
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromBGR(colors[0], colors[1], colors[2]), .6f);
                p.spawnParticle(Particle.REDSTONE, playerLoc, 1, .075, 0, .075, dust);
                playerLoc.add(dir);
                counter++;
            }

            //Displaying the actionbar
            String entityName;
            if(selectedEntity == null)
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
        outerloop: for(Entity e : entities) {
            if (e == p || !(e instanceof LivingEntity) || e == selectedEntity)
                continue;

            //Check if Thread is disabled via disable-thread command
            if (e instanceof Player && disabledCategories.contains("player"))
                return;
            EntityCategory entityCategory = normalizeCategory(((LivingEntity) e).getCategory());
            for (String s : disabledCategories) {
                if (s.equals("player"))
                    continue;
                if (entityCategory == stringToCategory.get(s))
                    continue outerloop;
            }

            //Randomly sets the selected entity to an entity in the control range
            if (e.getLocation().distance(p.getLocation()) <= maxDistanceControl) {
                selectedEntity = e;
                break;
            }
        }
    }

    public boolean disableCategory(String category) {
        if(!disabledCategories.contains(category.toLowerCase())) {
            disabledCategories.add(category.toLowerCase());
            return true;
        }
        else {
            disabledCategories.remove(category.toLowerCase());
            return false;
        }
    }

    public void setPreferredDistance(int distance) {
        preferredDistance = Math.min(distance, maxDistance);
    }

    public EntityCategory normalizeCategory(EntityCategory entityCategory) {
        if(entityCategory == EntityCategory.WATER)
            return EntityCategory.NONE;

        return entityCategory;
    }
}
