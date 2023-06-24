package de.firecreeper82.pathways.impl.tyrant.abilities;

import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.lotm.util.Util;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.NPCAbility;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.tyrant.TyrantItems;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class SirenSong extends NPCAbility {

    private Category selectedCategory = Category.CHAOTIC;
    private final Category[] categories = Category.values();
    private int selected = 0;

    public SirenSong(int identifier, Pathway pathway, int sequence, Items items, boolean npc) {
        super(identifier, pathway, sequence, items);

        if(!npc)
            items.addToSequenceItems(identifier - 1, sequence);
        p = pathway.getBeyonder().getPlayer();
    }

    enum Category {
        CHAOTIC("§9Chaotic song"),
        BOOST("§9Boost your strength");

        private final String name;

        Category(String name) {
            this.name = name;
        }
    }

    @Override
    public void useAbility() {
        switch (selectedCategory) {
            case CHAOTIC -> chaotic(p, getMultiplier());
            case BOOST -> boost(p);
        }
    }

    private void chaotic(Entity caster, double multiplier) {

        new BukkitRunnable() {
            int counter = 30 * 2;
            @Override
            public void run(){

                counter--;
                if(counter <= 0)
                    cancel();

                Util.drawParticlesForNearbyPlayers(Particle.NOTE, caster.getLocation(), 100, 10, 10, 10, 0);
            }
        }.runTaskTimer(Plugin.instance, 0, 10);

        new BukkitRunnable() {
            int counter = 30 / 2;
            @Override
            public void run(){

                counter--;
                if(counter <= 0)
                    cancel();

                Util.damageNearbyEntities(caster, caster.getLocation(), 10, 10, 10, 1.5 * multiplier);
                if(counter % 2 == 0) {
                    Util.effectForNearbyEntities(caster, caster.getLocation(), 20, 20, 20, new PotionEffect(PotionEffectType.CONFUSION, 20 * 10, 1));
                    Util.effectForNearbyEntities(caster, caster.getLocation(), 20, 20, 20, new PotionEffect(PotionEffectType.WEAKNESS, 20 * 10, 1));
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 40);
    }

    private void boost(Entity caster) {

        new BukkitRunnable() {
            int counter = 30 * 2;
            @Override
            public void run(){

                counter--;
                if(counter <= 0)
                    cancel();

                Util.drawParticlesForNearbyPlayers(Particle.NOTE, caster.getLocation(), 100, 10, 10, 10, 0);
            }
        }.runTaskTimer(Plugin.instance, 0, 10);

        new BukkitRunnable() {
            int counter = 30 / 2;
            @Override
            public void run(){

                counter--;
                if(counter <= 0)
                    cancel();

                if(caster instanceof LivingEntity livingEntity) {
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 8, 2, false, false));
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 8, 2, false, false));
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 8, 2, false, false));
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 40);
    }

    @Override
    public ItemStack getItem() {
        p = pathway.getBeyonder().getPlayer();
        return TyrantItems.createItem(Material.MUSIC_DISC_MALL, "Siren Song", "400", identifier, sequence, p.getName());
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
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§5Selected Song: §f" + selectedCategory.name));
    }
}
