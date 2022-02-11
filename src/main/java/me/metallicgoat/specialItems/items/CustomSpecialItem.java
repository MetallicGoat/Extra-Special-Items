package me.metallicgoat.specialItems.items;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.event.player.PlayerBuyInShopEvent;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItem;
import de.marcely.bedwars.api.game.specialitem.SpecialItemListener;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.Console;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CustomSpecialItem {

    private final SpecialItemType type;
    private final String itemId;
    private final String messagesFileId;
    private final ItemStack itemStack;

    public CustomSpecialItem(
            SpecialItemType type,
            String itemId,
            String messagesFileId,
            ItemStack itemStack){

        this.type = type;
        this.itemId = itemId;
        this.messagesFileId = messagesFileId;
        this.itemStack = itemStack;
    }

    public SpecialItemType getType(){
        return this.type;
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


    public static void register(CustomSpecialItem item){
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

            specialItem.setHandler(new SpecialItemUseHandler() {
                @Override
                public Plugin getPlugin() {
                    return ExtraSpecialItems.getInstance();
                }

                @Override
                public SpecialItemUseSession openSession(PlayerUseSpecialItemEvent e) {
                    SpecialItemUseSession session = new SpecialItemUseSession(e) {
                        @Override
                        protected void handleStop() {
                        }
                    };

                    // TODO
                    //SilverfishThrow.throwSilverfish(e, session);
                    return session;
                }
            });
        } else {
            SpecialItem registeredItem = GameAPI.get().getSpecialItem(item.getId());

            if(registeredItem != null && registeredItem.getPlugin().getName().equals(ExtraSpecialItems.getInstance().getName()))
                return;

            // id is already taken
            Console.printSpecializedInfo("Custom Item Registration Failed", "Another addon is probably using the " + item.getId() + " special item id");
        }
    }
}
