package me.metallicgoat.specialItems.items;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.event.player.PlayerBuyInShopEvent;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.*;
import de.marcely.bedwars.tools.Helper;
import me.metallicgoat.specialItems.Console;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import me.metallicgoat.specialItems.items.eggbridge.EggBridger;
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
                EggBridger.getEggBridgeHandler(),
                "egg-bridger",
                "%EggBridgerItem%",
                Helper.get().parseItemStack("EGG")));

        register(new CustomSpecialItem(
                null,
                "ice-bridger",
                "%IceBridgerItem%",
                Helper.get().parseItemStack("ICE")));

        register(new CustomSpecialItem(
                null,
                "tower",
                "%TowerItem%",
                Helper.get().parseItemStack("CHEST")));

        register(new CustomSpecialItem(
                null,
                "silverfish",
                "%SilverFishItem%",
                Helper.get().parseItemStack("SNOWBALL")));
    }

    private static void register(CustomSpecialItem item){
        SpecialItemListener listener = new SpecialItemListener() {
            @Override
            public void onShopBuy(PlayerBuyInShopEvent e) {
            }

            @Override
            public void onUse(PlayerUseSpecialItemEvent e) {
            }
        };

        SpecialItem specialItem = GameAPI.get().registerSpecialItem(item.getId(), ExtraSpecialItems.getInstance(), item.getMessageFileId(), item.getItemStack());

        if (specialItem != null) {
            specialItem.registerListener(listener);
            specialItem.setHandler(item.handler());

        } else {
            SpecialItem registeredItem = GameAPI.get().getSpecialItem(item.getId());

            // TODO better way?
            // Probably reloading bedwars or something
            if(registeredItem != null && registeredItem.getPlugin().getName().equals(ExtraSpecialItems.getInstance().getName()))
                return;

            // id is already taken by some other addon
            Console.printSpecializedInfo("SpecialItem Registration Failed", "Another addon is probably using the " + item.getId() + " special item id");
        }
    }
}
