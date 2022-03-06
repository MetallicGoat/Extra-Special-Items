package me.metallicgoat.specialItems.config;

import de.marcely.bedwars.tools.Helper;
import de.marcely.bedwars.tools.Pair;
import me.metallicgoat.specialItems.utils.Console;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import me.metallicgoat.specialItems.config.updater.ConfigUpdater;
import me.metallicgoat.specialItems.utils.XSound;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Config {

    public static File getFile(){
        return new File(ExtraSpecialItems.getAddon().getDataFolder(), "config.yml");
    }

    public static void save(){
        ExtraSpecialItems.getAddon().getDataFolder().mkdirs();

        synchronized(Config.class){
            try{
                saveUnchecked();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void saveUnchecked() throws IOException {
        final ExtraSpecialItems plugin = ExtraSpecialItems.getInstance();

        final File file = getFile();

        if(!file.exists())
            plugin.copyResource("config.yml", file);

        try {
            ConfigUpdater.update(plugin, "config.yml", file, Collections.singletonList("Silverfish.Display-Name"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        load();
    }

    public static FileConfiguration getConfig(){
        final FileConfiguration config = new YamlConfiguration();

        try{
            config.load(getFile());
        }catch(Exception e){
            e.printStackTrace();
        }

        return config;
    }

    public static void load(){

        final FileConfiguration mainConfig = getConfig();

        // SPECIAL
        ConfigValue.dye_tower_ukraine = mainConfig.getBoolean("Dye-Tower-Ukraine");

        // POP UP TOWER
        ConfigValue.tower_icon_name = mainConfig.getString("PopUpTower.Icon-Name");
        ConfigValue.tower_icon_material = parseConfigMaterial(mainConfig, "PopUpTower.Icon-Type", ConfigValue.tower_icon_material);
        ConfigValue.tower_block_material = parseConfigMaterial(mainConfig, "PopUpTower.Block-Type", ConfigValue.tower_block_material);
        ConfigValue.tower_block_place_interval = mainConfig.getInt("PopUpTower.Block-Place-Interval", ConfigValue.tower_block_place_interval);
        ConfigValue.tower_block_placed_per_interval = mainConfig.getInt("PopUpTower.Blocks-Placed-Per-Interval", ConfigValue.tower_block_placed_per_interval);
        ConfigValue.tower_place_place_sound = parseConfigSound(mainConfig, "PopUpTower.Sound", ConfigValue.tower_place_place_sound);

        // SILVERFISH
        ConfigValue.silverfish_icon_name = mainConfig.getString("Silverfish.Icon-Name");
        ConfigValue.silverfish_icon_material = parseConfigMaterial(mainConfig, "Silverfish.Icon-Type", ConfigValue.silverfish_icon_material);
        ConfigValue.silverfish_life_duration = mainConfig.getInt("Silverfish.Life-Duration", ConfigValue.silverfish_life_duration);
        ConfigValue.silverfish_life_display_name = mainConfig.getConfigurationSection("Silverfish.Display-Name");

        // EGG BRIDGER
        ConfigValue.egg_bridger_icon_name = mainConfig.getString("Egg-Bridger.Icon-Name");
        ConfigValue.egg_bridger_icon_material = parseConfigMaterial(mainConfig, "Egg-Bridger.Icon-Type", ConfigValue.egg_bridger_icon_material);
        ConfigValue.egg_bridger_block_material = parseConfigMaterial(mainConfig, "Egg-Bridger.Block-Type", ConfigValue.egg_bridger_block_material);
        ConfigValue.egg_bridger_max_length = mainConfig.getInt("Egg-Bridger.Max-Length", ConfigValue.egg_bridger_max_length);
        ConfigValue.egg_bridger_max_y_variation = mainConfig.getInt("Egg-Bridger.Max-Y-Variation", ConfigValue.egg_bridger_max_y_variation);
        ConfigValue.egg_bridger_place_sound = parseConfigSound(mainConfig, "Egg-Bridger.Sound", ConfigValue.egg_bridger_place_sound);

        // ICE BRIDGER
        ConfigValue.ice_bridger_icon_name = mainConfig.getString("Ice-Bridger.Icon-Name");
        ConfigValue.ice_bridger_icon_material = parseConfigMaterial(mainConfig, "Ice-Bridger.Icon-Type", ConfigValue.ice_bridger_material);
        ConfigValue.ice_bridger_material = parseConfigMaterial(mainConfig, "Ice-Bridger.Block-Type", ConfigValue.ice_bridger_material);
        ConfigValue.ice_bridger_max_distance = mainConfig.getInt("Ice-Bridger.Max-Distance", ConfigValue.ice_bridger_max_distance);

        // CUSTOM ITEMS
        ConfigValue.command_item_enabled = mainConfig.getBoolean("Custom-Command-Items.Enabled", ConfigValue.command_item_enabled);
        ConfigValue.command_item_player_commands = formatIdCommands(mainConfig.getStringList("Custom-Command-Items.Player"));
        ConfigValue.command_item_console_commands = formatIdCommands(mainConfig.getStringList("Custom-Command-Items.Console"));
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
            Optional<XSound> soundType = XSound.matchXSound(configSound.toUpperCase());

            if(soundType.isPresent())
                return soundType.get().parseSound();
            else
                Console.printConfigWarning(configPath, "Cannot parse sound " + configSound);
        }

        return def;
    }

    private static HashMap<String, Pair<Material, String>> formatIdCommands(List<String> keys){
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
