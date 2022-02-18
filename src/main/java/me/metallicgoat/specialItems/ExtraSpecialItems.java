package me.metallicgoat.specialItems;

import de.marcely.bedwars.api.BedwarsAPI;
import me.metallicgoat.specialItems.config.Config;
import me.metallicgoat.specialItems.items.CustomSpecialItem;
import me.metallicgoat.specialItems.items.eggbridge.PreventHatching;
import me.metallicgoat.specialItems.items.silverfish.SilverfishThrow;
import me.metallicgoat.specialItems.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ExtraSpecialItems extends JavaPlugin {

    public static final int MIN_MBEDWARS_API_VER = 8;
    public static final String MIN_MBEDWARS_VER_NAME = "5.0.7";

    private ExtraSpecialItemsAddon addon;
    private static ExtraSpecialItems instance;
    private final Server server = getServer();

    public void onEnable() {
        instance = this;

        if(!checkMBedwars()) return;
        if(!registerAddon()) return;

        final int pluginId = 14359;
        final Metrics metrics = new Metrics(this, pluginId);

        registerEvents();
        Config.save();

        final PluginDescriptionFile pdf = this.getDescription();

        log(
                "------------------------------",
                pdf.getName() + " For MBedwars",
                "By: " + pdf.getAuthors(),
                "Version: " + pdf.getVersion(),
                "------------------------------"
        );

        BedwarsAPI.onReady(CustomSpecialItem::registerAll);
    }

    public static ExtraSpecialItems getInstance() {
        return instance;
    }

    private void registerEvents() {
        final PluginManager manager = this.server.getPluginManager();
        manager.registerEvents(new PreventHatching(), this);
        manager.registerEvents(new SilverfishThrow(), this);
    }

    private boolean checkMBedwars(){
        try{
            final Class<?> apiClass = Class.forName("de.marcely.bedwars.api.BedwarsAPI");
            final int apiVersion = (int) apiClass.getMethod("getAPIVersion").invoke(null);

            if(apiVersion < MIN_MBEDWARS_API_VER)
                throw new IllegalStateException();
        }catch(Exception e){
            getLogger().warning("Sorry, your installed version of MBedwars is not supported. Please install at least v" + MIN_MBEDWARS_VER_NAME);
            Bukkit.getPluginManager().disablePlugin(this);

            return false;
        }

        return true;
    }

    private boolean registerAddon(){
        this.addon = new ExtraSpecialItemsAddon(this);

        if(!this.addon.register()){
            getLogger().warning("It seems like this addon has already been loaded. Please delete duplicates and try again.");
            Bukkit.getPluginManager().disablePlugin(this);

            return false;
        }

        return true;
    }

    private void log(String ...args) {
        for(String s : args)
            getLogger().info(s);
    }
}
