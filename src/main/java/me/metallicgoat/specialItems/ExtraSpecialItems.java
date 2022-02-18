package me.metallicgoat.specialItems;

import de.marcely.bedwars.api.BedwarsAPI;
import me.metallicgoat.specialItems.config.Config;
import me.metallicgoat.specialItems.items.CustomSpecialItem;
import me.metallicgoat.specialItems.items.eggbridge.PreventHatching;
import me.metallicgoat.specialItems.items.silverfish.SilverfishThrow;
import org.bstats.bukkit.Metrics;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ExtraSpecialItems extends JavaPlugin {

    private static ExtraSpecialItems instance;
    private final Server server = getServer();

    public void onEnable() {

        final int pluginId = 14359;
        final Metrics metrics = new Metrics(this, pluginId);

        instance = this;
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

    private void log(String ...args) {
        for(String s : args)
            getLogger().info(s);
    }
}
