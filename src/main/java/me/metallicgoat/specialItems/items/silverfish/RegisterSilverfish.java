package me.metallicgoat.specialItems.items.silverfish;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.event.player.PlayerBuyInShopEvent;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItem;
import de.marcely.bedwars.api.game.specialitem.SpecialItemListener;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import me.metallicgoat.specialItems.utils.XMaterial;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class RegisterSilverfish {
    public void registerItem() {
        SpecialItemListener listener = new SpecialItemListener() {

            @Override
            public void onShopBuy(PlayerBuyInShopEvent e) {
            }

            @Override
            public void onUse(PlayerUseSpecialItemEvent e) {
            }
        };

        assert XMaterial.SNOWBALL.parseMaterial() != null;
        SpecialItem specialItem = GameAPI.get().registerSpecialItem("silverfish", plugin(), "%silverfishItem%", new ItemStack(XMaterial.SNOWBALL.parseMaterial()));

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

                    SilverfishThrow.throwSilverfish(e, session);

                    return session;
                }
            });
        } else {
            SpecialItem item = GameAPI.get().getSpecialItem("tower");

            if(item != null && item.getPlugin().getName().equals(ExtraSpecialItems.getInstance().getName()))
                return;

            // id is already taken
            plugin().getLogger().info("WARNING: Another addon is probably using the 'silverfish' special item id");
        }
    }

    public static ExtraSpecialItems plugin(){
        return ExtraSpecialItems.getInstance();
    }
}