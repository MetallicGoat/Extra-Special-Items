package me.metallicgoat.specialItems.config;

import de.marcely.bedwars.tools.Helper;
import de.marcely.bedwars.tools.Pair;
import de.marcely.bedwars.tools.YamlConfigurationDescriptor;
import java.io.File;
import java.util.*;

import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.utils.Console;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class Config {

  public static final String ADDON_VERSION = ExtraSpecialItemsPlugin.getInstance().getDescription().getVersion();
  public static String CURRENT_CONFIG_VERSION = null;

  public static File getFile() {
    return new File(ExtraSpecialItemsPlugin.getAddon().getDataFolder(), "config.yml");
  }

  public static void load() {
    synchronized (Config.class) {
      try {
        loadUnchecked();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void loadUnchecked() throws Exception {
    final File file = getFile();

    if (!file.exists()) {
      save();
      return;
    }

    // load it
    final FileConfiguration config = new YamlConfiguration();

    try {
      config.load(file);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // SPECIAL CONFIGS
    ConfigValue.dye_tower_ukraine = config.getBoolean("Dye-Tower-Ukraine");

    // POP UP TOWER
    ConfigValue.tower_icon_name = config.getString("PopUpTower.Icon-Name");
    ConfigValue.tower_icon_material = parseItemStack(config.getString("PopUpTower.Icon-Type"), ConfigValue.tower_icon_material);
    ConfigValue.tower_block_material = parseMaterial(config.getString("PopUpTower.Block-Type"), ConfigValue.tower_block_material);
    ConfigValue.tower_block_place_interval = config.getInt("PopUpTower.Block-Place-Interval", ConfigValue.tower_block_place_interval);
    ConfigValue.tower_block_placed_per_interval = config.getInt("PopUpTower.Blocks-Placed-Per-Interval", ConfigValue.tower_block_placed_per_interval);
    ConfigValue.tower_place_place_sound = parseConfigSound(config, "PopUpTower.Sound", ConfigValue.tower_place_place_sound);

    // SILVERFISH
    ConfigValue.silverfish_icon_name = config.getString("Silverfish.Icon-Name");
    ConfigValue.silverfish_icon_material = parseItemStack(config.getString("Silverfish.Icon-Type"), ConfigValue.silverfish_icon_material);
    ConfigValue.silverfish_damage = config.getDouble("Silverfish.Damage", ConfigValue.silverfish_damage);
    ConfigValue.silverfish_life_duration = config.getInt("Silverfish.Life-Duration", ConfigValue.silverfish_life_duration);

    final List<String> silverfishNameTag = config.getStringList("Silverfish.Name-Tag");
    if (silverfishNameTag != null)
      ConfigValue.silverfish_name_tag = silverfishNameTag;

    // EGG BRIDGER
    ConfigValue.egg_bridger_icon_name = config.getString("Egg-Bridger.Icon-Name");
    ConfigValue.egg_bridger_icon_material = parseItemStack(config.getString("Egg-Bridger.Icon-Type"), ConfigValue.egg_bridger_icon_material);
    ConfigValue.egg_bridger_block_material = parseMaterial(config.getString("Egg-Bridger.Block-Type"), ConfigValue.egg_bridger_block_material);
    ConfigValue.egg_bridger_max_length = config.getInt("Egg-Bridger.Max-Length", ConfigValue.egg_bridger_max_length);
    ConfigValue.egg_bridger_max_y_variation = config.getInt("Egg-Bridger.Max-Y-Variation", ConfigValue.egg_bridger_max_y_variation);
    ConfigValue.egg_bridger_place_sound = parseConfigSound(config, "Egg-Bridger.Sound", ConfigValue.egg_bridger_place_sound);

    // ICE BRIDGER
    ConfigValue.ice_bridger_icon_name = config.getString("Ice-Bridger.Icon-Name");
    ConfigValue.ice_bridger_icon_material = parseItemStack(config.getString("Ice-Bridger.Icon-Type"), ConfigValue.ice_bridger_material);
    ConfigValue.ice_bridger_material = parseMaterial(config.getString("Ice-Bridger.Block-Type"), ConfigValue.ice_bridger_material);
    ConfigValue.ice_bridger_max_distance = config.getInt("Ice-Bridger.Max-Distance", ConfigValue.ice_bridger_max_distance);

    // SLINGSHOT
    ConfigValue.slingshot_icon_name = config.getString("Slingshot.Icon-Name");
    ConfigValue.slingshot_icon_material = parseItemStack(config.getString("Slingshot.Icon-Type"), ConfigValue.slingshot_icon_material);
    ConfigValue.slingshot_material = parseMaterial(config.getString("Slingshot.Icon-Type"), ConfigValue.slingshot_material);
    ConfigValue.slingshot_force = config.getDouble("Slingshot.Force", ConfigValue.slingshot_force);
    ConfigValue.slingshot_height_boost = config.getDouble("Slingshot.Height-Boost", ConfigValue.slingshot_height_boost);
    ConfigValue.slingshot_max_y_boost = config.getDouble("Slingshot.Max-Y-Boost", ConfigValue.slingshot_max_y_boost);
    ConfigValue.slingshot_cooldown_seconds = config.getInt("Slingshot.Cooldown-Seconds", ConfigValue.slingshot_cooldown_seconds);
    ConfigValue.slingshot_cooldown_bars = config.getInt("Slingshot.Cooldown-Bars", ConfigValue.slingshot_cooldown_bars);
    ConfigValue.slingshot_cooldown_message = config.getString("Slingshot.Cooldown-Message", ConfigValue.slingshot_cooldown_message);
    ConfigValue.slingshot_use_sound = parseConfigSound(config, "Slingshot.Use-Sound", ConfigValue.slingshot_use_sound);

    // COMMAND ITEMS
    ConfigValue.command_item_enabled = config.getBoolean("Custom-Items.Enabled");
    {
      ConfigValue.command_item_player_commands.clear();
      final ConfigurationSection playerSection = config.getConfigurationSection("Custom-Items.Player-Run");
      if (playerSection != null) {
        for (String id : playerSection.getKeys(false)) {
          final String materialName = playerSection.getString(id + ".Material");
          final String command = playerSection.getString(id + ".Command");
          ConfigValue.command_item_player_commands.put(id, new Pair<>(parseItemStack(materialName, Material.STONE), command));
        }
      }

      ConfigValue.command_item_console_commands.clear();
      final ConfigurationSection consoleSection = config.getConfigurationSection("Custom-Items.Console-Run");
      if (consoleSection != null) {
        for (String id : consoleSection.getKeys(false)) {
          final String materialName = consoleSection.getString(id + ".Material");
          final String command = consoleSection.getString(id + ".Command");
          ConfigValue.command_item_console_commands.put(id, new Pair<>(parseItemStack(materialName, Material.STONE), command));
        }
      }
    }

    // auto update file if newer version
    {
      CURRENT_CONFIG_VERSION = config.getString("file-version");

      if (CURRENT_CONFIG_VERSION == null || !CURRENT_CONFIG_VERSION.equals(ADDON_VERSION)) {
        loadOldConfigs(config);
        save();
      }
    }
  }

  private static void save() throws Exception {
    final YamlConfigurationDescriptor config = new YamlConfigurationDescriptor();

    config.addComment("Used for auto-updating the config file. Ignore it");
    config.set("file-version", ADDON_VERSION);

    config.addEmptyLine();

    config.addComment("Join our discord for support: https://discord.gg/3mJuxUW");

    config.addEmptyLine();

    config.addComment("SPECIAL ITEM ID's");
    config.addComment("PopUpTower - 'tower'");
    config.addComment("Silverfish - 'silverfish'");
    config.addComment("Egg-Bridger - 'egg-bridger'");
    config.addComment("Ice-Bridger - 'ice-bridger'");
    config.addComment("Command-Items - You Choose!");

    config.addEmptyLine();

    config.addComment("Special Configs (May be removed in the future)");
    config.set("Dye-Tower-Ukraine", ConfigValue.dye_tower_ukraine);

    config.addEmptyLine();

    config.addComment("Automatically builds you a tower");
    config.addComment("Note: Block-Place-Interval in ticks (20 ticks = 1 second)");
    config.set("PopUpTower.Icon-Name", ConfigValue.tower_icon_name);
    config.set("PopUpTower.Icon-Type", Helper.get().composeItemStack(ConfigValue.tower_icon_material));
    config.set("PopUpTower.Block-Type", ConfigValue.tower_block_material.name());
    config.set("PopUpTower.Block-Place-Interval", ConfigValue.tower_block_place_interval);
    config.set("PopUpTower.Blocks-Placed-Per-Interval", ConfigValue.tower_block_placed_per_interval);
    config.set("PopUpTower.Sound", ConfigValue.tower_place_place_sound.toString());

    config.addEmptyLine();

    config.addComment("Summon a Silverfish to help defend your base");
    config.addComment("Note: Life-Duration in ticks (20 ticks = 1 second)");
    config.addComment("Name-Tag Placeholders: {team-name} {team-color}");
    config.set("Silverfish.Icon-Name", ConfigValue.silverfish_icon_name);
    config.set("Silverfish.Icon-Type", Helper.get().composeItemStack(ConfigValue.silverfish_icon_material));
    config.set("Silverfish.Damage", ConfigValue.silverfish_damage);
    config.set("Silverfish.Life-Duration", ConfigValue.silverfish_life_duration);
    config.set("Silverfish.Name-Tag", ConfigValue.silverfish_name_tag);

    config.addEmptyLine();

    config.addComment("Construct a bridger following the path of an egg");
    config.set("Egg-Bridger.Icon-Name", ConfigValue.egg_bridger_icon_name);
    config.set("Egg-Bridger.Icon-Type", Helper.get().composeItemStack(ConfigValue.egg_bridger_icon_material));
    config.set("Egg-Bridger.Block-Type", ConfigValue.egg_bridger_block_material.name());
    config.set("Egg-Bridger.Max-Length", ConfigValue.egg_bridger_max_length);
    config.set("Egg-Bridger.Max-Y-Variation", ConfigValue.egg_bridger_max_y_variation);
    config.set("Egg-Bridger.Sound", ConfigValue.egg_bridger_place_sound.toString());

    config.addEmptyLine();

    config.addComment("Builds a flat bridge that only lasts for a few seconds");
    config.set("Ice-Bridger.Icon-Name", ConfigValue.ice_bridger_icon_name);
    config.set("Ice-Bridger.Icon-Type", ConfigValue.ice_bridger_material.name());
    config.set("Ice-Bridger.Block-Type", ConfigValue.ice_bridger_material.name());
    config.set("Ice-Bridger.Max-Distance", ConfigValue.ice_bridger_max_distance);

    config.addEmptyLine();

    config.addComment("Launch yourself with a SlingShot");
    config.set("Slingshot.Icon-Name", ConfigValue.slingshot_icon_name);
    config.set("Slingshot.Icon-Type", ConfigValue.slingshot_material.name());
    config.set("Slingshot.Force", ConfigValue.slingshot_force);
    config.set("Slingshot.Height-Boost", ConfigValue.slingshot_height_boost);
    config.set("Slingshot.Max-Y-Boost", ConfigValue.slingshot_max_y_boost);
    config.set("Slingshot.Cooldown-Seconds", ConfigValue.slingshot_cooldown_seconds);
    config.set("Slingshot.Cooldown-Bars", ConfigValue.slingshot_cooldown_bars);
    config.set("Slingshot.Cooldown-Message", ConfigValue.slingshot_cooldown_message);
    config.set("Slingshot.Use-Sound", ConfigValue.slingshot_use_sound.toString());
    config.addEmptyLine();

    config.addComment("Create as many custom Special items at you want");
    config.addComment("Specify is you want the command to be run as player, or as console");
    config.addComment("Add the item to your shop.cm2 though the use of the specialID");
    config.addComment("PlaceHolders: {player} {player-display-name} {x} {y} {z} {yaw} {pitch}");
    {
      config.set("Custom-Items.Enabled", ConfigValue.command_item_enabled);

      if (!ConfigValue.command_item_player_commands.isEmpty()) {
        for (String itemId : ConfigValue.command_item_player_commands.keySet()) {
          final Pair<ItemStack, String> pair = ConfigValue.command_item_player_commands.get(itemId);
          config.set("Custom-Items.Player-Run." + itemId + ".Material", pair.getKey() != null ? Helper.get().composeItemStack(pair.getKey()) : "STONE");
          config.set("Custom-Items.Player-Run." + itemId + ".Command", pair.getValue());
        }
      } else {
        config.set("Custom-Items.Player-Run.player-example.Material", "stone");
        config.set("Custom-Items.Player-Run.player-example.Command", "say a command that is run by a player");
      }

      if (!ConfigValue.command_item_console_commands.isEmpty()) {
        for (String itemId : ConfigValue.command_item_console_commands.keySet()) {
          final Pair<ItemStack, String> pair = ConfigValue.command_item_console_commands.get(itemId);
          config.set("Custom-Items.Console-Run." + itemId + ".Material", pair.getKey() != null ? Helper.get().composeItemStack(pair.getKey()) : "STONE");
          config.set("Custom-Items.Console-Run." + itemId + ".Command", pair.getValue());
        }
      } else {
        config.set("Custom-Items.Console-Run.console-example.Material", "stone");
        config.set("Custom-Items.Console-Run.console-example.Command", "say a command that is run by a console");
      }
    }

    config.save(getFile());
  }

  public static void loadOldConfigs(FileConfiguration config) {
    // Custom special items
    {
      if (config.isConfigurationSection("Custom-Command-Items")) {
        ConfigValue.command_item_enabled = config.getBoolean("Custom-Command-Items.Enabled", ConfigValue.command_item_enabled);

        if (ConfigValue.command_item_enabled) {
          ConfigValue.command_item_player_commands = loadLegacyCommandItems(config.getStringList("Custom-Command-Items.Player"));
          ConfigValue.command_item_console_commands = loadLegacyCommandItems(config.getStringList("Custom-Command-Items.Console"));
        }
      }
    }

    // Silverfish Name Tags
    {
      final ConfigurationSection section = config.getConfigurationSection("Silverfish.Display-Name");
      if (section != null) {
        final List<String> nameTags = new ArrayList<>();

        for (String tagVal : section.getKeys(false)) {
          final String tag = section.getString(tagVal, "");
          nameTags.add(tag.replace("{sqr}", "■"));
        }

        ConfigValue.silverfish_name_tag = nameTags;
      }
    }
  }

  private static ItemStack parseItemStack(String string, Material def) {
    return parseItemStack(string, new ItemStack(def));
  }

  private static ItemStack parseItemStack(String string, ItemStack def) {
    final ItemStack stack = Helper.get().parseItemStack(string);
    return stack != null ? stack : new ItemStack(def);
  }

  private static Material parseMaterial(String string, Material def) {
    final Material mat = Helper.get().getMaterialByName(string);
    return mat != null ? mat : def;
  }

  private static Sound parseConfigSound(FileConfiguration config, String configPath, Sound def) {
    final String configSound = config.getString(configPath);

    if (configSound != null) {
      final Sound sound = Helper.get().getSoundByName(configSound);

      if (sound != null)
        return sound;
      else
        Console.printConfigWarning(configPath, "Cannot parse sound " + configSound);
    }

    return def;
  }

  @Deprecated
  private static HashMap<String, Pair<ItemStack, String>> loadLegacyCommandItems(List<String> keys) {
    final HashMap<String, Pair<ItemStack, String>> idCommand = new HashMap<>();

    if (keys.isEmpty())
      return idCommand;

    for (String string : keys) {
      if (string.contains(":")) {
        final String[] idCommandString = string.split(":");

        // remove the command '/' if there is one
        final String command = idCommandString[2].startsWith("/") ? idCommandString[2].substring(1) : idCommandString[2];
        final ItemStack itemStack = parseItemStack(idCommandString[1], Material.STONE);

        idCommand.put(idCommandString[0], new Pair<>(itemStack, command));
      }
    }
    return idCommand;
  }
}
