package me.metallicgoat.specialItems;

import de.marcely.bedwars.api.BedwarsAPI;
import me.metallicgoat.specialItems.config.Config;
import me.metallicgoat.specialItems.customitems.CustomSpecialItem;
import me.metallicgoat.specialItems.customitems.handlers.TowerHandler;
import me.metallicgoat.specialItems.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class ExtraSpecialItemsPlugin extends JavaPlugin {

  public static final int MIN_MBEDWARS_API_VER = 23;
  public static final String MIN_MBEDWARS_VER_NAME = "5.2.7";

  private static ExtraSpecialItemsAddon addon;
  private static ExtraSpecialItemsPlugin instance;

  public static ExtraSpecialItemsPlugin getInstance() {
    return instance;
  }

  public static ExtraSpecialItemsAddon getAddon() {
    return addon;
  }

  public void onEnable() {
    instance = this;

    if (!checkMBedwars())
      return;
    if (!registerAddon())
      return;

    new Metrics(this, 14359);

    Config.load();

    final PluginDescriptionFile pdf = this.getDescription();

    log(
        "------------------------------",
        pdf.getName() + " For MBedwars",
        "By: " + pdf.getAuthors(),
        "Version: " + pdf.getVersion(),
        "------------------------------"
    );

    BedwarsAPI.onReady(() -> {
      CustomSpecialItem.registerAll();
      TowerHandler.init();
    });
  }

  private boolean checkMBedwars() {
    try {
      final Class<?> apiClass = Class.forName("de.marcely.bedwars.api.BedwarsAPI");
      final int apiVersion = (int) apiClass.getMethod("getAPIVersion").invoke(null);

      if (apiVersion < MIN_MBEDWARS_API_VER)
        throw new IllegalStateException();

    } catch (Exception e) {
      getLogger().warning("Sorry, your installed version of MBedwars is not supported. Please install at least v" + MIN_MBEDWARS_VER_NAME);
      Bukkit.getPluginManager().disablePlugin(this);

      return false;
    }

    return true;
  }

  private boolean registerAddon() {
    addon = new ExtraSpecialItemsAddon(this);

    if (!addon.register()) {
      getLogger().warning("It seems like this addon has already been loaded. Please delete duplicates and try again.");
      Bukkit.getPluginManager().disablePlugin(this);

      return false;
    }

    return true;
  }

  private void log(String... args) {
    for (String s : args)
      getLogger().info(s);
  }
}
