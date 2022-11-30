package me.metallicgoat.specialItems;

import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.tools.Helper;
import me.metallicgoat.specialItems.config.Config;
import me.metallicgoat.specialItems.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExtraSpecialItemsPlugin extends JavaPlugin {

    public static final int MIN_MBEDWARS_API_VER = 15;
    public static final String MIN_MBEDWARS_VER_NAME = "5.1";

    private static ExtraSpecialItemsAddon addon;
    private static ExtraSpecialItemsPlugin instance;

    public void onEnable() {
        instance = this;

        if(!checkMBedwars()) return;
        if(!registerAddon()) return;

        new Metrics(this, 14359);

        addon.registerEvents();
        Config.load();

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

    public static ExtraSpecialItemsPlugin getInstance() {
        return instance;
    }

    public static ExtraSpecialItemsAddon getAddon() {
        return addon;
    }

    private boolean checkMBedwars(){
        try{
            final Class<?> apiClass = Class.forName("de.marcely.bedwars.api.BedwarsAPI");
            final int apiVersion = (int) apiClass.getMethod("getAPIVersion").invoke(null);

            if(apiVersion < MIN_MBEDWARS_API_VER)
                throw new IllegalStateException();

        } catch(Exception e) {
            getLogger().warning("Sorry, your installed version of MBedwars is not supported. Please install at least v" + MIN_MBEDWARS_VER_NAME);
            Bukkit.getPluginManager().disablePlugin(this);

            return false;
        }

        return true;
    }

    private boolean registerAddon(){
        addon = new ExtraSpecialItemsAddon(this);

        if(!addon.register()){
            getLogger().warning("It seems like this addon has already been loaded. Please delete duplicates and try again.");
            Bukkit.getPluginManager().disablePlugin(this);

            return false;
        }

        return true;
    }

    public boolean copyResource(String internalPath, File out) throws IOException {
        if(!out.exists() || out.length() == 0){
            try(InputStream is = getResource(internalPath)){
                if(is == null){
                    getLogger().warning("Your plugin seems to be broken (Failed to find internal file " + internalPath + ")");
                    return false;
                }

                out.createNewFile();

                try(FileOutputStream os = new FileOutputStream(out)){
                    Helper.get().copy(is, os);
                }

                return true;
            }
        }

        return false;
    }

    private void log(String ...args) {
        for(String s : args)
            getLogger().info(s);
    }
}
