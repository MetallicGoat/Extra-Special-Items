package me.metallicgoat.specialItems.config;

import de.marcely.bedwars.tools.Helper;
import de.marcely.bedwars.tools.Pair;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class ConfigValue {

  // Special
  public static boolean dye_tower_ukraine = false;

  // Tower
  public static String tower_icon_name = "PopUpTower";
  public static ItemStack tower_icon_material = Helper.get().parseItemStack("CHEST");
  public static Material tower_block_material = Helper.get().getMaterialByName("WHITE_WOOL");
  public static int tower_block_place_interval = 1;
  public static int tower_block_placed_per_interval = 2;
  public static Sound tower_place_place_sound = Helper.get().getSoundByName("ENTITY_CHICKEN_EGG");

  // Sliverfish
  public static String silverfish_icon_name = "Silverfish";
  public static ItemStack silverfish_icon_material = Helper.get().parseItemStack("SNOWBALL");
  public static double silverfish_damage = 1.5;
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
  public static ItemStack egg_bridger_icon_material = Helper.get().parseItemStack("EGG");
  public static Material egg_bridger_block_material = Helper.get().getMaterialByName("WHITE_WOOL");
  public static int egg_bridger_max_length = 30;
  public static int egg_bridger_max_y_variation = 18;
  public static Sound egg_bridger_place_sound = Helper.get().getSoundByName("ENTITY_CHICKEN_EGG");
  public static double egg_bridger_clutch_fall_damage_cap = 4;

  // Ice Bridger
  public static String ice_bridger_icon_name = "IceBridger";
  public static ItemStack ice_bridger_icon_material = Helper.get().parseItemStack("ICE");
  public static Material ice_bridger_material = Helper.get().getMaterialByName("ICE");
  public static int ice_bridger_max_distance = 37;

  // SlingShot
  public static String slingshot_icon_name = "SlingShot";
  public static ItemStack slingshot_icon_material = Helper.get().parseItemStack("MAGMA_CREAM");
  public static Material slingshot_material = Helper.get().getMaterialByName("MAGMA_CREAM");
  public static double slingshot_force = 2.0;
  public static double slingshot_height_boost = 0.5;
  public static double slingshot_max_y_boost = 1.0;
  public static int slingshot_cooldown_seconds = 7;
  public static int slingshot_cooldown_bars = 20;
  public static DecimalFormat slingshot_cooldown_seconds_format = new DecimalFormat("0.00s");
  public static String slingshot_cooldown_message = "&7You are in cooldown for &e{seconds} &7seconds";
  public static String slingshot_action_bar = "&f{item-name} &r{cooldown-bar} &f{seconds}";
  public static Sound slingshot_use_sound = Helper.get().getSoundByName("SLIME_WALK");

  // Endless Boost
  public static String endless_boost_icon_name = "EndlessBoost";
  public static ItemStack endless_boost_icon_material = Helper.get().parseItemStack("FIREWORK_ROCKET");
  public static Duration endless_boost_cooldown_duration = Duration.ofSeconds(2);

  // Command Item
  public static boolean command_item_enabled = false;
  public static HashMap<String, Pair<ItemStack, String>> command_item_player_commands = new HashMap<String, Pair<ItemStack, String>>() {{
    put("player-example", new Pair<>(new ItemStack(Material.STONE), "say a fancy player command"));
  }};
  public static HashMap<String, Pair<ItemStack, String>> command_item_console_commands = new HashMap<String, Pair<ItemStack, String>>() {{
    put("console-example", new Pair<>(new ItemStack(Material.STONE), "say a fancy console command"));
  }};

}
