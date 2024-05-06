package me.nik.hackprotectorx;

import me.nik.hackprotectorx.listeners.CheatListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class HackProtectorX extends JavaPlugin {

    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(new CheatListener(), this);
    }

    @Override
    public void onDisable() {

        HandlerList.unregisterAll(this);
    }
}