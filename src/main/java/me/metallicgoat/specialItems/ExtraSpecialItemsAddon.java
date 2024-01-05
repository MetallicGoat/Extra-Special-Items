package me.metallicgoat.specialItems;

import de.marcely.bedwars.api.BedwarsAddon;

public class ExtraSpecialItemsAddon extends BedwarsAddon {

  private final ExtraSpecialItemsPlugin plugin;

  public ExtraSpecialItemsAddon(ExtraSpecialItemsPlugin plugin) {
    super(plugin);

    this.plugin = plugin;
  }

  @Override
  public String getName() {
    return "ExtraSpecialItems";
  }
}
