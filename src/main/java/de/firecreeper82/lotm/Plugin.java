package de.firecreeper82.lotm;

import de.firecreeper82.cmds.ItemsCmd;
import de.firecreeper82.cmds.BeyonderCmd;
import de.firecreeper82.listeners.InteractListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Plugin extends JavaPlugin {

    public static Plugin instance;
    public static String prefix;

    public static HashMap<UUID, Beyonder> beyonders;

    private File configSaveFile;
    private FileConfiguration configSave;

    @Override
    public void onEnable() {
        instance = this;
        prefix = "§8§l[§5Lord of the Mysteries§8] ";

        beyonders = new HashMap<>();

        Bukkit.getConsoleSender().sendMessage(prefix + "§aEnabled Plugin");

        createSaveConfig();
        load();

        register();
    }

    public void register() {
        ItemsCmd itemsCmd = new ItemsCmd();

        PluginManager pl = this.getServer().getPluginManager();
        pl.registerEvents(new InteractListener(), this);
        pl.registerEvents(itemsCmd, this);

        this.getCommand("beyonder").setExecutor(new BeyonderCmd());
        this.getCommand("items").setExecutor(itemsCmd);
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
            configSaveFile.getParentFile().mkdirs();
            saveResource("save.yml", false);
        }

        configSave = new YamlConfiguration();
        YamlConfiguration.loadConfiguration(configSaveFile);
    }

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

    }
}
