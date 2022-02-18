package me.metallicgoat.specialItems.items.eggbridge;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import me.metallicgoat.specialItems.ExtraSpecialItems;
import org.bukkit.plugin.Plugin;

public class EggBridgerHandler {

    public static SpecialItemUseHandler getEggBridgeHandler(){
        return new SpecialItemUseHandler() {
            @Override
            public Plugin getPlugin() {
                return ExtraSpecialItems.getInstance();
            }

            @Override
            public SpecialItemUseSession openSession(PlayerUseSpecialItemEvent e) {
                final SpecialItemUseSession session = new SpecialItemUseSession(e) {
                    @Override
                    protected void handleStop() {
                    }
                };

                EggBridgeThrow bridgeThrow = new EggBridgeThrow();
                bridgeThrow.buildEggBridge(e, session);

                return session;
            }
        };
    }
}