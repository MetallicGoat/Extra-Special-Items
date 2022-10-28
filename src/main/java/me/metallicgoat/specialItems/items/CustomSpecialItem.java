package me.metallicgoat.specialItems.items;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.game.specialitem.*;
import de.marcely.bedwars.tools.Helper;
import me.metallicgoat.specialItems.config.ConfigValue;
import me.metallicgoat.specialItems.items.customcommanditems.CommandItemHandler;
import me.metallicgoat.specialItems.utils.Console;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import me.metallicgoat.specialItems.items.eggbridge.EggBridgerHandler;
import me.metallicgoat.specialItems.items.icebridge.IceBridgerHandler;
import me.metallicgoat.specialItems.items.popuptower.TowerHandler;
import me.metallicgoat.specialItems.items.silverfish.SilverfishHandler;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomSpecialItem {

    private final SpecialItemUseHandler handler;
    private final String itemId;
    private final String messagesFileId;
    private final ItemStack itemStack;

    public CustomSpecialItem(
            SpecialItemUseHandler handler,
            String itemId,
            String messagesFileId,
            ItemStack itemStack){

        this.handler = handler;
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
                EggBridgerHandler.getEggBridgeHandler(),
                "egg-bridger",
                ConfigValue.egg_bridger_icon_name,
                new ItemStack(ConfigValue.egg_bridger_icon_material)));

        register(new CustomSpecialItem(
                IceBridgerHandler.getIceBridgeHandler(),
                "ice-bridger",
                ConfigValue.ice_bridger_icon_name,
                new ItemStack(ConfigValue.ice_bridger_icon_material)));

        register(new CustomSpecialItem(
                TowerHandler.getPopUpTowerHandler(),
                "tower",
                ConfigValue.tower_icon_name,
                new ItemStack(ConfigValue.tower_icon_material)));

        register(new CustomSpecialItem(
                SilverfishHandler.getSilverfishHandler(),
                "silverfish",
                ConfigValue.silverfish_icon_name,
                new ItemStack(ConfigValue.silverfish_icon_material)));

        if(ConfigValue.command_item_enabled){
            if(ConfigValue.command_item_player_commands != null
                    && !ConfigValue.command_item_player_commands.isEmpty()){
                ConfigValue.command_item_player_commands.forEach((id, materialStringPair) -> {
                    final Material material = materialStringPair.getKey();
                    register(new CustomSpecialItem(
                            CommandItemHandler.getCustomItemHandler(materialStringPair.getValue(), false),
                            id,
                            "%" + id + "%",
                            material != null ? new ItemStack(material) : Helper.get().parseItemStack("STONE")));
                });
            }

            if(ConfigValue.command_item_console_commands != null
                    && !ConfigValue.command_item_console_commands.isEmpty()){
                ConfigValue.command_item_console_commands.forEach((id, materialStringPair) -> {
                    final Material material = materialStringPair.getKey();
                    register(new CustomSpecialItem(
                            CommandItemHandler.getCustomItemHandler(materialStringPair.getValue(), true),
                            id,
                            "%" + id + "%",
                            material != null ? new ItemStack(material) : Helper.get().parseItemStack("STONE")));
                });
            }
        }
    }

    private static void register(CustomSpecialItem item){

        final SpecialItem specialItem = GameAPI.get().registerSpecialItem(item.getId(), ExtraSpecialItemsPlugin.getInstance(), item.getMessageFileId(), item.getItemStack());

        if (specialItem != null) {
            specialItem.setHandler(item.handler());

        } else {
            SpecialItem registeredItem = GameAPI.get().getSpecialItem(item.getId());

            // TODO better way to check?
            // Probably reloading bedwars or something
            if(registeredItem != null && registeredItem.getPlugin().getName().equals(ExtraSpecialItemsPlugin.getInstance().getName()))
                return;

            // id is already taken by some other addon
            Console.printSpecializedInfo("SpecialItem Registration Failed", "Another addon is probably using the " + item.getId() + " special item id");
        }
    }
}
