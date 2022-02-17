package me.metallicgoat.specialItems.config;

import de.marcely.bedwars.tools.Helper;
import me.metallicgoat.specialItems.utils.XSound;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigValue {

    public static int tower_block_place_interval = 1;
    public static int tower_block_placed_per_interval = 2;
    public static Sound tower_place_place_sound = XSound.ENTITY_CHICKEN_EGG.parseSound();

    public static int silverfish_life_duration = 400;
    public static ConfigurationSection silverfish_life_display_name = null;

    public static int egg_bridger_max_length = 30;
    public static int egg_bridger_max_y_variation = 18;
    public static Sound egg_bridger_place_sound = XSound.ENTITY_CHICKEN_EGG.parseSound();

    public static Material ice_bridger_material = Helper.get().getMaterialByName("ICE");
    public static int ice_bridger_max_distance = 37;

}
