package me.metallicgoat.specialItems.items.popuptower;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.ExtraSpecialItemsPlugin;
import org.bukkit.plugin.Plugin;

public class TowerHandler {

    public static SpecialItemUseHandler getPopUpTowerHandler(){
        return new SpecialItemUseHandler() {
            @Override
            public Plugin getPlugin() {
                return ExtraSpecialItemsPlugin.getInstance();
            }

            @Override
            public SpecialItemUseSession openSession(PlayerUseSpecialItemEvent event) {
                final SpecialItemUseSession session = new SpecialItemUseSession(event) {
                    @Override
                    protected void handleStop() {}
                };

                TowerPlace towerPlace = new TowerPlace();
                towerPlace.buildTower(event, session);

                return session;
            }
        };
    }
}