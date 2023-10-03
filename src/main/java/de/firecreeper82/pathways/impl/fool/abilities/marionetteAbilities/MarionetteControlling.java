package de.firecreeper82.pathways.impl.fool.abilities.marionetteAbilities;

import de.firecreeper82.pathways.Ability;
import de.firecreeper82.pathways.Items;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.impl.fool.FoolItems;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MarionetteControlling extends Ability {

    private final SpiritBodyThreads spiritBodyThreadsAbility;

    private final Particle.DustOptions dustWhite, dustBlue;

    private Marionette selectedMarionette;
    private int index;


    //TODO: Hiding in FOH enable controlling

    public MarionetteControlling(int identifier, Pathway pathway, int sequence, Items items, SpiritBodyThreads spiritBodyThreadsAbility) {
        super(identifier, pathway, sequence, items);

        this.spiritBodyThreadsAbility = spiritBodyThreadsAbility;

        p = pathway.getBeyonder().getPlayer();
        items.addToSequenceItems(identifier - 1, sequence);

        dustBlue = new Particle.DustOptions(Color.fromRGB(0, 128, 255), .75f);
        dustWhite = new Particle.DustOptions(Color.fromRGB(255, 255, 255), .75f);

        index = 0;
    }

    @Override
    public void useAbility() {
        p = pathway.getBeyonder().getPlayer();
    }

    @Override
    public void leftClick() {
        if(spiritBodyThreadsAbility.getMarionettes().isEmpty()) {
            index = 0;
            return;
        }

        index++;

        if(index >= spiritBodyThreadsAbility.getMarionettes().size())
            index = 0;

        selectedMarionette = spiritBodyThreadsAbility.getMarionettes().get(index);
    }

    @Override
    public void onHold() {
        if(selectedMarionette == null) {
            index = 0;
            if(!spiritBodyThreadsAbility.getMarionettes().isEmpty())
                selectedMarionette = spiritBodyThreadsAbility.getMarionettes().get(index);
            return;
        }

        if(!selectedMarionette.isAlive()) {
            selectedMarionette = null;
            index = 0;
        }

        for(Marionette marionette : spiritBodyThreadsAbility.getMarionettes()) {
            if(marionette == selectedMarionette)
                drawLineToEntity(p.getEyeLocation(), marionette.getEntity().getLocation(), dustWhite);
            else
                drawLineToEntity(p.getEyeLocation(), marionette.getEntity().getLocation(), dustBlue);
        }
    }

    private void drawLineToEntity(Location startLoc, Location target, Particle.DustOptions dust) {
        Location loc = startLoc.clone();
        Vector dir = target
                .toVector()
                .subtract(loc.toVector())
                .normalize()
                .multiply(.75);

        for (int i = 0; i < target.distance(startLoc); i++) {
            p.spawnParticle(
                    Particle.REDSTONE,
                    loc,
                    1,
                    0,
                    0,
                    0,
                    dust
            );
            loc.add(dir);
        }
    }


    @Override
    public ItemStack getItem() {
        return FoolItems.createItem(Material.STRING, "Marionette Controlling", "none", identifier, sequence, pathway.getBeyonder().getPlayer().getName());
    }
}
