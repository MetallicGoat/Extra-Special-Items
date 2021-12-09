package me.metallicgoat.specialItems.items.icebridge;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.event.player.PlayerBuyInShopEvent;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItem;
import de.marcely.bedwars.api.game.specialitem.SpecialItemListener;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.Main;
import me.metallicgoat.specialItems.utils.XMaterial;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class RegisterIceBridger {
    public void registerItem() {
        SpecialItemListener listener = new SpecialItemListener() {

            @Override
            public void onShopBuy(PlayerBuyInShopEvent e) {
            }

            @Override
            public void onUse(PlayerUseSpecialItemEvent e) {
            }
        };

        assert XMaterial.ICE.parseMaterial() != null;
        SpecialItem specialItem = GameAPI.get().registerSpecialItem("ice-bridger", plugin(), "%eggBridgerItem%", new ItemStack(XMaterial.ICE.parseMaterial()));

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

                    IceBridgeUse iceBridge = new IceBridgeUse();
                    iceBridge.createIceBridge(e, session);

                    return session;
                }
            });
        } else {
            // id is already taken
            plugin().getLogger().info("WARNING: Another addon is probably using the 'ice-bridger' special item id");
        }
    }

    public static Main plugin(){
        return Main.getInstance();
    }
}
