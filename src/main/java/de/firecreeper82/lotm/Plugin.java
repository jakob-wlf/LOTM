package de.firecreeper82.lotm;

import de.firecreeper82.cmds.ItemsCmd;
import de.firecreeper82.cmds.BeyonderCmd;
import de.firecreeper82.listeners.InteractListener;
import de.firecreeper82.listeners.PotionHandler;
import de.firecreeper82.listeners.PotionListener;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Potion;
import de.firecreeper82.pathways.impl.fool.FoolPotions;
import de.firecreeper82.pathways.impl.sun.SunPotions;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

public final class Plugin extends JavaPlugin {

    public static Plugin instance;
    public static String prefix;

    public static HashMap<UUID, Beyonder> beyonders;

    private File configSaveFile;
    private FileConfiguration configSave;

    public ArrayList<Potion> potions;

    @Override
    public void onEnable() {
        instance = this;
        prefix = "§8§l[§5Lord of the Mysteries§8] ";

        beyonders = new HashMap<>();

        Bukkit.getConsoleSender().sendMessage(prefix + "§aEnabled Plugin");

        createSaveConfig();

        register();
        initPotions();
    }

    public void register() {
        ItemsCmd itemsCmd = new ItemsCmd();

        PluginManager pl = this.getServer().getPluginManager();
        pl.registerEvents(new InteractListener(), this);
        pl.registerEvents(itemsCmd, this);
        pl.registerEvents(new PotionHandler(), this);
        pl.registerEvents(new PotionListener(), this);

        Objects.requireNonNull(this.getCommand("beyonder")).setExecutor(new BeyonderCmd());
        Objects.requireNonNull(this.getCommand("items")).setExecutor(itemsCmd);
    }

    public void initPotions() {
        potions = new ArrayList<>();
        potions.add(new SunPotions());
        potions.add(new FoolPotions());
    }

    @Override
    public void onDisable() {
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createSaveConfig() {
        configSaveFile = new File(getDataFolder(), "save.yml");
        if(!configSaveFile.exists()) {
            if(configSaveFile.getParentFile().mkdirs())
                saveResource("save.yml", false);
            else
                Bukkit.getConsoleSender().sendMessage("§cSomething went wrong while saving the save.yml file");
        }

        configSave = new YamlConfiguration();
        try {
            configSave.load(configSaveFile);
        }
        catch (InvalidConfigurationException | IOException exc) {
            Bukkit.getConsoleSender().sendMessage(exc.getLocalizedMessage());
        }
        load();
    }

    public void removeBeyonder(UUID uuid) {
        beyonders.remove(uuid);
        configSave.set("beyonders." + uuid, null);
        try {
            configSave.save(configSaveFile);
        }
        catch(IOException exc) {
            Bukkit.getConsoleSender().sendMessage(exc.getLocalizedMessage());
        }
    }

    @SuppressWarnings("unused") //Maybe used later on
    public FileConfiguration getSaveConfig() {
        return configSave;
    }

    public void save() throws IOException {
        Bukkit.getConsoleSender().sendMessage(prefix + "§aSaving Beyonders");
        for(Map.Entry<UUID, Beyonder> entry : beyonders.entrySet()) {
            configSave.set("beyonders." + entry.getKey() + ".pathway", entry.getValue().getPathway().getNameNormalized());
            configSave.set("beyonders." + entry.getKey() + ".sequence", entry.getValue().getPathway().getSequence().currentSequence);
        }
        configSave.save(configSaveFile);
    }

    public void load() {
        for(String s : Objects.requireNonNull(configSave.getConfigurationSection("beyonders")).getKeys(false)) {
            try {
                if(!configSave.contains("beyonders." + s + ".sequence") || !(configSave.get("beyonders." + s + ".sequence") instanceof Integer))
                    return;
                Player p = Bukkit.getPlayer(UUID.fromString(s));
                Pathway pathway = Pathway.initializeNew((String) Objects.requireNonNull(configSave.get("beyonders." + s + ".pathway")), UUID.fromString(s), (Integer) configSave.get("beyonders." + s + ".sequence"));
                assert p != null;
                assert pathway != null;
                Beyonder beyonder = new Beyonder(p.getUniqueId(), pathway);
                Plugin.beyonders.put(p.getUniqueId(), beyonder);
                Plugin.instance.getServer().getPluginManager().registerEvents(beyonder, Plugin.instance);
            } catch (Exception exception) {
                Bukkit.getConsoleSender().sendMessage("Failed to initialize " + s);
            }
        }
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }
}
