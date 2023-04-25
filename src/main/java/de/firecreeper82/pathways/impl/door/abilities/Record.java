package de.firecreeper82.pathways.impl.door.abilities;

import de.firecreeper82.lotm.Beyonder;
import de.firecreeper82.lotm.Plugin;
import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Recordable;
import de.firecreeper82.pathways.impl.door.DoorItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Record extends Ability {

    private boolean recording;

    public Record(int identifier, Pathway pathway, int sequence, Items items) {
        super(identifier, pathway, sequence, items);
        items.addToSequenceItems(identifier - 1, sequence);

        recording = false;
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();

        pathway.getSequence().getUsesAbilities()[identifier - 1] = true;

        Vector dir = p.getEyeLocation().getDirection().normalize();
        Location loc = p.getEyeLocation();
        if(loc.getWorld() == null)
            return;

        Beyonder target = null;

        outerloop: for(int i = 0; i < 25; i++) {
            for(Entity entity : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
                if(!(entity instanceof Player player) || entity == p)
                    continue;

                if(!Plugin.beyonders.containsKey(player.getUniqueId()))
                    continue;

                target = Plugin.beyonders.get(player.getUniqueId());

                if(target == null)
                    continue;


                break outerloop;
            }

            loc.add(dir);
        }

        if(target == null) {
            p.sendMessage("§cCouldn't find the target");
            return;
        }

        recording = true;

        target.addRecording(this);

        Record instance = this;

        Beyonder beyonder = target;
        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {

                p.spawnParticle(Particle.SPELL_WITCH, beyonder.getPlayer().getEyeLocation().subtract(0, .5, 0), 20, .5, 1, .5, 0);

                if(counter >= 20) {
                    pathway.getSequence().removeSpirituality(15);
                    counter = 0;
                }

                if(pathway.getBeyonder().getSpirituality() <= 15)
                    recording = false;

                counter++;

                if(beyonder.getPlayer().getLocation().distance(p.getLocation()) > 50) {
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    recording = false;
                    cancel();
                    beyonder.removeRecording(instance);
                    return;
                }

                if(!recording) {
                    pathway.getSequence().getUsesAbilities()[identifier - 1] = false;
                    recording = false;
                    cancel();
                    beyonder.removeRecording(instance);
                    return;
                }

                if(!pathway.getSequence().getUsesAbilities()[identifier - 1]) {
                    beyonder.removeRecording(instance);
                    cancel();
                    recording = false;
                }
            }
        }.runTaskTimer(Plugin.instance, 0, 0);
    }

    @Override
    public ItemStack getItem() {
        return DoorItems.createItem(Material.BOOK, "Record", "15/s", identifier, 8, pathway.getBeyonder().getPlayer().getName());
    }

    public void addAbility(Recordable recordable) {
        recording = false;
        pathway.getSequence().addRecordable(recordable);
        p.getInventory().addItem(recordable.getItem());
    }
}
