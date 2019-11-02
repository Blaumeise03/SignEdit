/*
 * Copyright (c) 2019 Blaumeise03
 */

package de.blaumeise03.signEdit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SignEdit extends JavaPlugin {
    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info("Registering Event...");
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new SignEditEventListener(), this);
        getLogger().info("Complete!");
    }
}
