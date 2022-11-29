package me.metallicgoat.specialItems.config;

import de.marcely.bedwars.tools.Helper;
import de.marcely.bedwars.tools.Pair;
import de.marcely.bedwars.tools.YamlConfigurationDescriptor;
import me.metallicgoat.specialItems.utils.Console;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Config {

    public static final String ADDON_VERSION = ExtraSpecialItemsPlugin.getInstance().getDescription().getVersion();
    public static String CURRENT_CONFIG_VERSION = null;

    public static File getFile(){
        return new File(ExtraSpecialItemsPlugin.getAddon().getDataFolder(), "config.yml");
    }

    public static void load(){
        synchronized(Config.class){
            try{
                loadUnchecked();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void loadUnchecked() throws Exception {
        final File file = getFile();

        if(!file.exists()){
            save();
            return;
        }

        // load it
        final FileConfiguration config = new YamlConfiguration();

        try{
            config.load(file);
        }catch(Exception e){
            e.printStackTrace();
        }

        // SPECIAL
        ConfigValue.dye_tower_ukraine = config.getBoolean("Dye-Tower-Ukraine");

        // POP UP TOWER
        ConfigValue.tower_icon_name = config.getString("PopUpTower.Icon-Name");
        ConfigValue.tower_icon_material = parseConfigMaterial(config, "PopUpTower.Icon-Type", ConfigValue.tower_icon_material);
        ConfigValue.tower_block_material = parseConfigMaterial(config, "PopUpTower.Block-Type", ConfigValue.tower_block_material);
        ConfigValue.tower_block_place_interval = config.getInt("PopUpTower.Block-Place-Interval", ConfigValue.tower_block_place_interval);
        ConfigValue.tower_block_placed_per_interval = config.getInt("PopUpTower.Blocks-Placed-Per-Interval", ConfigValue.tower_block_placed_per_interval);
        ConfigValue.tower_place_place_sound = parseConfigSound(config, "PopUpTower.Sound", ConfigValue.tower_place_place_sound);

        // SILVERFISH
        ConfigValue.silverfish_icon_name = config.getString("Silverfish.Icon-Name");
        ConfigValue.silverfish_icon_material = parseConfigMaterial(config, "Silverfish.Icon-Type", ConfigValue.silverfish_icon_material);
        ConfigValue.silverfish_life_duration = config.getInt("Silverfish.Life-Duration", ConfigValue.silverfish_life_duration);

        final List<String> silverfishNameTag = config.getStringList("Silverfish.Name-Tag");
        if(silverfishNameTag != null)
            ConfigValue.silverfish_name_tag = silverfishNameTag;

        // EGG BRIDGER
        ConfigValue.egg_bridger_icon_name = config.getString("Egg-Bridger.Icon-Name");
        ConfigValue.egg_bridger_icon_material = parseConfigMaterial(config, "Egg-Bridger.Icon-Type", ConfigValue.egg_bridger_icon_material);
        ConfigValue.egg_bridger_block_material = parseConfigMaterial(config, "Egg-Bridger.Block-Type", ConfigValue.egg_bridger_block_material);
        ConfigValue.egg_bridger_max_length = config.getInt("Egg-Bridger.Max-Length", ConfigValue.egg_bridger_max_length);
        ConfigValue.egg_bridger_max_y_variation = config.getInt("Egg-Bridger.Max-Y-Variation", ConfigValue.egg_bridger_max_y_variation);
        ConfigValue.egg_bridger_place_sound = parseConfigSound(config, "Egg-Bridger.Sound", ConfigValue.egg_bridger_place_sound);

        // ICE BRIDGER
        ConfigValue.ice_bridger_icon_name = config.getString("Ice-Bridger.Icon-Name");
        ConfigValue.ice_bridger_icon_material = parseConfigMaterial(config, "Ice-Bridger.Icon-Type", ConfigValue.ice_bridger_material);
        ConfigValue.ice_bridger_material = parseConfigMaterial(config, "Ice-Bridger.Block-Type", ConfigValue.ice_bridger_material);
        ConfigValue.ice_bridger_max_distance = config.getInt("Ice-Bridger.Max-Distance", ConfigValue.ice_bridger_max_distance);

        // TODO CUSTOM ITEMS ITEM PARSING
        ConfigValue.command_item_enabled = config.getBoolean("Command-Items.Enabled");
        {
            ConfigValue.command_item_player_commands.clear();

            for (String id : config.getConfigurationSection("Command-Items.Player-Run").getKeys(false)) {
                final String materialName = config.getString("Command-Items.Player-Run." + ".Material");
                final String command = config.getString("Command-Items.Player-Run." + ".Command");
                ConfigValue.command_item_player_commands.put(id, new Pair<>(Helper.get().getMaterialByName(materialName), command));
            }

            ConfigValue.command_item_player_commands.clear();

            for (String id : config.getConfigurationSection("Command-Items.Console-Run").getKeys(false)) {
                final String materialName = config.getString("Command-Items.Console-Run." + ".Material");
                final String command = config.getString("Command-Items.Console-Run." + ".Command");
                ConfigValue.command_item_console_commands.put(id, new Pair<>(Helper.get().getMaterialByName(materialName), command));
            }
        }


        // auto update file if newer version
        {
            CURRENT_CONFIG_VERSION = config.getString("file-version");

            if(CURRENT_CONFIG_VERSION == null || !CURRENT_CONFIG_VERSION.equals(ADDON_VERSION)) {
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

        config.set("PopUpTower.Icon-Name", ConfigValue.tower_icon_name);
        config.set("PopUpTower.Icon-Type", ConfigValue.tower_icon_material.name());
        config.set("PopUpTower.Block-Type", ConfigValue.tower_block_material.name());
        config.set("PopUpTower.Block-Place-Interval", ConfigValue.tower_block_place_interval);
        config.set("PopUpTower.Blocks-Placed-Per-Interval", ConfigValue.tower_block_placed_per_interval);
        config.set("PopUpTower.Sound", ConfigValue.tower_place_place_sound.toString());

        config.addEmptyLine();

        config.set("Silverfish.Icon-Name", ConfigValue.silverfish_icon_name);
        config.set("Silverfish.Icon-Type", ConfigValue.silverfish_icon_material);
        config.set("Silverfish.Life-Duration", ConfigValue.silverfish_life_duration);
        config.set("Silverfish.Name-Tag", ConfigValue.silverfish_name_tag);

        config.addEmptyLine();

        config.set("Egg-Bridger.Icon-Name", ConfigValue.egg_bridger_icon_name);
        config.set("Egg-Bridger.Icon-Type", ConfigValue.egg_bridger_icon_material.name());
        config.set("Egg-Bridger.Block-Type", ConfigValue.egg_bridger_block_material.name());
        config.set("Egg-Bridger.Max-Length", ConfigValue.egg_bridger_max_length);
        config.set("Egg-Bridger.Max-Y-Variation", ConfigValue.egg_bridger_max_y_variation);
        config.set("Egg-Bridger.Sound", ConfigValue.egg_bridger_place_sound.toString());

        config.addEmptyLine();

        config.set("Ice-Bridger.Icon-Name", ConfigValue.ice_bridger_icon_name);
        config.set("Ice-Bridger.Icon-Type", ConfigValue.ice_bridger_material);
        config.set("Ice-Bridger.Block-Type", ConfigValue.ice_bridger_material);
        config.set("Ice-Bridger.Max-Distance",ConfigValue.ice_bridger_max_distance);

        config.set("Custom-Items.Enabled", ConfigValue.command_item_enabled);

        for(String itemId : ConfigValue.command_item_player_commands.keySet()){
            final Pair<Material, String> pair = ConfigValue.command_item_player_commands.get(itemId);
            config.set("Custom-Items.Player-Run." + itemId + ".Material", pair.getKey() != null ? pair.getKey().name() : Material.STONE);
            config.set("Custom-Items.Player-Run." + itemId + ".Command", pair.getValue());
        }

        for(String itemId : ConfigValue.command_item_console_commands.keySet()){
            final Pair<Material, String> pair = ConfigValue.command_item_console_commands.get(itemId);
            config.set("Custom-Items.Console-Run." + itemId + ".Material", pair.getKey() != null ? pair.getKey().name() : Material.STONE);
            config.set("Custom-Items.Console-Run." + itemId + ".Command", pair.getValue());
        }

        config.save(getFile());
    }

    public static void loadOldConfigs(FileConfiguration config){
        // Custom special items
        {
            if (config.isConfigurationSection("Custom-Command-Items")) {
                ConfigValue.command_item_enabled = config.getBoolean("Custom-Command-Items.Enabled", ConfigValue.command_item_enabled);
                ConfigValue.command_item_player_commands = loadLegacyCommandItems(config.getStringList("Custom-Command-Items.Player"));
                ConfigValue.command_item_console_commands = loadLegacyCommandItems(config.getStringList("Custom-Command-Items.Console"));
            }
        }

        // Silverfish Name Tags
        {
            final ConfigurationSection section = config.getConfigurationSection("Silverfish.Display-Name");
            if (section != null)
                ConfigValue.silverfish_name_tag = new ArrayList<>(section.getKeys(false));
        }
    }

    private static Material parseConfigMaterial(FileConfiguration config, String configPath, Material def){
        final String configMaterial = config.getString(configPath);

        if(configMaterial != null){
            final Material material = Helper.get().getMaterialByName(configMaterial.toUpperCase());

            if(material != null)
                return material;
            else
                Console.printConfigWarning(configPath, "Cannot parse material " + configMaterial);
        }

        return def;
    }

    private static Sound parseConfigSound(FileConfiguration config, String configPath, Sound def){
        final String configSound = config.getString(configPath);

        if(configSound != null){
            final Sound sound = Helper.get().getSoundByName(configSound);

            if(sound != null)
                return sound;
            else
                Console.printConfigWarning(configPath, "Cannot parse sound " + configSound);
        }

        return def;
    }

    @Deprecated
    private static HashMap<String, Pair<Material, String>> loadLegacyCommandItems(List<String> keys){
        final HashMap<String, Pair<Material, String>> idCommand = new HashMap<>();

        if(keys.isEmpty())
            return idCommand;

        for(String string:keys){
            if(string.contains(":")){
                final String[] idCommandString = string.split(":");

                // remove the command '/' if there is one
                final Material material = Helper.get().getMaterialByName(idCommandString[1]);
                if(material == null)
                    Helper.get().getMaterialByName("STONE");

                Pair<Material, String> materialCommand = new Pair<>(material, idCommandString[2].startsWith("/") ? idCommandString[2].substring(1) : idCommandString[2]);

                idCommand.put(idCommandString[0], materialCommand);
            }
        }
        return idCommand;
    }
}
