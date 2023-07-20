package de.firecreeper82.pathways;

import de.firecreeper82.lotm.AbilityUtilHandler;
import de.firecreeper82.lotm.Plugin;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SealedArtifacts implements CommandExecutor {

    private final HashMap<Material, String> artifactMaterials = new HashMap<>();
    private final HashMap<Integer, String[]> pathwayNames = new HashMap<>();

    public SealedArtifacts() {
        Objects.requireNonNull(Plugin.instance.getCommand("artifact")).setExecutor(this);

        String[][] materials = {
                {"STICK", "Staff"},
                {"CHEST", "Box"},
                {"DIAMOND_SWORD", "Blade"},
                {"STONE_SWORD", "Rusty Blade"},
                {"ENDER_EYE", "Eye"},
                {"SNOWBALL", "Orb"}
        };
        for (String[] mapping : materials) { artifactMaterials.put(Material.getMaterial(mapping[0]), mapping[1]); }

        addToPathwayNames(0,
                "Gold",
                "Sun",
                "Light",
                "Holiness",
                "Purification"
        );

        addToPathwayNames(1,
                "Mystery",
                "History",
                "Spirit",
                "Magic",
                "Weirdness"
        );

        addToPathwayNames(2,
                "Space",
                "Stars",
                "Door",
                "Secret",
                "Wandering"
        );

        addToPathwayNames(3,
                "Beauty",
                "Magic",
                "Destruction",
                "Ice",
                "Illness"
        );

        addToPathwayNames(4,
                "Storm",
                "Tides",
                "Waves",
                "Water",
                "Thunder"
        );
    }

    private Material getRandomMaterial() {
        List<Material> keysAsArray = new ArrayList<>(artifactMaterials.keySet());
        Random random = new Random();
        return keysAsArray.get(random.nextInt(keysAsArray.size()));
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if(!(s instanceof Player p))
            return true;

        Random random = new Random();

        int pathway = random.nextInt(pathwayNames.size());

        p.getInventory().addItem(generateArtifact(pathway, random.nextInt(AbilityUtilHandler.getAbilities().get(pathway).size()), true));
        return true;
    }

    private ItemStack generateArtifact(int pathway, int sequence, boolean isRandom) {
        List<NPCAbility> abilities = AbilityUtilHandler.getSequenceAbilities(pathway, sequence);
        Random random = new Random();

        while(abilities.size() == 0 && isRandom) {
            sequence = random.nextInt(AbilityUtilHandler.getAbilities().get(pathway).size());
            abilities = AbilityUtilHandler.getSequenceAbilities(pathway, sequence);
        }

        NPCAbility ability = abilities.get(random.nextInt(abilities.size()));

        Material material = getRandomMaterial();
        String name = artifactMaterials.get(material) + " of " + pathwayNames.get(pathway)[random.nextInt(pathwayNames.get(pathway).length)];

        return new SealedArtifact(material, name, pathway, ability).getItem();
    }

    private void addToPathwayNames(int pathway, String... names) {
        pathwayNames.put(pathway, names);
    }
}
