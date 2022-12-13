package me.metallicgoat.specialItems.customitems;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.*;
import de.marcely.bedwars.tools.Pair;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.config.ConfigValue;
import me.metallicgoat.specialItems.customitems.use.commanditems.CommandItemHandler;
import me.metallicgoat.specialItems.customitems.use.icebridge.IceBridgerHandler;
import me.metallicgoat.specialItems.customitems.use.popuptower.TowerHandler;
import me.metallicgoat.specialItems.customitems.use.silverfish.SilverfishHandler;
import me.metallicgoat.specialItems.utils.Console;
import me.metallicgoat.specialItems.customitems.use.eggbridge.EggBridgerHandler;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.function.Function;

public class CustomSpecialItem {

    private final SpecialItemUseHandler handler;
    private final String itemId;
    private final String messagesFileId;
    private final ItemStack itemStack;

    public CustomSpecialItem(
            Function<PlayerUseSpecialItemEvent, CustomSpecialItemUseSession> factory,
            String itemId,
            String messagesFileId,
            ItemStack itemStack){

        this.handler = new CustomSpecialItemUseHandler(factory);
        this.itemId = itemId;
        this.messagesFileId = messagesFileId;
        this.itemStack = itemStack;
    }

    public SpecialItemUseHandler handler(){
        return this.handler;
    }

    public String getId(){
        return this.itemId;
    }

    public String getMessageFileId(){
        return this.messagesFileId;
    }

    public ItemStack getItemStack(){
        return this.itemStack;
    }

    public static void registerAll(){
        register(new CustomSpecialItem(
                EggBridgerHandler::new,
                "egg-bridger",
                ConfigValue.egg_bridger_icon_name,
                ConfigValue.egg_bridger_icon_material));
        register(new CustomSpecialItem(
                IceBridgerHandler::new,
                "ice-bridger",
                ConfigValue.ice_bridger_icon_name,
                ConfigValue.ice_bridger_icon_material));

        register(new CustomSpecialItem(
                TowerHandler::new,
                "tower",
                ConfigValue.tower_icon_name,
                ConfigValue.tower_icon_material));

        register(new CustomSpecialItem(
                SilverfishHandler::new,
                "silverfish",
                ConfigValue.silverfish_icon_name,
                ConfigValue.silverfish_icon_material));

        /*
        register(new CustomSpecialItem(
                FloodEmpty.getFloodEmptyHandler(),
                "flooder",
                "Flood Filler",
                new ItemStack(Material.FLINT)));
         */

        if(ConfigValue.command_item_enabled){

            loadCommandItems(ConfigValue.command_item_player_commands, false);
            loadCommandItems(ConfigValue.command_item_console_commands, true);
        }
    }

    private static void loadCommandItems(HashMap<String, Pair<ItemStack, String>> map, boolean console){
        if(map != null && !map.isEmpty()){
            map.forEach((id, materialStringPair) -> {
                final ItemStack material = materialStringPair.getKey();
                register(new CustomSpecialItem(
                        (event -> new CommandItemHandler(event, materialStringPair.getValue(), console)),
                        id,
                        "%" + id + "%", // TODO do I need these '%'?
                        material));
            });
        }
    }

    private static void register(CustomSpecialItem item){

        final SpecialItem specialItem = GameAPI.get().registerSpecialItem(item.getId(), ExtraSpecialItemsPlugin.getInstance(), item.getMessageFileId(), item.getItemStack());

        if (specialItem != null) {
            specialItem.setHandler(item.handler());

        } else {
            final SpecialItem registeredItem = GameAPI.get().getSpecialItem(item.getId());

            // TODO better way to check?
            // Probably reloading bedwars or something
            if(registeredItem != null && registeredItem.getPlugin().getName().equals(ExtraSpecialItemsPlugin.getInstance().getName()))
                return;

            // id is already taken by some other addon
            Console.printSpecializedInfo("SpecialItem Registration Failed", "Another addon is probably using the " + item.getId() + " special item id");
        }
    }
}
