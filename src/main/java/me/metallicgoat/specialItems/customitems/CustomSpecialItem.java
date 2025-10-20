package me.metallicgoat.specialItems.customitems;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItem;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import de.marcely.bedwars.tools.Pair;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.function.Function;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.api.ExtraSpecialItemType;
import me.metallicgoat.specialItems.config.ConfigValue;
import me.metallicgoat.specialItems.customitems.handlers.CommandItemHandler;
import me.metallicgoat.specialItems.customitems.handlers.EggBridgerHandler;
import me.metallicgoat.specialItems.customitems.handlers.IceBridgerHandler;
import me.metallicgoat.specialItems.customitems.handlers.SilverfishHandler;
import me.metallicgoat.specialItems.customitems.handlers.SlingShotHandler;
import me.metallicgoat.specialItems.customitems.handlers.TowerHandler;
import me.metallicgoat.specialItems.utils.Console;
import org.bukkit.inventory.ItemStack;

public class CustomSpecialItem {

  private final CustomSpecialItemUseHandler handler;
  private final ExtraSpecialItemType type;
  private final String itemId;
  private final String messagesFileId;
  private final ItemStack itemStack;

  public CustomSpecialItem(
      Function<PlayerUseSpecialItemEvent, CustomSpecialItemUseSession> factory,
      ExtraSpecialItemType type,
      String itemId,
      String messagesFileId,
      ItemStack itemStack) {

    this.handler = new CustomSpecialItemUseHandler(factory);
    this.type = type;
    this.itemId = itemId;
    this.messagesFileId = messagesFileId;
    this.itemStack = itemStack;
  }

  public static void registerAll() {
    register(new CustomSpecialItem(
        EggBridgerHandler::new,
        ExtraSpecialItemType.EGG_BRIDGER,
        "egg-bridger",
        ConfigValue.egg_bridger_icon_name,
        ConfigValue.egg_bridger_icon_material));

    register(new CustomSpecialItem(
        IceBridgerHandler::new,
        ExtraSpecialItemType.ICE_BRIDGER,
        "ice-bridger",
        ConfigValue.ice_bridger_icon_name,
        ConfigValue.ice_bridger_icon_material));

    register(new CustomSpecialItem(
        TowerHandler::new,
        ExtraSpecialItemType.TOWER,
        "tower",
        ConfigValue.tower_icon_name,
        ConfigValue.tower_icon_material));

    register(new CustomSpecialItem(
        SilverfishHandler::new,
        ExtraSpecialItemType.SILVERFISH,
        "silverfish",
        ConfigValue.silverfish_icon_name,
        ConfigValue.silverfish_icon_material));

    register(new CustomSpecialItem(
        SlingShotHandler::new,
        ExtraSpecialItemType.SLINGSHOT,
        "slingshot",
        ConfigValue.slingshot_icon_name,
        ConfigValue.slingshot_icon_material));

        /*
        register(new CustomSpecialItem(
                FloodEmpty.getFloodEmptyHandler(),
                "flooder",
                "Flood Filler",
                new ItemStack(Material.FLINT)));
         */

    if (ConfigValue.command_item_enabled) {
      loadCommandItems(ConfigValue.command_item_player_commands, false);
      loadCommandItems(ConfigValue.command_item_console_commands, true);
    }

    testAPI();
  }

  private static void loadCommandItems(HashMap<String, Pair<ItemStack, String>> map, boolean console) {
    if (map != null && !map.isEmpty()) {
      map.forEach((id, materialStringPair) -> {
        final ItemStack material = materialStringPair.getKey();
        register(new CustomSpecialItem(
            (event -> new CommandItemHandler(event, materialStringPair.getValue(), console)),
            ExtraSpecialItemType.COMMAND,
            id,
            "%" + id + "%", // TODO do I need these '%'?
            material));
      });
    }
  }

  private static void register(CustomSpecialItem item) {
    final SpecialItem specialItem = GameAPI.get().registerSpecialItem(item.getId(), ExtraSpecialItemsPlugin.getInstance(), item.getMessageFileId(), item.getItemStack());

    registerAPI(item.type, specialItem);

    if (specialItem != null) {
      specialItem.setHandler(item.handler());

    } else {
      final SpecialItem registeredItem = GameAPI.get().getSpecialItem(item.getId());

      // TODO better way to check?
      // Probably reloading bedwars or something
      if (registeredItem != null && registeredItem.getPlugin().getName().equals(ExtraSpecialItemsPlugin.getInstance().getName()))
        return;

      // id is already taken by some other addon
      Console.printSpecializedInfo("SpecialItem Registration Failed", "Another addon is probably using the " + item.getId() + " special item id");
    }
  }

  private static void registerAPI(ExtraSpecialItemType type, SpecialItem item) {
    if (type.hasMultipleItems())
      return;

    try {
      final Field f = ExtraSpecialItemType.class.getDeclaredField("specialItem");

      f.setAccessible(true);
      f.set(type, item);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void testAPI() {
    for (ExtraSpecialItemType type : ExtraSpecialItemType.values()) {
      if (type.getItem() == null)
        throw new IllegalStateException("Missing specialItem for " + type.name());
    }
  }

  public SpecialItemUseHandler handler() {
    return this.handler;
  }

  public String getId() {
    return this.itemId;
  }

  public ExtraSpecialItemType getType() {
    return this.type;
  }

  public String getMessageFileId() {
    return this.messagesFileId;
  }

  public ItemStack getItemStack() {
    return this.itemStack;
  }
}
