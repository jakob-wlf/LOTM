package de.firecreeper82.lotm;

import de.firecreeper82.cmds.*;
import de.firecreeper82.listeners.DeathListener;
import de.firecreeper82.listeners.InteractListener;
import de.firecreeper82.listeners.PotionHandler;
import de.firecreeper82.listeners.PotionListener;
import de.firecreeper82.pathways.Divination;
import de.firecreeper82.pathways.Pathway;
import de.firecreeper82.pathways.Potion;
import de.firecreeper82.pathways.impl.fool.FoolPotions;
import de.firecreeper82.pathways.impl.fool.abilities.FogOfHistory;
import de.firecreeper82.pathways.impl.sun.SunPotions;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

public final class Plugin extends JavaPlugin{

    public static Plugin instance;
    public static String prefix;

    public static HashMap<UUID, Beyonder> beyonders;

    public static HashMap<UUID, ServerPlayer> fakePlayers = new HashMap<>();

    public static HashMap<UUID, FogOfHistory> fogOfHistories = new HashMap<>();

    private File configSaveFile;
    private FileConfiguration configSave;

    private File configSaveFileFoh;
    private FileConfiguration configSaveFoh;

    public ArrayList<Potion> potions;
    public Divination divination;

    @Override
    public void onEnable() {
        instance = this;
        prefix = "§8§l[§5Lord of the Mysteries§8] ";

        beyonders = new HashMap<>();
        fakePlayers = new HashMap<>();

        Bukkit.getConsoleSender().sendMessage(prefix + "§aEnabled Plugin");

        createSaveConfig();

        register();
        initPotions();

        createSaveConfigFoH();
    }

    public void register() {
        ItemsCmd itemsCmd = new ItemsCmd();
        divination = new Divination();

        PluginManager pl = this.getServer().getPluginManager();
        pl.registerEvents(new InteractListener(), this);
        pl.registerEvents(itemsCmd, this);
        pl.registerEvents(new PotionHandler(), this);
        pl.registerEvents(new PotionListener(), this);
        pl.registerEvents(new DeathListener(), this);
        pl.registerEvents(divination, this);

        Objects.requireNonNull(this.getCommand("beyonder")).setExecutor(new BeyonderCmd());
        Objects.requireNonNull(this.getCommand("disable-threads")).setExecutor(new DisableThreadsCmd());
        Objects.requireNonNull(this.getCommand("configure-threads")).setExecutor(new ConfigureThreadsCmd());
        Objects.requireNonNull(this.getCommand("exclude-entities")).setExecutor(new ExcludeEntityCmd());
        Objects.requireNonNull(this.getCommand("thread-length")).setExecutor(new ThreadLengthCmd());
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

        for(FogOfHistory foh : fogOfHistories.values()) {
            try {
                saveFoH(foh);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFoH(FogOfHistory foh) throws IOException {
        Bukkit.getConsoleSender().sendMessage(prefix + "§aSaving Fog of History Inventories");

        for(int i = 0; i < foh.getItems().size(); i++) {
            configSaveFoh.set("fools." + foh.getPathway().getBeyonder().getUuid() + ("." + i), foh.getItems().get(i));
        }

        //Remove duplicates
        ArrayList<ItemStack> contained = new ArrayList<>();
        for(String s : Objects.requireNonNull(configSaveFoh.getConfigurationSection("fools")).getKeys(false)) {
            if(fogOfHistories.get(UUID.fromString(s)) == null)
                return;

            if(configSaveFoh.get("fools." + s) == null)
                return;

            for(String t : Objects.requireNonNull(configSaveFoh.getConfigurationSection("fools." + s)).getKeys(false)) {
                int i = parseInt(t);
                if(i == -1)
                    return;

                ItemStack item = configSaveFoh.getItemStack("fools." + s + "." + i);
                if(item == null)
                    continue;
                if(contained.contains(item))
                    configSaveFoh.set("fools." + s + "." + i, null);
                else
                    contained.add(item);
            }
        }

        configSaveFoh.save(configSaveFileFoh);
    }

    public void createSaveConfig() {
        configSaveFile = new File(getDataFolder(), "beyonders.yml");
        if(!configSaveFile.exists()) {
            if(configSaveFile.getParentFile().mkdirs())
                saveResource("beyonders.yml", false);
            else
                Bukkit.getConsoleSender().sendMessage("§cSomething went wrong while saving the beyonders.yml file");
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

    private void createSaveConfigFoH() {
        configSaveFileFoh = new File(getDataFolder(), "fools.yml");
        if(!configSaveFileFoh.exists()) {
            saveResource("fools.yml", true);
        }

        configSaveFoh = new YamlConfiguration();

        try {
            configSaveFoh.load(configSaveFileFoh);
        }
        catch (InvalidConfigurationException | IOException exc) {
            Bukkit.getConsoleSender().sendMessage(exc.getLocalizedMessage());
        }

        loadFoh();
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

    public void save() throws IOException {
        Bukkit.getConsoleSender().sendMessage(prefix + "§aSaving Beyonders");

        for(Map.Entry<UUID, Beyonder> entry : beyonders.entrySet()) {
            configSave.set("beyonders." + entry.getKey() + ".pathway", entry.getValue().getPathway().getNameNormalized());
            configSave.set("beyonders." + entry.getKey() + ".sequence", entry.getValue().getPathway().getSequence().getCurrentSequence());
        }
        configSave.save(configSaveFile);
    }

    public void loadFoh() {
        if(configSaveFoh.getConfigurationSection("fools") == null)
            return;

        for(String s : Objects.requireNonNull(configSaveFoh.getConfigurationSection("fools")).getKeys(false)) {
            if(fogOfHistories.get(UUID.fromString(s)) == null)
                return;

            if(configSaveFoh.get("fools." + s) == null)
                return;

            for(String t : Objects.requireNonNull(configSaveFoh.getConfigurationSection("fools." + s)).getKeys(false)) {
                int i = parseInt(t);
                if(i == -1)
                    return;

                ItemStack item = configSaveFoh.getItemStack("fools." + s + "." + i);
                if(item == null)
                    continue;
                for(FogOfHistory fogOfHistory : fogOfHistories.values()) {
                    if(fogOfHistory.getPathway().getBeyonder().getUuid().equals(UUID.fromString(s))) {
                        fogOfHistory.addItem(item);
                    }
                }
            }
        }
    }

    public Integer parseInt(String s) {
        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException exception) {
            return -1;
        }
    }

    public void load() {
        if(configSave.getConfigurationSection("beyonders") == null) {
            configSave.set("beyonders.uuid.pathway", "pathway-name");
            configSave.set("beyonders.uuid.sequence", "sequence");
        }
        for(String s : Objects.requireNonNull(configSave.getConfigurationSection("beyonders")).getKeys(false)) {
            if(s.equals("uuid"))
                continue;
            try {
                if(!configSave.contains("beyonders." + s + ".sequence") || !(configSave.get("beyonders." + s + ".sequence") instanceof Integer))
                    return;
                Integer sequence = (Integer) configSave.get("beyonders." + s + ".sequence");
                int primitiveSequence = 9;
                if(sequence != null)
                    primitiveSequence = sequence;
                Pathway pathway = Pathway.initializeNew((String) Objects.requireNonNull(configSave.get("beyonders." + s + ".pathway")), UUID.fromString(s), primitiveSequence);
                assert pathway != null;
                Beyonder beyonder = new Beyonder(UUID.fromString(s), pathway);
                Plugin.beyonders.put(UUID.fromString(s), beyonder);
                Plugin.instance.getServer().getPluginManager().registerEvents(beyonder, Plugin.instance);
            } catch (Exception exception) {
                Bukkit.getConsoleSender().sendMessage("Failed to initialize " + s);
                StringWriter sw = new StringWriter();
                exception.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                Bukkit.getConsoleSender().sendMessage("§c" + exceptionAsString);
            }
        }
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }
}
