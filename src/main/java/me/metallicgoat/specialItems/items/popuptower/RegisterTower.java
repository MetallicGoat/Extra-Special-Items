package me.metallicgoat.specialItems.items.popuptower;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.event.player.PlayerBuyInShopEvent;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItem;
import de.marcely.bedwars.api.game.specialitem.SpecialItemListener;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class RegisterTower {
    public void registerItem() {
        SpecialItemListener listener = new SpecialItemListener() {
            @Override
            public void onShopBuy(PlayerBuyInShopEvent e) {
            }

            @Override
            public void onUse(PlayerUseSpecialItemEvent e) {
            }
        };

        SpecialItem specialItem = GameAPI.get().registerSpecialItem("tower", plugin(), "%towerItem%", new ItemStack(Material.CHEST));

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


                    TowerPlace towerPlace = new TowerPlace();
                    towerPlace.buildTower(e, session);

                    return session;
                }
            });
        } else {
            SpecialItem item = GameAPI.get().getSpecialItem("tower");

            if(item != null && item.getPlugin().getName().equals(Main.getInstance().getName()))
                return;

            // id is already taken
            plugin().getLogger().info("WARNING: Another addon is probably using the 'tower' special item id");
        }
    }

    public static Main plugin(){
        return Main.getInstance();
    }


}