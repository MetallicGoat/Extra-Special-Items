package me.metallicgoat.specialItems.config;

import me.metallicgoat.specialItems.Console;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import me.metallicgoat.specialItems.config.updater.ConfigUpdater;
import me.metallicgoat.specialItems.utils.XSound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class Config {

    public static void save(){
        final ExtraSpecialItems plugin = ExtraSpecialItems.getInstance();
        plugin.saveDefaultConfig();

        final File configFile = new File(plugin.getDataFolder(), "config.yml");

        try {
            ConfigUpdater.update(plugin, "config.yml", configFile, Collections.emptyList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        plugin.reloadConfig();
        load();
    }

    public static void load(){

        final ExtraSpecialItems plugin = ExtraSpecialItems.getInstance();
        final FileConfiguration mainConfig = plugin.getConfig();

        final Integer config_tower_block_place_interval = mainConfig.getInt("PopUpTower.Block-Place-Interval");
        final Integer config_tower_block_placed_per_interval = mainConfig.getInt("PopUpTower.Blocks-Placed-Per-Interval");
        final String config_tower_place_sound = mainConfig.getString("PopUpTower.Sound");

        final int config_silverfish_life_duration = mainConfig.getInt("Silverfish.Life-Duration");
        final ConfigurationSection config_silverfish_life_display_name = mainConfig.getConfigurationSection("Silverfish.Display-Name");

        final int config_egg_bridger_max_length = mainConfig.getInt("Egg-Bridger.Max-Length");
        final int config_egg_bridger_max_y_variation = mainConfig.getInt("Egg-Bridger.Max-Y-Variation");
        final String config_egg_bridger_place_sound = mainConfig.getString("Egg-Bridger.Sound");

        final String config_ice_bridger_material = mainConfig.getString("Ice-Bridger.Block-Type");
        final int config_ice_bridger_max_distance = mainConfig.getInt("Ice-Bridger.Max-Distance");

        if(config_tower_block_place_interval != null){

        }




        if(config_egg_bridger_place_sound != null){
            Optional<XSound> soundType = XSound.matchXSound(config_egg_bridger_place_sound);

            if(soundType.isPresent())
                ConfigValue.egg_bridger_place_sound = soundType.get().parseSound();
            else
                Console.printConfigWarning("Egg-Bridger.Sound", "Cannot parse sound " + config_egg_bridger_place_sound);
        }else{
            Console.printConfigWarning("Egg-Bridger.Sound", "Cannot read config");
        }

    }

}
