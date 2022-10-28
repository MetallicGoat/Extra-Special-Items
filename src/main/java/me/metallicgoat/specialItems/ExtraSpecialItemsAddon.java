package me.metallicgoat.specialItems;

import de.marcely.bedwars.api.BedwarsAddon;
import me.metallicgoat.specialItems.items.eggbridge.PreventHatching;
import me.metallicgoat.specialItems.items.silverfish.SilverfishThrow;
import org.bukkit.plugin.PluginManager;

public class ExtraSpecialItemsAddon extends BedwarsAddon {

    private final ExtraSpecialItemsPlugin plugin;

    public ExtraSpecialItemsAddon(ExtraSpecialItemsPlugin plugin) {
        super(plugin);

        this.plugin = plugin;
    }

    public void registerEvents() {
        final PluginManager manager = plugin.getServer().getPluginManager();

        manager.registerEvents(new PreventHatching(), plugin);
        manager.registerEvents(new SilverfishThrow(), plugin);
    }

    @Override
    public String getName(){
        return "ExtraSpecialItems";
    }
}
