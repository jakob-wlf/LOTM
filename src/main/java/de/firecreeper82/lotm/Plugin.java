package de.firecreeper82.lotm;

import de.firecreeper82.cmds.ItemsCmd;
import de.firecreeper82.cmds.BeyonderCmd;
import de.firecreeper82.listeners.InteractListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Plugin extends JavaPlugin {

    public static Plugin instance;
    public static String prefix;

    public static HashMap<UUID, Beyonder> beyonders;

    @Override
    public void onEnable() {
        instance = this;
        prefix = "§8§l[§5Lord of the Mysteries§8] ";

        beyonders = new HashMap<>();

        Bukkit.getConsoleSender().sendMessage(prefix + "§aEnabled Plugin");

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

    }
}
