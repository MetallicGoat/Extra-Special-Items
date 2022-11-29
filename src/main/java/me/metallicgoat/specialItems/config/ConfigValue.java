package me.metallicgoat.specialItems.config;

import de.marcely.bedwars.tools.Helper;
import de.marcely.bedwars.tools.Pair;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigValue {

    // Special
    public static boolean dye_tower_ukraine = false;

    // Tower
    public static String tower_icon_name = "PopUpTower";
    public static Material tower_icon_material = Helper.get().getMaterialByName("CHEST");
    public static Material tower_block_material = Helper.get().getMaterialByName("WHITE_WOOL");
    public static int tower_block_place_interval = 1;
    public static int tower_block_placed_per_interval = 2;
    public static Sound tower_place_place_sound = Helper.get().getSoundByName("ENTITY_CHICKEN_EGG");

    // Sliverfish
    public static String silverfish_icon_name = "Silverfish";
    public static Material silverfish_icon_material = Helper.get().getMaterialByName("SNOWBALL");
    public static int silverfish_life_duration = 400;
    public static List<String> silverfish_name_tag = Arrays.asList(
            "{team-color}&l{team-name} {team-color}[■ ■ ■ ■ ■]",
            "{team-color}&l{team-name} {team-color}[■ ■ ■ ■ &7■]",
            "{team-color}&l{team-name} {team-color}[■ ■ ■ &7■ ■]",
            "{team-color}&l{team-name} {team-color}[■ ■ &7■ ■ ■]",
            "{team-color}&l{team-name} {team-color}[■ &7■ ■ ■ ■]"
    );

    // Egg Bridger
    public static String egg_bridger_icon_name = "EggBridger";
    public static Material egg_bridger_icon_material = Helper.get().getMaterialByName("EGG");
    public static Material egg_bridger_block_material = Helper.get().getMaterialByName("WHITE_WOOL");
    public static int egg_bridger_max_length = 30;
    public static int egg_bridger_max_y_variation = 18;
    public static Sound egg_bridger_place_sound = Helper.get().getSoundByName("ENTITY_CHICKEN_EGG");

    // Ice Bridger
    public static String ice_bridger_icon_name = "IceBridger";
    public static Material ice_bridger_icon_material = Helper.get().getMaterialByName("ICE");
    public static Material ice_bridger_material = Helper.get().getMaterialByName("ICE");
    public static int ice_bridger_max_distance = 37;

    // Command Item
    public static boolean command_item_enabled = false;
    public static HashMap<String, Pair<ItemStack, String>> command_item_player_commands = new HashMap<String, Pair<ItemStack, String>>(){{
        put("player-example", new Pair<>(new ItemStack(Material.STONE), "a fancy player command"));
    }};
    public static HashMap<String, Pair<ItemStack, String>> command_item_console_commands = new HashMap<String, Pair<ItemStack, String>>(){{
        put("console-example", new Pair<>(new ItemStack(Material.STONE), "a fancy console command"));
    }};

}
