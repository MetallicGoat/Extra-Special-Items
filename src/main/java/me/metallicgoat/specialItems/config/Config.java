package me.metallicgoat.specialItems.config;

import de.marcely.bedwars.tools.Helper;
import me.metallicgoat.specialItems.utils.Console;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import me.metallicgoat.specialItems.config.updater.ConfigUpdater;
import me.metallicgoat.specialItems.utils.XSound;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Config {

    public static void save(){
        final ExtraSpecialItems plugin = ExtraSpecialItems.getInstance();
        plugin.saveDefaultConfig();

        final File configFile = new File(plugin.getDataFolder(), "config.yml");

        try {
            ConfigUpdater.update(plugin, "config.yml", configFile, Collections.singletonList("Silverfish.Display-Name"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        plugin.reloadConfig();
        load();
    }

    public static void load(){

        final ExtraSpecialItems plugin = ExtraSpecialItems.getInstance();
        final FileConfiguration mainConfig = plugin.getConfig();

        // POP UP TOWER
        final String config_tower_block_material = mainConfig.getString("Ice-Bridger.Block-Type");
        if(config_tower_block_material != null){
            final Material material = Helper.get().getMaterialByName(config_tower_block_material.toUpperCase());

            if(material != null)
                ConfigValue.tower_block_material = material;
            else
                Console.printConfigWarning("PopUpTower.Block-Type", "Cannot parse material " + config_tower_block_material);
        }

        ConfigValue.tower_block_place_interval = mainConfig.getInt("PopUpTower.Block-Place-Interval", ConfigValue.tower_block_place_interval);
        ConfigValue.tower_block_placed_per_interval = mainConfig.getInt("PopUpTower.Blocks-Placed-Per-Interval", ConfigValue.tower_block_placed_per_interval);
        final String config_tower_place_sound = mainConfig.getString("PopUpTower.Sound");

        if(config_tower_place_sound != null){
            Optional<XSound> soundType = XSound.matchXSound(config_tower_place_sound.toUpperCase());

            if(soundType.isPresent())
                ConfigValue.tower_place_place_sound = soundType.get().parseSound();
            else
                Console.printConfigWarning("PopUpTower.Sound", "Cannot parse sound " + config_tower_place_sound);
        }

        // SILVERFISH
        ConfigValue.silverfish_life_duration = mainConfig.getInt("Silverfish.Life-Duration", ConfigValue.silverfish_life_duration);
        ConfigValue.silverfish_life_display_name = mainConfig.getConfigurationSection("Silverfish.Display-Name");

        // EGG BRIDGER
        final String config_egg_bridger_material = mainConfig.getString("Ice-Bridger.Block-Type");
        if(config_egg_bridger_material != null){
            final Material material = Helper.get().getMaterialByName(config_egg_bridger_material.toUpperCase());

            if(material != null)
                ConfigValue.egg_bridger_block_material = material;
            else
                Console.printConfigWarning("Egg-Bridger.Block-Type", "Cannot parse material " + config_egg_bridger_material);
        }

        ConfigValue.egg_bridger_max_length = mainConfig.getInt("Egg-Bridger.Max-Length", ConfigValue.egg_bridger_max_length);
        ConfigValue.egg_bridger_max_y_variation = mainConfig.getInt("Egg-Bridger.Max-Y-Variation", ConfigValue.egg_bridger_max_y_variation);
        final String config_egg_bridger_place_sound = mainConfig.getString("Egg-Bridger.Sound");

        if(config_egg_bridger_place_sound != null){
            Optional<XSound> soundType = XSound.matchXSound(config_egg_bridger_place_sound.toUpperCase());

            if(soundType.isPresent())
                ConfigValue.egg_bridger_place_sound = soundType.get().parseSound();
            else
                Console.printConfigWarning("Egg-Bridger.Sound", "Cannot parse sound " + config_egg_bridger_place_sound);
        }

        // ICE BRIDGER
        final String config_ice_bridger_material = mainConfig.getString("Ice-Bridger.Block-Type");
        if(config_ice_bridger_material != null){
            final Material material = Helper.get().getMaterialByName(config_ice_bridger_material.toUpperCase());

            if(material != null)
                ConfigValue.ice_bridger_material = material;
            else
                Console.printConfigWarning("Ice-Bridger.Block-Type", "Cannot parse material " + config_ice_bridger_material);
        }

        ConfigValue.ice_bridger_max_distance = mainConfig.getInt("Ice-Bridger.Max-Distance", ConfigValue.ice_bridger_max_distance);

        // CUSTOM ITEMS
        ConfigValue.command_item_enabled = mainConfig.getBoolean("Custom-Command-Items.Enabled", ConfigValue.command_item_enabled);

        final List<String> config_command_items_player_commands = mainConfig.getStringList("Custom-Command-Items.Player");
        if(!config_command_items_player_commands.isEmpty()){
            ConfigValue.command_item_player_commands = formatIdCommands(config_command_items_player_commands);
        }

        final List<String> config_command_items_console_commands = mainConfig.getStringList("Custom-Command-Items.Console");
        if(!config_command_items_console_commands.isEmpty()){
            ConfigValue.command_item_console_commands = formatIdCommands(config_command_items_console_commands);
        }
    }

    private static HashMap<String, String> formatIdCommands(List<String> keys){
        final HashMap<String, String> idCommand = new HashMap<>();

        for(String string:keys){
            if(string.contains(":")){
                final String[] idCommandString = string.split(":");

                // remove the command '/' if there is one
                idCommand.put(idCommandString[0], idCommandString[1].startsWith("/") ? idCommandString[1].substring(1) : idCommandString[1]);
            }
        }
        return idCommand;
    }
}
