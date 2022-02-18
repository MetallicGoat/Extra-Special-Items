package me.metallicgoat.specialItems.items;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.game.specialitem.*;
import de.marcely.bedwars.tools.Helper;
import me.metallicgoat.specialItems.config.ConfigValue;
import me.metallicgoat.specialItems.items.customcommanditems.CommandItemHandler;
import me.metallicgoat.specialItems.utils.Console;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import me.metallicgoat.specialItems.items.eggbridge.EggBridgerHandler;
import me.metallicgoat.specialItems.items.icebridge.IceBridgerHandler;
import me.metallicgoat.specialItems.items.popuptower.TowerHandler;
import me.metallicgoat.specialItems.items.silverfish.SilverfishHandler;
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
                "%EggBridgerItem%",
                Helper.get().parseItemStack("EGG")));

        register(new CustomSpecialItem(
                IceBridgerHandler.getIceBridgeHandler(),
                "ice-bridger",
                "%IceBridgerItem%",
                Helper.get().parseItemStack("ICE")));

        register(new CustomSpecialItem(
                TowerHandler.getPopUpTowerHandler(),
                "tower",
                "%TowerItem%",
                Helper.get().parseItemStack("CHEST")));

        register(new CustomSpecialItem(
                SilverfishHandler.getSilverfishHandler(),
                "silverfish",
                "%SilverFishItem%",
                Helper.get().parseItemStack("SNOWBALL")));

        if(ConfigValue.command_item_enabled){
            if(ConfigValue.command_item_player_commands != null
                    && !ConfigValue.command_item_player_commands.isEmpty()){
                ConfigValue.command_item_player_commands.forEach((id, command) -> {
                    register(new CustomSpecialItem(
                            CommandItemHandler.getCustomItemHandler(command, false),
                            id,
                            "%" + id + "%",
                            Helper.get().parseItemStack("STONE")));
                });
            }

            if(ConfigValue.command_item_console_commands != null
                    && !ConfigValue.command_item_console_commands.isEmpty()){
                ConfigValue.command_item_console_commands.forEach((id, command) -> {
                    register(new CustomSpecialItem(
                            CommandItemHandler.getCustomItemHandler(command, true),
                            id,
                            "%" + id + "%",
                            Helper.get().parseItemStack("STONE")));
                });
            }
        }
    }

    private static void register(CustomSpecialItem item){

        final SpecialItem specialItem = GameAPI.get().registerSpecialItem(item.getId(), ExtraSpecialItems.getInstance(), item.getMessageFileId(), item.getItemStack());

        if (specialItem != null) {
            specialItem.setHandler(item.handler());

        } else {
            SpecialItem registeredItem = GameAPI.get().getSpecialItem(item.getId());

            // TODO better way to check?
            // Probably reloading bedwars or something
            if(registeredItem != null && registeredItem.getPlugin().getName().equals(ExtraSpecialItems.getInstance().getName()))
                return;

            // id is already taken by some other addon
            Console.printSpecializedInfo("SpecialItem Registration Failed", "Another addon is probably using the " + item.getId() + " special item id");
        }
    }
}
