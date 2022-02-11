package me.metallicgoat.specialItems.items.eggbridge;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.event.player.PlayerBuyInShopEvent;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItem;
import de.marcely.bedwars.api.game.specialitem.SpecialItemListener;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class RegisterEggBridger {
    public void registerItem() {
        SpecialItemListener listener = new SpecialItemListener() {

            @Override
            public void onShopBuy(PlayerBuyInShopEvent e) {
            }

            @Override
            public void onUse(PlayerUseSpecialItemEvent e) {
            }
        };

        SpecialItem specialItem = GameAPI.get().registerSpecialItem("egg-bridger", plugin(), "%eggBridgerItem%", new ItemStack(Material.EGG));

        if (specialItem != null) {
            specialItem.registerListener(listener);

            specialItem.setHandler(new SpecialItemUseHandler() {
                @Override
                public Plugin getPlugin() {
                    return plugin();
                }

                @Override
                public SpecialItemUseSession openSession(PlayerUseSpecialItemEvent e) {
                    SpecialItemUseSession session = new SpecialItemUseSession(e) {
                        @Override
                        protected void handleStop() {
                        }
                    };

                    EggBridgeThrow bridgeThrow = new EggBridgeThrow();
                    bridgeThrow.buildEggBridge(e, session);

                    return session;
                }
            });
        } else {
            SpecialItem item = GameAPI.get().getSpecialItem("egg-bridger");

            if(item != null && item.getPlugin().getName().equals(ExtraSpecialItems.getInstance().getName()))
                return;

            // id is already taken
            plugin().getLogger().info("WARNING: Another addon is probably using the 'egg-bridger' special item id");
        }
    }

    public static ExtraSpecialItems plugin(){
        return ExtraSpecialItems.getInstance();
    }
}