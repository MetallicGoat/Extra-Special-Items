package me.metallicgoat.specialItems;

import de.marcely.bedwars.api.BedwarsAPI;
import me.metallicgoat.specialItems.config.ConfigUpdater;
import me.metallicgoat.specialItems.items.eggbridge.PreventHatching;
import me.metallicgoat.specialItems.items.eggbridge.RegisterEggBridger;
import me.metallicgoat.specialItems.items.icebridge.RegisterIceBridger;
import me.metallicgoat.specialItems.items.popuptower.RegisterTower;
import me.metallicgoat.specialItems.items.silverfish.RegisterSilverfish;
import me.metallicgoat.specialItems.items.silverfish.SilverfishThrow;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class ExtraSpecialItems extends JavaPlugin {

    private static ExtraSpecialItems instance;
    private final Server server = getServer();

    public void onEnable() {

        //int pluginId = 11772;
        //Metrics metrics = new Metrics(this, pluginId);

        registerEvents();
        loadConfig();
        instance = this;
        //configManager = new ConfigManager();

        PluginDescriptionFile pdf = this.getDescription();

        log(
                "------------------------------",
                pdf.getName() + " For MBedwars",
                "By: " + pdf.getAuthors(),
                "Version: " + pdf.getVersion(),
                "------------------------------"
        );

        BedwarsAPI.onReady(() -> {
            RegisterTower registerTower = new RegisterTower();
            registerTower.registerItem();
            RegisterEggBridger registerEggBridger = new RegisterEggBridger();
            registerEggBridger.registerItem();
            RegisterSilverfish registerSilverfish = new RegisterSilverfish();
            registerSilverfish.registerItem();
            RegisterIceBridger registerIceBridger = new RegisterIceBridger();
            registerIceBridger.registerItem();
        });
    }

    private void registerEvents() {
        PluginManager manager = this.server.getPluginManager();
        manager.registerEvents(new PreventHatching(), this);
        manager.registerEvents(new SilverfishThrow(), this);
    }


    public static ExtraSpecialItems getInstance() {
        return instance;
    }

    private void log(String ...args) {
        for(String s : args)
            getLogger().info(s);
    }

    private void loadConfig(){
        saveDefaultConfig();
        File configFile = new File(getDataFolder(), "config.yml");

        try {
            ConfigUpdater.update(this, "config.yml", configFile, Collections.singletonList("Nothing"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        reloadConfig();
    }
}
