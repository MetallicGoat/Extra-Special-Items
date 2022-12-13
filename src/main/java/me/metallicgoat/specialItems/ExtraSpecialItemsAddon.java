package me.metallicgoat.specialItems;

import de.marcely.bedwars.api.BedwarsAddon;
import me.metallicgoat.specialItems.customitems.Events;

public class ExtraSpecialItemsAddon extends BedwarsAddon {

    private final ExtraSpecialItemsPlugin plugin;

    public ExtraSpecialItemsAddon(ExtraSpecialItemsPlugin plugin) {
        super(plugin);

        this.plugin = plugin;
    }

    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new Events(), plugin);
    }

    @Override
    public String getName(){
        return "ExtraSpecialItems";
    }
}
