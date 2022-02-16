package me.metallicgoat.specialItems.items.popuptower;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import org.bukkit.plugin.Plugin;

public class TowerHandler {

    public static SpecialItemUseHandler getPopUpTowerHandler(){
        return new SpecialItemUseHandler() {
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


                TowerPlace towerPlace = new TowerPlace();
                towerPlace.buildTower(e, session);

                return session;
            }
        };
    }
}